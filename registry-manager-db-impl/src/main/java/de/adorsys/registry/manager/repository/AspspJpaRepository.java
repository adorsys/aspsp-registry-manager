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

    /**
     * Looks for and returns a found entity from the database by input params
     * <p>
     * Searches for {@link AspspEntity} that has BIC and Bank Code specified
     * in method parameters
     *
     * @param bic
     * @param bankCode
     * @return AspspEntity object
     */
    AspspEntity findOneByBicAndBankCode(String bic, String bankCode);
}
