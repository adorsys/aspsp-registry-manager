package de.adorsys.registry.manager.service;


import de.adorsys.registry.manager.service.model.AspspBO;

import java.util.List;
import java.util.UUID;

public interface AspspService {

    AspspBO save(AspspBO aspsp);

    void saveAll(List<AspspBO> aspsps);

    void deleteById(UUID aspspId);

    void deleteAll();

    /**
     * Converts array of bytes, passed from a front-end, into a list of {@link AspspBO} records
     * and sends that list for further processing into data layer.
     * <p>
     * Converts an input file bytes into list of ASPSPs and passes it into
     * {@link AspspRepository} for saving and/or updating existing records within database.
     *
     * @param csv as an array of bytes received from a front-end.
     */
    void convertAndSaveAll(byte[] csv);
}
