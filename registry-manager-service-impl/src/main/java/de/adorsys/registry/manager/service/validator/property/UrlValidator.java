package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;

import java.util.regex.Pattern;

public class UrlValidator implements PropertyValidator {
    private static final String URL_REGEX = "(https|http)://[a-zA-Z\\d\\-]+\\.[^\\n\\r]+$";
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX);
    private static final String EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE = "URL is empty";
    private static final String PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "URL property value is not valid: %s";

    @Override
    public AspspValidationReportBO validate(AspspValidationReportBO validationReport, AspspBO aspsp) {
        String url = aspsp.getUrl();

        validationReport = validateNonEmptyPropertyConstraint(aspsp.getUrl(), validationReport, EMPTY_PROPERTY_VALIDATION_ERROR_MESSAGE);

        return validatePropertyValuePatternConstraint(url, URL_PATTERN, validationReport,
                String.format(PROPERTY_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, url));
    }
}
