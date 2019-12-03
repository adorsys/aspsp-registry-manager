package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.FileValidationReportTO;
import de.adorsys.registry.manager.service.model.FileValidationReportBO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileValidationReportTOConverter {

    FileValidationReportTO toFileValidationReportTO(FileValidationReportBO bo);
}
