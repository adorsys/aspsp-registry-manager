package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AspspRepositoryImpl implements AspspRepository {

    private final AspspJpaRepository repository;
    private final AspspEntityConverter converter;

    public AspspRepositoryImpl(AspspJpaRepository repository, AspspEntityConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    @Override
    public List<AspspPO> findAll() {
        return converter.toAspspPOList(repository.findAll());
    }

    @Override
    public Page<AspspPO> findByExample(AspspPO aspsp, Pageable pageable) {
        AspspEntity entity = converter.toAspspEntity(aspsp);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                                         .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                                         .withIgnoreCase()
                                         .withIgnoreNullValues();

        Page<AspspEntity> entities = repository.findAll(Example.of(entity, matcher), pageable);

        return new PageImpl<>(converter.toAspspPOList(entities.getContent()), entities.getPageable(), entities.getTotalElements());
    }

    @Override
    public Page<AspspPO> findByBankCode(String bankCode, Pageable pageable) {
        Page<AspspEntity> entities = repository.findByBankCode(bankCode, pageable);
        return new PageImpl<>(converter.toAspspPOList(entities.getContent()), entities.getPageable(), entities.getTotalElements());
    }

    @Override
    public AspspPO save(AspspPO aspsp) {
        AspspEntity entity = converter.toAspspEntity(aspsp);
        AspspEntity saved = repository.save(entity);
        return converter.toAspspPO(saved);
    }

    @Override
    public void saveAll(List<AspspPO> aspsps) {
        repository.saveAll(converter.toAspspEntityList(aspsps));
    }

    @Override
    public void deleteById(UUID aspspId) {
        repository.deleteById(aspspId);
    }

    @Override
    public void delete() {
        repository.deleteAll();
    }

    public void delete(List<AspspPO> aspsps) {
        repository.deleteAll(converter.toAspspEntityList(aspsps));
    }
}
