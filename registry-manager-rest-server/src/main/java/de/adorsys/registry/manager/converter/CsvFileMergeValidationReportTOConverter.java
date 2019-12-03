package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.CsvFileMergeValidationReportTO;
import de.adorsys.registry.manager.service.model.CsvFileMergeValidationReportBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CsvFileMergeValidationReportTOConverter {

    CsvFileMergeValidationReportTO toCsvFileMergeValidationReportTO(CsvFileMergeValidationReportBO bo);
}
