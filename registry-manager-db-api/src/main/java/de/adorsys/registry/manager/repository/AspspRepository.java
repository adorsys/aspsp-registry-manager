package de.adorsys.registry.manager.repository;

import de.adorsys.registry.manager.repository.model.AspspPO;

import java.util.List;
import java.util.UUID;

public interface AspspRepository {

    AspspPO save(AspspPO aspsp);

    /**
     * Saves a list of {@link AspspPO} into existing database.
     * <p>
     * Writes all AspspPO objects into the current repository. Iterates through the input
     * collection of aspsps, converts those into {@link AspspEntity},
     * each entry is either added or updated, if it already exists within the repository.
     *
     * @param aspsps a list of AspspPO objects to be added into the repository
     */
    void saveAll(List<AspspPO> aspsps);

    void deleteById(UUID aspspId);

    /**
     * Deletes all records from the database.
     */
    void deleteAll();
}
