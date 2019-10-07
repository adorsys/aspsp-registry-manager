package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AspspRepositoryImpl implements AspspRepository {

    private final AspspJpaRepository repository;
    private final AspspEntityConverter converter;

    public AspspRepositoryImpl(AspspJpaRepository repository, AspspEntityConverter converter) {
        this.repository = repository;
        this.converter = converter;
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
    public void deleteById(String aspspId) {
        repository.deleteById(aspspId);
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }
}
