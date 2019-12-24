package de.adorsys.registry.manager.repository.converter;

import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AspspEntityConverter {

    @Mapping(target = "id", source = "aspspId")
    AspspPO toAspspPO(AspspEntity entity);

    @Mapping(target = "aspspId", source = "id")
    @Mapping(target = "id", ignore = true)
    AspspEntity toAspspEntity(AspspPO po);

    List<AspspPO> toAspspPOList(List<AspspEntity> list);

    List<AspspEntity> toAspspEntityList(List<AspspPO> list);
}
