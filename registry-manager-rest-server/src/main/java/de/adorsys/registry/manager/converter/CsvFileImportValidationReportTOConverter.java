package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.CsvFileImportValidationReportTO;
import de.adorsys.registry.manager.service.model.CsvFileImportValidationReportBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CsvFileImportValidationReportTOConverter {

    CsvFileImportValidationReportTO toCsvFileImportValidationReportTO(CsvFileImportValidationReportBO bo);
}
