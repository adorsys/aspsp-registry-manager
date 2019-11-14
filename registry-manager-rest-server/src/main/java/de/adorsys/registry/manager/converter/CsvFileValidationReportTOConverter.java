package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.CsvFileValidationReportTO;
import de.adorsys.registry.manager.service.model.CsvFileValidationReportBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AspspValidationReportTOConverter.class)
public interface CsvFileValidationReportTOConverter {

    CsvFileValidationReportTO toCsvFileValidationReportTO(CsvFileValidationReportBO bo);
}
