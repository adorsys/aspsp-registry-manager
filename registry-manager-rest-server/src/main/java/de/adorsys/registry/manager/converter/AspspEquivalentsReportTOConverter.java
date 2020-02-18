package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.AspspEquivalentsReportTO;
import de.adorsys.registry.manager.service.model.AspspEquivalentsReportBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AspspEquivalentsReportTOConverter {

    AspspEquivalentsReportTO toAspspEquivalentsReportTO(AspspEquivalentsReportBO bo);
}
