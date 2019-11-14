package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;

import java.util.regex.Pattern;

public class AdapterIdValidator implements PropertyValidator {
    private static final String ADAPTER_ID_REGEX = ".{2,}-adapter";
    private static final Pattern ADAPTER_ID_PATTERN = Pattern.compile(ADAPTER_ID_REGEX);
    private static final String EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE = "Adapter ID is empty";
    private static final String PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "Adapter ID property value is not valid: %s";

    @Override
    public AspspValidationReportBO validate(AspspValidationReportBO validationReport, AspspBO aspsp) {
        String adapterId = aspsp.getAdapterId();

        validationReport = validateNonEmptyPropertyConstraint(adapterId, validationReport, EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE);

        return validatePropertyValuePatternConstraint(adapterId, ADAPTER_ID_PATTERN, validationReport,
                String.format(PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, adapterId));
    }
}
