package de.adorsys.registry.manager.repository;

import de.adorsys.registry.manager.repository.model.AspspEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AspspJpaRepository extends PagingAndSortingRepository<AspspEntity, String> {
}
