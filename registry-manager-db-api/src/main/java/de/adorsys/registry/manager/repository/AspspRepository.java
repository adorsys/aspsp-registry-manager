package de.adorsys.registry.manager.repository;

import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.repository.model.PagePO;

import java.util.List;
import java.util.UUID;

public interface AspspRepository {

    List<AspspPO> findAll();

    PagePO findByExample(AspspPO aspsp, int page, int size);

    PagePO findByBankCode(String bankCode, int page, int size);

    AspspPO save(AspspPO aspsp);

    /**
     * Saves a list of aspsps into existing database.
     * <p>
     * Writes all Aspsp objects into the current repository. The iteration through input
     * collection of aspsps is taken place, each object is either added or updated, if it
     * already exists within the repository. New random id is generated if input entity
     * id is NULL.
     *
     * @param aspsps, a list of aspsp objects to be added into the repository
     */
    void saveAll(List<AspspPO> aspsps);

    void deleteById(UUID aspspId);

    /**
     * Deletes all records from the existing database.
     */
    void delete();

    /**
     * Deletes a list of {@link AspspPO} from the existing database.
     *
     * @param aspsps, a list of objects
     */
    void delete(List<AspspPO> aspsps);
}
