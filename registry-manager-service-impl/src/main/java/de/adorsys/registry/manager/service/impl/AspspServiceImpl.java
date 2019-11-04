package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.exception.IbanException;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.iban4j.Iban;
import org.iban4j.Iban4jException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Page<AspspBO> getByAspsp(AspspBO aspsp, Pageable pageable) {
        logger.info("Trying to get ASPSPs by name [{}], bic [{}] and bankCode [{}]",
                aspsp.getName(), aspsp.getBic(), aspsp.getBankCode());

        AspspPO po = converter.toAspspPO(aspsp);
        Page<AspspPO> pos = repository.findByExample(po, pageable);

        return new PageImpl<>(converter.toAspspBOList(pos.get().collect(Collectors.toList())), pos.getPageable(), pos.getTotalElements());
    }

    @Override
    public Page<AspspBO> getByIban(String iban, Pageable pageable) {
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

        Page<AspspPO> pos = repository.findByBankCode(bankCode, pageable);

        return new PageImpl<>(converter.toAspspBOList(pos.get().collect(Collectors.toList())), pos.getPageable(), pos.getTotalElements());
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
}
