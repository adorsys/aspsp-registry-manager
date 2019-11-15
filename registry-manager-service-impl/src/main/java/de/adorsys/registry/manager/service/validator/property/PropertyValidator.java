package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;

import java.util.regex.Pattern;

public interface PropertyValidator {

    AspspValidationReportBO validate(AspspValidationReportBO validationReport, AspspBO aspsp);

    default PropertyValidator andThen(PropertyValidator validator) {
        return (AspspValidationReportBO validationReport, AspspBO aspsp) -> validator.validate(validate(validationReport, aspsp), aspsp);
    }

    default AspspValidationReportBO validateNonEmptyPropertyConstraint(String propertyValue, AspspValidationReportBO validationReport,
                                                    String errorMessage) {
        if (propertyValue == null || propertyValue.isEmpty()) {
            validationReport.addValidationError(errorMessage);
        }

        return validationReport;
    }

    default AspspValidationReportBO validatePropertyValuePatternConstraint(String propertyValue, Pattern propertyValuePattern,
                                                        AspspValidationReportBO validationReport, String errorMessage) {
        if (propertyValue != null && !propertyValue.isEmpty()) {
            if (!propertyValuePattern.matcher(propertyValue).find()) {
                validationReport.addValidationError(errorMessage);
            }
        }

        return validationReport;
    }
}
