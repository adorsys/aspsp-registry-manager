package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.repository.model.PagePO;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AspspRepositoryImpl implements AspspRepository {
    private static final String BIC_FIELD_NAME = "bic";
    private static final String BANK_CODE_FIELD_NAME = "bankCode";
    private static final String NAME_FIELD_NAME = "name";

    private final AspspJpaRepository repository;
    private final AspspEntityConverter converter;

    public AspspRepositoryImpl(AspspJpaRepository repository, AspspEntityConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public List<AspspPO> findAll() {
        return converter.toAspspPOList(repository.findAllByOrderByIdAsc());
    }

    @Override
    public PagePO findByExample(AspspPO aspsp, int page, int size) {
        AspspEntity entity = converter.toAspspEntity(aspsp);

        ExampleMatcher matcher = ExampleMatcher.matchingAny()
                                         .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                                         .withIgnoreCase()
                                         .withIgnoreNullValues();

        Page<AspspEntity> entities = repository.findAll(
                Example.of(entity, matcher),
                PageRequest.of(page, size, Sort.by(
                        Sort.Order.by(BIC_FIELD_NAME).nullsLast(),
                        Sort.Order.by(BANK_CODE_FIELD_NAME).nullsLast(),
                        Sort.Order.by(NAME_FIELD_NAME).nullsLast())
                )
        );

        return new PagePO(converter.toAspspPOList(entities.getContent()), entities.getTotalElements());
    }

    @Override
    public PagePO findByBankCode(String bankCode, int page, int size) {
        Page<AspspEntity> entities = repository.findByBankCode(bankCode, PageRequest.of(page, size));
        return new PagePO(converter.toAspspPOList(entities.getContent()), entities.getTotalElements());
    }

    @Override
    public AspspPO save(AspspPO aspsp) {
        AspspEntity entity = converter.toAspspEntity(aspsp);
        checkPresenceByAspspId(entity);
        AspspEntity saved = repository.save(entity);
        return converter.toAspspPO(saved);
    }

    private void checkPresenceByAspspId(AspspEntity entity) {
        AspspEntity checker = repository.findByAspspId(entity.getAspspId());

        if (checker != null) {
            entity.setId(checker.getId());
        }
    }

    @Override
    public void saveAll(List<AspspPO> aspsps) {
        repository.saveAll(converter.toAspspEntityList(aspsps));
    }

    @Override
    public void deleteById(UUID aspspId) {
        repository.deleteByAspspId(aspspId);
    }

    @Override
    public void delete() {
        repository.deleteAll();
    }

    @Override
    public void delete(List<AspspPO> aspsps) {
        repository.deleteAll(converter.toAspspEntityList(aspsps));
    }

    @Override
    public long count() {
        return repository.count();
    }
}
