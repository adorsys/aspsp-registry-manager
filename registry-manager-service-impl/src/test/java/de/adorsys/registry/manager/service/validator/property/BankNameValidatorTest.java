package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BankNameValidatorTest {
    private static final int LINE_NUMBER_IN_CSV = 1;
    private static final String BANK_NAME_VALID = "Test-Bank Nüremberg";
    private static final String BANK_NAME_NOT_VALID = "Test Bank *";
    private static final String EMPTY_BANK_NAME_VALIDATION_ERROR_MESSAGE = "Bank name is empty";
    private static final String BANK_NAME_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "Bank name property value is not valid: %s";

    private BankNameValidator validator = new BankNameValidator();
    private AspspBO aspspValid = buildValidAspsp();
    private AspspBO aspspWithEmptyBankName = new AspspBO();
    private AspspBO aspspWithNotValidBankName = buildAspspWithNotValidBankName();

    @Test
    public void validate_Success() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValid, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValid);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.getValidationErrors().isEmpty(), is(true));
    }

    @Test
    public void validate_Failure_BankCodeIsEmpty() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithEmptyBankName, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithEmptyBankName);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(EMPTY_BANK_NAME_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void validate_Failure_AdapterIdIsNotValid() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithNotValidBankName, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithNotValidBankName);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(String.format(BANK_NAME_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BANK_NAME_NOT_VALID)));
    }

    private AspspBO buildValidAspsp() {
        AspspBO aspsp = new AspspBO();
        aspsp.setName(BANK_NAME_VALID);
        return aspsp;
    }

    private AspspBO buildAspspWithNotValidBankName() {
        AspspBO aspsp = new AspspBO();
        aspsp.setName(BANK_NAME_NOT_VALID);
        return aspsp;
    }
}