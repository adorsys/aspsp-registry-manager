package de.adorsys.registry.manager.service.converter;

import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.model.AspspCsvRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AspspCsvRecordConverter {

    @Mapping(source = "aspspName", target = "name")
    @Mapping(source = "aspspScaApproaches", target = "scaApproaches")
    @Mapping(target = "paginationId", ignore = true)
    @Mapping(target = "id", source = "id", defaultExpression = "java(java.util.UUID.randomUUID())")
    AspspPO toAspspPO(AspspCsvRecord aspspCsvRecord);

    List<AspspPO> toAspspPOList(List<AspspCsvRecord> aspspCsvRecordList);

    @Mapping(source = "scaApproaches", target = "aspspScaApproaches")
    @Mapping(source = "name", target = "aspspName")
    AspspCsvRecord toAspspCsvRecord(AspspPO aspsp);

    List<AspspCsvRecord> toAspspCsvRecordList(List<AspspPO> aspspPOList);
}
