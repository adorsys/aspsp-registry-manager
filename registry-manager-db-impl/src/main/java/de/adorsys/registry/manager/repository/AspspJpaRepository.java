package de.adorsys.registry.manager.repository;

import de.adorsys.registry.manager.repository.model.AspspEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AspspJpaRepository extends JpaRepository<AspspEntity, UUID> {

    List<AspspEntity> findByBankCode(String bankCode, Pageable pageable);
}
