package de.adorsys.registry.manager.service.converter;

import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AspspBOConverter {
    AspspBO toAspspBO(AspspPO po);

    AspspPO toAspspPO(AspspBO bo);

    List<AspspBO> toAspspBOList(List<AspspPO> list);

    List<AspspPO> toAspspPOList(List<AspspBO> list);
}
