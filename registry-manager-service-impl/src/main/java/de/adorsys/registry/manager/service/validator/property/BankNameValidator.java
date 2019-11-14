package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;

public class BankNameValidator implements PropertyValidator {
    private static final String EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE = "Bank name is empty";

    @Override
    public AspspValidationReportBO validate(AspspValidationReportBO validationReport, AspspBO aspsp) {
        return validateNonEmptyPropertyConstraint(aspsp.getName(), validationReport, EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE);
    }
}
