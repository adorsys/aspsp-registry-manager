package de.adorsys.registry.manager.service;


import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.PageBO;

import java.util.List;
import java.util.UUID;

public interface AspspService {

    PageBO getByAspsp(AspspBO aspsp, int page, int size);

    PageBO getByIban(String iban, int page, int size);

    AspspBO save(AspspBO aspsp);

    void saveAll(List<AspspBO> aspsps);

    void deleteById(UUID aspspId);

    void deleteAll();
}
