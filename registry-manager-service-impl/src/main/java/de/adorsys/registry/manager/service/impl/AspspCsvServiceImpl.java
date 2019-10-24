package de.adorsys.registry.manager.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.AspspCsvService;
import de.adorsys.registry.manager.service.converter.AspspCsvRecordConverter;
import de.adorsys.registry.manager.service.model.AspspCsvRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AspspCsvServiceImpl implements AspspCsvService {
    private static final Logger logger = LoggerFactory.getLogger(AspspCsvServiceImpl.class);
    private static final String ASPSP_NAME_COLUMN_NAME = "aspspName";
    private static final ObjectReader CSV_OBJECT_READER;

    private final AspspRepository aspspRepository;
    private final AspspCsvRecordConverter csvRecordConverter;
    private final UUIDGeneratorService uuidGeneratorService;

    static {
        CSV_OBJECT_READER = new CsvMapper()
                                    .readerWithTypedSchemaFor(AspspCsvRecord.class)
                                    .withHandler(new DeserializationProblemHandler() {
                                        @Override
                                        public Object handleWeirdStringValue(DeserializationContext ctxt, Class<?> targetType, String valueToConvert, String failureMsg) {
                                            if (targetType.isEnum()) {
                                                return Enum.valueOf((Class<Enum>) targetType, valueToConvert.trim().toUpperCase());
                                            }

                                            return DeserializationProblemHandler.NOT_HANDLED;
                                        }
                                    });
    }

    public AspspCsvServiceImpl(AspspRepository aspspRepository, AspspCsvRecordConverter csvRecordConverter, UUIDGeneratorService uuidGeneratorService) {
        this.aspspRepository = aspspRepository;
        this.csvRecordConverter = csvRecordConverter;
        this.uuidGeneratorService = uuidGeneratorService;
    }

    @Override
    public byte[] exportCsv() {
        List<AspspPO> pos = aspspRepository.findAll();

        return pos.stream()
                       .map(csvRecordConverter::toAspspCsvRecord)
                       .map(this::toCsvString)
                       .collect(Collectors.joining())
                       .getBytes();
    }

    @Override
    public void importCsv(byte[] file) {
        List<AspspPO> aspsps = readAllRecords(file);
        uuidGeneratorService.checkAndUpdateUUID(aspsps);
        aspspRepository.deleteAll();
        aspspRepository.saveAll(aspsps);
    }

    @Override
    public void deserializeAndMerge(byte[] file) {
        List<AspspPO> input = readAllRecords(file);
        List<AspspPO> database = aspspRepository.findAll();
        List<AspspPO> forUpdate = new LinkedList<>(), forSave = new LinkedList<>();

        input.forEach(in -> {
            if (in.getId() != null) {
//                match on BIC and BLZ but NOT Id
                database.stream().filter(element -> in.getBic().equals(element.getBic()) && in.getBankCode().equals(element.getBankCode()) && !in.getId().equals(element.getId()))
                    .forEach(element -> {
                        AspspPO copy = copyContent(in);
                        forUpdate.add(copy);
                        input.remove(in);
                    });
                forSave.add(in);
            } else {
//                match on BIC and BLZ, input id is NULL
                database.stream().filter(element -> in.getBic().equals(element.getBic()) && in.getBankCode().equals(element.getBankCode()))
                    .forEach(element -> {
                        AspspPO copy = copyContent(in);
                        copy.setId(element.getId());
                        forSave.add(copy);
                        input.remove(in);
                    });
//                no match, input id is NULL
                forSave.add(in);
            }
        });

//        everything that has been matched by id, no match by id (new entries), id is NULL and matched by BIC and BLZ and no match with NULL id (new entries)
        uuidGeneratorService.checkAndUpdateUUID(forSave);
        aspspRepository.saveAll(forSave);

//        everything that has been matched by BIC and BLZ but not by id (id is NOT NULL)
        aspspRepository.deleteAll(forUpdate);
        uuidGeneratorService.checkAndUpdateUUID(forUpdate);
        aspspRepository.saveAll(forUpdate);
    }

    private AspspPO copyContent(AspspPO from) {
        AspspPO copy = new AspspPO();

        copy.setId(from.getId());
        copy.setName(from.getName());
        copy.setBic(from.getBic());
        copy.setUrl(from.getUrl());
        copy.setBankCode(from.getBankCode());
        copy.setAdapterId(from.getAdapterId());
        copy.setIdpUrl(from.getIdpUrl());
        copy.setScaApproaches(from.getScaApproaches());

        return copy;
    }

    private String toCsvString(AspspCsvRecord aspsp) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(AspspCsvRecord.class).withoutQuoteChar();

        if (aspsp.getAspspName() == null) {
            aspsp.setAspspName("");
        }

        if (aspsp.getAspspName().contains(",")) {
            int nameColumnIndex = schema.column(ASPSP_NAME_COLUMN_NAME).getIndex();
            schema = mapper.configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true)
                             .schemaFor(AspspCsvRecord.class)
                             .rebuild()
                             .replaceColumn(nameColumnIndex, new CsvSchema.Column(nameColumnIndex, ASPSP_NAME_COLUMN_NAME))
                             .build();
        }

        try {
            return mapper.writer(schema).writeValueAsString(aspsp);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<AspspPO> readAllRecords(byte[] csv) {
        List<AspspCsvRecord> aspsps;
        try {
            aspsps = CSV_OBJECT_READER
                             .<AspspCsvRecord>readValues(csv)
                             .readAll();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return csvRecordConverter.toAspspPOList(aspsps);
    }
}
