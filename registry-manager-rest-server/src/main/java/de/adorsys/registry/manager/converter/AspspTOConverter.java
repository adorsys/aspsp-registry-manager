package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.AspspTO;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AspspTOConverter {

    AspspTO toAspspTO(AspspBO bo);

    List<AspspTO> toAspspTOList(List<AspspBO> list);

    AspspBO toAspspBO(AspspTO to);
}
