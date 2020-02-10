package de.adorsys.registry.manager.repository;

import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.repository.model.PagePO;

import java.util.List;
import java.util.UUID;

public interface AspspRepository {

    List<AspspPO> findAll();

    /**
     * Looks for ASPSPs by {@link AspspPO} entity example starting from the provided
     * page and returns the output of the provided size
     *
     * @param aspsp
     * @param page
     * @param size
     * @return {@link PagePO} object that holds a list of ASPSPs entities
     * and the total element number of the search results
     */
    PagePO findByExample(AspspPO aspsp, int page, int size);

    /**
     * Looks for ASPSPs by the provided Bank Code starting from the provided
     * page and returns the output of the provided size
     *
     * @param bankCode
     * @param page
     * @param size
     * @return {@link PagePO} object that holds a list of ASPSPs entities
     * and the total element number of the search results
     */
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

    /**
     * Counts total number of ASPSPs entities persisting in the database
     *
     * @return total number of elements in the repository
     */
    long count();

    /**
     * Returns a {@link PagePO} with results of exact matching the provided example
     * entity
     *
     * @param example for lookup
     * @param page starting page for lookup
     * @param size the maximum size of output
     * @return a PagePO entity with results of lookup
     */

    PagePO findExactByExample(AspspPO example, int page, int size);
}
