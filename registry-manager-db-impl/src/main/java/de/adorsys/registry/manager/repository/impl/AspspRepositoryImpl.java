package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
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
    public List<AspspPO> findByExample(AspspPO aspsp, int page, int size) {
        AspspEntity entity = converter.toAspspEntity(aspsp);

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                                         .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                                         .withIgnoreCase()
                                         .withIgnoreNullValues();

        List<AspspEntity> entities = repository.findAll(Example.of(entity, matcher), PageRequest.of(page, size))
                                                 .getContent();

        return converter.toAspspPOList(entities);
    }

    @Override
    public List<AspspPO> findByBankCode(String bankCode, int page, int size) {
        List<AspspEntity> entities = repository.findByBankCode(bankCode, PageRequest.of(page, size));
        return converter.toAspspPOList(entities);
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
