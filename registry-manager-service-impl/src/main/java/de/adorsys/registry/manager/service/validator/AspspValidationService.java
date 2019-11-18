package de.adorsys.registry.manager.service.validator;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import de.adorsys.registry.manager.service.model.FileValidationReportBO;
import de.adorsys.registry.manager.service.validator.property.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AspspValidationService {
    private final PropertyValidator validator;

    public AspspValidationService() {
        this.validator = new BankNameValidator()
                                 .andThen(new UrlValidator())
                                 .andThen(new AdapterIdValidator())
                                 .andThen(new BicAndBankCodeValidator());
    }

    public FileValidationReportBO validate(List<AspspBO> aspsps) {
        FileValidationReportBO fileValidationReport = new FileValidationReportBO();

        for (int i = 0; i < aspsps.size(); i++) {
            AspspBO aspsp = aspsps.get(i);
            AspspValidationReportBO aspspValidationReport = validator.validate(new AspspValidationReportBO(aspsp, i + 1), aspsp);

            if (aspspValidationReport.isNotValid()) {
                fileValidationReport.addAspspValidationReport(aspspValidationReport);
            }
        }

        if (fileValidationReport.containsErrors()) {
            fileValidationReport.notValid();
        } else {
            fileValidationReport.valid();
        }

        return fileValidationReport;
    }
}
