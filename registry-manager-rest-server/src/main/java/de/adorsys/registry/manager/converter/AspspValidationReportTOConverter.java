package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.AspspValidationReportTO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AspspTOConverter.class)
public interface AspspValidationReportTOConverter {

    AspspValidationReportTO toAspspValidationReportTO(AspspValidationReportBO bo);
}
