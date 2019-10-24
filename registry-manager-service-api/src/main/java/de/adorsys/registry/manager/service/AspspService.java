package de.adorsys.registry.manager.service;


import de.adorsys.registry.manager.service.model.AspspBO;

import java.util.List;
import java.util.UUID;

public interface AspspService {

    List<AspspBO> getByAspsp(AspspBO aspsp, int page, int size);

    List<AspspBO> getByIban(String iban, int page, int size);

    AspspBO save(AspspBO aspsp);

    void saveAll(List<AspspBO> aspsps);

    void deleteById(UUID aspspId);

    void deleteAll();
}
