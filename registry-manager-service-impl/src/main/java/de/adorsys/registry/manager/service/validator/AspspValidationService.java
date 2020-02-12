package de.adorsys.registry.manager.service.validator;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspEquivalentsReportBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import de.adorsys.registry.manager.service.model.FileValidationReportBO;
import de.adorsys.registry.manager.service.validator.duplicates.EquivalentsSearcher;
import de.adorsys.registry.manager.service.validator.duplicates.EquivalentsSearcherImpl;
import de.adorsys.registry.manager.service.validator.property.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AspspValidationService {
    private final PropertyValidator validator;
    private final EquivalentsSearcher searcher;

    public AspspValidationService() {
        this.validator = new BankNameValidator()
                                 .andThen(new UrlValidator())
                                 .andThen(new AdapterIdValidator())
                                 .andThen(new BicAndBankCodeValidator());
        this.searcher = new EquivalentsSearcherImpl();
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

        List<AspspEquivalentsReportBO> aspspEquivalentsReport = searcher.equivalentsLookup(aspsps);
        if (!aspspEquivalentsReport.isEmpty()) {
            fileValidationReport.setAspspEquivalentsReports(aspspEquivalentsReport);
        }

        if (fileValidationReport.containsErrors()) {
            fileValidationReport.notValid();
        } else {
            fileValidationReport.valid();
        }

        return fileValidationReport;
    }
}
