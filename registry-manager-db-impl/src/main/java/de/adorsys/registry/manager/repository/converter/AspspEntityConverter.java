package de.adorsys.registry.manager.repository.converter;

import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AspspEntityConverter {

    AspspPO toAspspPO(AspspEntity entity);

    AspspEntity toAspspEntity(AspspPO po);

    List<AspspPO> toAspspPOList(List<AspspEntity> list);

    List<AspspEntity> toAspspEntityList(List<AspspPO> list);
}
