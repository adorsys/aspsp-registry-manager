package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AdapterIdValidatorTest {
    private static final int LINE_NUMBER_IN_CSV = 1;
    private static final String ADAPTER_ID_VALID = "test-bank-adapter";
    private static final String ADAPTER_ID_NOT_VALID = "test-bankadapter";
    private static final String EMPTY_ADAPTER_ID_VALIDATION_ERROR_MESSAGE = "Adapter ID is empty";
    private static final String ADAPTER_ID_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "Adapter ID property value is not valid: %s";

    private AdapterIdValidator validator = new AdapterIdValidator();
    private AspspBO aspspValid = buildValidAspsp();
    private AspspBO aspspWithEmptyAdapterId = new AspspBO();
    private AspspBO aspspWithNotValidAdapterId = buildAspspWithNotValidAdapterId();

    @Test
    public void validate_Success() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValid, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValid);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.getValidationErrors().isEmpty(), is(true));
    }

    @Test
    public void validate_Failure_AdapterIdIsEmpty() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithEmptyAdapterId, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithEmptyAdapterId);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(EMPTY_ADAPTER_ID_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void validate_Failure_AdapterIdIsNotValid() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithNotValidAdapterId, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithNotValidAdapterId);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(String.format(ADAPTER_ID_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, ADAPTER_ID_NOT_VALID)));
    }

    private AspspBO buildValidAspsp() {
        AspspBO aspsp = new AspspBO();
        aspsp.setAdapterId(ADAPTER_ID_VALID);
        return aspsp;
    }

    private AspspBO buildAspspWithNotValidAdapterId() {
        AspspBO aspsp = new AspspBO();
        aspsp.setAdapterId(ADAPTER_ID_NOT_VALID);
        return aspsp;
    }
}