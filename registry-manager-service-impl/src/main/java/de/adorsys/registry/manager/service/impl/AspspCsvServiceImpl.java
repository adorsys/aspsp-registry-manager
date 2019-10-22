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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AspspCsvServiceImpl implements AspspCsvService {
    private static final Logger logger = LoggerFactory.getLogger(AspspCsvServiceImpl.class);
    private static final String ASPSP_NAME_COLUMN_NAME = "aspspName";
    private static final ObjectReader CSV_OBJECT_READER;

    private final AspspRepository aspspRepository;
    private final AspspCsvRecordConverter csvRecordConverter;

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

    public AspspCsvServiceImpl(AspspRepository aspspRepository, AspspCsvRecordConverter csvRecordConverter) {
        this.aspspRepository = aspspRepository;
        this.csvRecordConverter = csvRecordConverter;
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
        aspspRepository.deleteAll();
        aspspRepository.saveAll(aspsps);
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
            logger.warn("Exception occurred while indexes were being written into a CSV: {}", e.getMessage());
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
