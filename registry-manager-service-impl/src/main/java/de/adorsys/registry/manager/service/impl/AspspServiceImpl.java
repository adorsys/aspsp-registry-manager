package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.repository.model.PagePO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.exception.IbanException;
import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.PageBO;
import org.iban4j.Iban;
import org.iban4j.Iban4jException;
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
    private final UUIDGeneratorService uuidGeneratorService;

    public AspspServiceImpl(AspspRepository repository, AspspBOConverter converter, UUIDGeneratorService uuidGeneratorService) {
        this.repository = repository;
        this.converter = converter;
        this.uuidGeneratorService = uuidGeneratorService;
    }

    @Override
    public PageBO getByAspsp(AspspBO aspsp, int page, int size) {
        logger.info("Trying to get ASPSPs by name [{}], bic [{}] and bankCode [{}]",
                aspsp.getName(), aspsp.getBic(), aspsp.getBankCode());

        AspspPO po = converter.toAspspPO(aspsp);
        PagePO pagePO = repository.findByExample(po, page, size);

        return new PageBO(converter.toAspspBOList(pagePO.getContent()), pagePO.getTotalElements());
    }

    @Override
    public PageBO getByIban(String iban, int page, int size) {
        logger.info("Trying to get ASPSPs by IBAN {}", iban);

        String bankCode;

        try {
            bankCode = Iban.valueOf(iban).getBankCode();
        } catch (Iban4jException e) {
            throw new IbanException(e.getMessage());
        }

        if (bankCode == null) {
            throw new IbanException("Failed to extract the bank code from the IBAN");
        }

        PagePO pagePO = repository.findByBankCode(bankCode, page, size);

        return new PageBO(converter.toAspspBOList(pagePO.getContent()), pagePO.getTotalElements());
    }

    @Override
    public AspspBO save(AspspBO aspsp) {
        logger.info("Trying to save ASPSP {}", aspsp);

        AspspPO po = converter.toAspspPO(aspsp);
        AspspPO saved = repository.save(uuidGeneratorService.checkAndUpdateUUID(po));

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

        List<AspspPO> aspspPOList = converter.toAspspPOList(aspsps);

        repository.saveAll(uuidGeneratorService.checkAndUpdateUUID(aspspPOList));
    }

    @Override
    public void deleteAll() {
        logger.info("Deleting all ASPSPs");

        repository.delete();
    }

    @Override
    public long count() {
        logger.info("Counting all available ASPSPs");

        return repository.count();
    }
}
