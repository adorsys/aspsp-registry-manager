package de.adorsys.registry.manager.service;


import de.adorsys.registry.manager.service.model.AspspBO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AspspService {

    Page<AspspBO> getByAspsp(AspspBO aspsp, Pageable pageable);

    Page<AspspBO> getByIban(String iban, Pageable pageable);

    AspspBO save(AspspBO aspsp);

    void saveAll(List<AspspBO> aspsps);

    void deleteById(UUID aspspId);

    void deleteAll();
}
