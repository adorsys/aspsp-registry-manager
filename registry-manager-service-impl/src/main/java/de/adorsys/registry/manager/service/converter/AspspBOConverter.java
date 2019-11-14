package de.adorsys.registry.manager.service.converter;

import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspCsvRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AspspBOConverter {
    AspspBO toAspspBO(AspspPO po);

    AspspPO toAspspPO(AspspBO bo);

    @Mapping(source = "aspspName", target = "name")
    @Mapping(source = "aspspScaApproaches", target = "scaApproaches")
    @Mapping(target = "paginationId", ignore = true)
    AspspBO toAspspBO(AspspCsvRecord csvRecord);

    List<AspspBO> toAspspBOList(List<AspspPO> list);

    List<AspspPO> toAspspPOList(List<AspspBO> list);

    List<AspspBO> csvRecordListToAspspBOList(List<AspspCsvRecord> list);
}
