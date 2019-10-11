package de.adorsys.registry.manager.repository;

import de.adorsys.registry.manager.repository.model.AspspEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AspspJpaRepository extends JpaRepository<AspspEntity, UUID> {

    /**
     * Looks for ASPSPs by Bank Code with correlating results with provided {@link Pageable}
     * options
     *
     * @param bankCode
     * @param pageable constructed of, at least, a page number and a size for the
     *                 maximum entries in the result set
     * @return {@link Page} object that contains a list of ASPSPs with additional
     * information related to the pagination (page, size, order, etc.)
     */
    Page<AspspEntity> findByBankCode(String bankCode, Pageable pageable);
}
