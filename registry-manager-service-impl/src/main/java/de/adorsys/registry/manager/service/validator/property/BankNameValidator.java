package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;

import java.util.regex.Pattern;

public class BankNameValidator implements PropertyValidator {
    private static final String BANK_NAME_REGEX = "^[\\w\\-\\säöüÄÖÜß]+$";
    private static final Pattern BANK_NAME_PATTERN = Pattern.compile(BANK_NAME_REGEX);
    private static final String EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE = "Bank name is empty";
    private static final String PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "Bank name property value is not valid: %s";

    @Override
    public AspspValidationReportBO validate(AspspValidationReportBO validationReport, AspspBO aspsp) {
        String bankName = aspsp.getName();

        validationReport = validateNonEmptyPropertyConstraint(aspsp.getName(), validationReport, EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE);

        return validatePropertyValuePatternConstraint(bankName, BANK_NAME_PATTERN, validationReport,
                String.format(PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, bankName));
    }
}
