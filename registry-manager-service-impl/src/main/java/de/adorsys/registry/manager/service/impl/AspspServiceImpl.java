package de.adorsys.registry.manager.service.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.UUID;

@Service
public class AspspServiceImpl implements AspspService {
    private static final Logger logger = LoggerFactory.getLogger(AspspServiceImpl.class);

    private final AspspRepository repository;
    private final AspspBOConverter converter;

    public AspspServiceImpl(AspspRepository repository, AspspBOConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public AspspBO save(AspspBO aspsp) {
        logger.info("Trying to save ASPSP {}", aspsp);

        checkAndUpdateUUID(aspsp);
        AspspPO po = converter.toAspspPO(aspsp);
        AspspPO saved = repository.save(po);

        return converter.toAspspBO(saved);
    }

    @Override
    public void deleteById(UUID aspspId) {
        logger.info("Deleting ASPSP by id={}", aspspId);

        repository.deleteById(aspspId);
    }

    @Override
    public void saveAll(List<AspspBO> aspsps) {
        logger.info("Trying to save ASPSPs {}", aspsps);

        aspsps.forEach(this::checkAndUpdateUUID);

        repository.saveAll(converter.toAspspPOList(aspsps));
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all ASPSPs");

        repository.deleteAll();
    }

    @Override
    public void convertAndSaveAll(byte[] csv) {
        logger.info("Converting file into ASPSP objects");
        List<AspspBO> aspsps = readAllRecords(csv);

        saveAll(aspsps);
    }

    private void checkAndUpdateUUID(AspspBO aspsp) {
        if (aspsp.getId() == null) {
            aspsp.setId(UUID.randomUUID());
        }
    }

    private List<AspspBO> readAllRecords(byte[] csv) {
        ObjectReader objectReader = new CsvMapper()
            .readerWithTypedSchemaFor(AspspBO.class)
            .withHandler(new DeserializationProblemHandler() {
                @Override
                public Object handleWeirdStringValue(DeserializationContext ctxt, Class<?> targetType, String valueToConvert, String failureMsg) {
                    if (targetType.isEnum()) {
                        return Enum.valueOf((Class<Enum>) targetType, valueToConvert.trim().toUpperCase());
                    }

                    return DeserializationProblemHandler.NOT_HANDLED;
                }
            });

        List<AspspBO> aspsps;
        try {
            aspsps = objectReader
                .<AspspBO>readValues(csv)
                .readAll();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return aspsps;
    }
}
