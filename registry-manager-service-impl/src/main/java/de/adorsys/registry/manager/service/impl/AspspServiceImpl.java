package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

        ckeckAndUpdateUUID(aspsp);
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

        for (AspspBO aspsp: aspsps) {
            ckeckAndUpdateUUID(aspsp);
        }

        repository.saveAll(converter.toAspspPOList(aspsps));
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all ASPSPs");

        repository.deleteAll();
    }

    private void ckeckAndUpdateUUID(AspspBO aspsp) {
        if (aspsp.getId() == null) {
            aspsp.setId(UUID.randomUUID());
        }
    }
}
