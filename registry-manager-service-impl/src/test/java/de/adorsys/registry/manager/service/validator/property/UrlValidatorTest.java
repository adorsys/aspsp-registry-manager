package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class UrlValidatorTest {
    private static final int LINE_NUMBER_IN_CSV = 1;
    private static final String URL_VALID = "https://example.com";
    private static final String URL_NOT_VALID = "example";
    private static final String EMPTY_URL_VALIDATION_ERROR_MESSAGE = "URL is empty";
    private static final String URL_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "URL property value is not valid: %s";

    private UrlValidator validator = new UrlValidator();

    private AspspBO aspspValid = buildValidAspsp();
    private AspspBO aspspWithEmptyUrl = new AspspBO();
    private AspspBO aspspWithNotValidUrl = buildAspspWithNotValidUrl();

    @Test
    public void validate_Success() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValid, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValid);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.getValidationErrors().isEmpty(), is(true));
    }

    @Test
    public void validate_Failure_UrlIsEmpty() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithEmptyUrl, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithEmptyUrl);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(EMPTY_URL_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void validate_Failure_UrlIsNotValid() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithNotValidUrl, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithNotValidUrl);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(String.format(URL_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, URL_NOT_VALID)));
    }

    private AspspBO buildValidAspsp() {
        AspspBO aspsp = new AspspBO();
        aspsp.setUrl(URL_VALID);
        return aspsp;
    }

    private AspspBO buildAspspWithNotValidUrl() {
        AspspBO aspsp = new AspspBO();
        aspsp.setUrl(URL_NOT_VALID);
        return aspsp;
    }
}