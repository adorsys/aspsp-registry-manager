package de.adorsys.registry.manager.service;


import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.PageBO;

import java.util.List;
import java.util.UUID;

public interface AspspService {

    /**
     * Calls for {@link AspspRepository} findByExample(...) and returns results
     * from that request converted into appropriate entity.
     *
     * @param aspsp
     * @param page
     * @param size
     * @return {@link PageBO} object that holds a list of ASPSPs entities
     * and the total element number of the search results
     */
    PageBO getByAspsp(AspspBO aspsp, int page, int size);

    /**
     * Extracts Bank Code from Iban and calls for {@link AspspRepository} findByBankCode(...)
     * and returns results from that request converted into appropriate entity.
     *
     * @param iban
     * @param page
     * @param size
     * @return {@link PageBO} object that holds a list of ASPSPs entities
     * and the total element number of the search results
     * @throws {@link de.adorsys.registry.manager.service.exception.IbanException} if
     * extracting Bank Code fails or extracted Bank Code is NULL
     */
    PageBO getByIban(String iban, int page, int size);

    AspspBO save(AspspBO aspsp);

    /**
     * Checks if an incoming Aspsp record is already represented in the database
     *
     * @param aspsp to check
     * @return result if there are any duplicates
     */
    AspspBO checkNewAspsp(AspspBO aspsp);

    void saveAll(List<AspspBO> aspsps);

    void deleteById(UUID aspspId);

    void deleteAll();

    /**
     * Return the total number of ASPSPs currently recorded in the database
     *
     * @return integer, representing total elements in the database
     */
    long count();
}
