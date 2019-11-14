package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;

import java.util.regex.Pattern;

public class BicAndBankCodeValidator implements PropertyValidator {
    private static final String BIC_PROPERTY = "BIC";
    private static final String BANK_CODE_PROPERTY = "Bank code";
    private static final String BIC_REGEX = "^[A-Z]{6}([A-Z0-9]{2})?([A-Z0-9]{3})?$";
    private static final String BANK_CODE_REGEX = "^[0-9]{8}$";
    private static final Pattern BIC_PATTERN = Pattern.compile(BIC_REGEX);
    private static final Pattern BANK_CODE_PATTERN = Pattern.compile(BANK_CODE_REGEX);
    private static final String EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE = "%s is empty";
    private static final String PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "%s property value is not valid: %s";

    @Override
    public AspspValidationReportBO validate(AspspValidationReportBO validationReport, AspspBO aspsp) {
        String bic = aspsp.getBic();
        String bankCode = aspsp.getBankCode();

        if ((bic == null || bic.isEmpty())
                    && (bankCode == null || bankCode.isEmpty())) {
            validationReport.addValidationError(String.format(EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE, BIC_PROPERTY));
            validationReport.addValidationError(String.format(EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE, BANK_CODE_PROPERTY));
        }

        validationReport = validatePropertyValuePatternConstraint(bic, BIC_PATTERN, validationReport,
                String.format(PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BIC_PROPERTY, bic));

        return validatePropertyValuePatternConstraint(bankCode, BANK_CODE_PATTERN, validationReport,
                String.format(PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BANK_CODE_PROPERTY, bankCode));
    }
}
