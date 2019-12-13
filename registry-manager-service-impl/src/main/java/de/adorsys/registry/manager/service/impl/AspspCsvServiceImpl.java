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
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.converter.AspspCsvRecordConverter;
import de.adorsys.registry.manager.service.model.*;
import de.adorsys.registry.manager.service.validator.AspspValidationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class AspspCsvServiceImpl implements AspspCsvService {
    private static final ObjectReader CSV_OBJECT_READER;

    private final AspspRepository aspspRepository;
    private final AspspCsvRecordConverter csvRecordConverter;
    private final AspspBOConverter aspspBOConverter;
    private final UUIDGeneratorService uuidGeneratorService;
    private final AspspValidationService aspspValidationService;

    static {
        CsvMapper csvMapper = new CsvMapper();
        CSV_OBJECT_READER = csvMapper
                                    .readerFor(AspspCsvRecord.class)
                                    .with(csvMapper.typedSchemaFor(AspspCsvRecord.class).withNullValue(""))
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

    public AspspCsvServiceImpl(AspspRepository aspspRepository, AspspCsvRecordConverter csvRecordConverter,
                               AspspBOConverter aspspBOConverter, UUIDGeneratorService uuidGeneratorService,
                               AspspValidationService aspspValidationService) {
        this.aspspRepository = aspspRepository;
        this.csvRecordConverter = csvRecordConverter;
        this.aspspBOConverter = aspspBOConverter;
        this.uuidGeneratorService = uuidGeneratorService;
        this.aspspValidationService = aspspValidationService;
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
    public CsvFileImportValidationReportBO validateImportCsv(byte[] file) {
        List<AspspBO> aspsps = readAllRecords(file, aspspBOConverter::csvRecordListToAspspBOList);

        long csvFileRecordsNumber = aspsps.size();
        long dbRecordsNumber = aspspRepository.count();
        FileValidationReportBO fileValidationReport = aspspValidationService.validate(aspsps);

        return new CsvFileImportValidationReportBO(csvFileRecordsNumber, dbRecordsNumber, fileValidationReport);
    }

    @Override
    public void importCsv(byte[] file) {
        List<AspspPO> aspsps = readAllRecords(file, csvRecordConverter::toAspspPOList);
        aspspRepository.delete();
        aspspRepository.saveAll(uuidGeneratorService.checkAndUpdateUUID(aspsps));
    }

    @Override
    public CsvFileMergeValidationReportBO validateMergeCsv(byte[] file) {
        List<AspspBO> input = readAllRecords(file, aspspBOConverter::csvRecordListToAspspBOList);
        FileValidationReportBO fileValidationReport = aspspValidationService.validate(input);

        List<AspspBO> database = aspspBOConverter.toAspspBOList(aspspRepository.findAll());
        Set<AspspBO> difference = new HashSet<>();
        int numberOfNewRecords = 0;

        for (AspspBO inputItem : input) {
            boolean matched = false;

            for (AspspBO dbItem : database) {
                if (Objects.equals(inputItem, dbItem)) {
                    matched = true;
                    break;
                }

                if (isAspspModified(inputItem, dbItem)) {
                    difference.add(inputItem);
                    matched = true;
                    break;
                }
            }

            if (!matched) {
                numberOfNewRecords++;
            }
        }

        return new CsvFileMergeValidationReportBO(numberOfNewRecords, difference, fileValidationReport);
    }

    private boolean isAspspModified(AspspBO inputItem, AspspBO dbItem) {
        return Objects.equals(inputItem.getBankCode(), dbItem.getBankCode())
                       && Objects.equals(inputItem.getBic(), dbItem.getBic())
                       && !(Objects.equals(inputItem.getId(), dbItem.getId())
                                    && Objects.equals(inputItem.getAdapterId(), dbItem.getAdapterId())
                                    && Objects.equals(inputItem.getName(), dbItem.getName())
                                    && Objects.equals(inputItem.getUrl(), dbItem.getUrl())
                                    && Objects.equals(inputItem.getIdpUrl(), dbItem.getIdpUrl())
                                    && Objects.equals(inputItem.getScaApproaches(), dbItem.getScaApproaches()));
    }

    @Override
    public void merge(byte[] file) {
        List<AspspPO> input = readAllRecords(file, csvRecordConverter::toAspspPOList);
        List<AspspPO> database = aspspRepository.findAll();
        List<AspspPO> forDeleting = new LinkedList<>();
        Set<AspspPO> forSave = new HashSet<>();

        database.forEach(dbItem -> {
            input.forEach(inputItem -> {
                if (inputItem.getId() == null) {
                    logicForProcessingWithNullId(input, forSave, dbItem, inputItem);
//                no match by id, but match by BIC and BLZ
                } else if (areBicAndBlzEqualWithDifferentId(dbItem, inputItem)) {
                    forDeleting.add(dbItem);
                }

//                no match, match by id, or id is NULL and no match by BIC and BLZ
                forSave.add(inputItem);
            });
        });

//        everything that has been matched by BIC and BLZ but not by id (id is NOT NULL)
        aspspRepository.delete(forDeleting);

//        everything that has been matched by id, no match by id (new entries), id is NULL and matched by BIC and BLZ and no match with NULL id (new entries)
        aspspRepository.saveAll(uuidGeneratorService.checkAndUpdateUUID(new LinkedList<>(forSave)));
    }

    private void logicForProcessingWithNullId(List<AspspPO> input, Set<AspspPO> forSave, AspspPO dbItem, AspspPO inputItem) {
//        input id is NULL, but match by BIC and BLZ
        if (areBicAndBlzEqual(dbItem, inputItem)) {
            AspspPO copy = copyContent(inputItem);
            copy.setId(dbItem.getId());
            forSave.add(copy);
            input.remove(inputItem);
        }
    }

    private boolean areBicAndBlzEqual(AspspPO dbItem, AspspPO inputItem) {
        return inputItem.getBic().equalsIgnoreCase(dbItem.getBic()) && inputItem.getBankCode().equals(dbItem.getBankCode());
    }

    private boolean areBicAndBlzEqualWithDifferentId(AspspPO dbItem, AspspPO inputItem) {
        return !inputItem.getId().equals(dbItem.getId()) && areBicAndBlzEqual(dbItem, inputItem);
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
        CsvMapper mapper = new CsvMapper().configure(CsvGenerator.Feature.STRICT_CHECK_FOR_QUOTING, true);
        CsvSchema schema = mapper.schemaFor(AspspCsvRecord.class);

        try {
            return mapper.writer(schema).writeValueAsString(aspsp);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }

    private <T> List<T> readAllRecords(byte[] csv, Function<List<AspspCsvRecord>, List<T>> converter) {
        List<AspspCsvRecord> aspsps;
        try {
            aspsps = CSV_OBJECT_READER
                             .<AspspCsvRecord>readValues(csv)
                             .readAll();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return converter.apply(aspsps);
    }
}
