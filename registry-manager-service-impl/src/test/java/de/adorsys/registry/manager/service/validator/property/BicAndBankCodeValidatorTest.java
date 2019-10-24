package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BicAndBankCodeValidatorTest {
    private static final int LINE_NUMBER_IN_CSV = 1;
    private static final String BIC_VALID = "BANKABICXXX";
    private static final String BIC_NOT_VALID = "BANKABICXX";
    private static final String BANK_CODE_VALID = "11111111";
    private static final String BANK_CODE_NOT_VALID = "111111";
    private static final String EMPTY_BIC_ID_VALIDATION_ERROR_MESSAGE = "BIC is empty";
    private static final String EMPTY_BANK_CODE_ID_VALIDATION_ERROR_MESSAGE = "Bank code is empty";
    private static final String BIC_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "BIC property value is not valid: %s";
    private static final String BANK_CODE_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE = "Bank code property value is not valid: %s";

    private BicAndBankCodeValidator validator = new BicAndBankCodeValidator();

    private AspspBO aspspValidWithBic = buildAspspWithBic(BIC_VALID);
    private AspspBO aspspValidWithBankCode = buildAspspWithBankCode(BANK_CODE_VALID);
    private AspspBO aspspValidWithBicAndBankCode = buildAspspWithBicAndBankCode(BIC_VALID, BANK_CODE_VALID);
    private AspspBO aspspWithEmptyBicAndBankCode = new AspspBO();
    private AspspBO aspspWithNotValidBic = buildAspspWithBic(BIC_NOT_VALID);
    private AspspBO aspspWithNotValidBankCode = buildAspspWithBankCode(BANK_CODE_NOT_VALID);
    private AspspBO aspspWithNotValidBicAndBankCode = buildAspspWithBicAndBankCode(BIC_NOT_VALID, BANK_CODE_NOT_VALID);

    @Test
    public void validate_Success_ValidBicIsPresent() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValidWithBic, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValidWithBic);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.getValidationErrors().isEmpty(), is(true));
    }

    @Test
    public void validate_Success_ValidBankCodeIsPresent() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValidWithBankCode, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValidWithBankCode);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.getValidationErrors().isEmpty(), is(true));
    }

    @Test
    public void validate_Success_ValidBicAndBankCodeArePresent() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValidWithBicAndBankCode, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValidWithBicAndBankCode);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.getValidationErrors().isEmpty(), is(true));
    }

    @Test
    public void validate_Failure_BicAndBankCodeAreEmpty() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithEmptyBicAndBankCode, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithEmptyBicAndBankCode);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(2));
        assertThat(actual.getValidationErrors().get(0), is(EMPTY_BIC_ID_VALIDATION_ERROR_MESSAGE));
        assertThat(actual.getValidationErrors().get(1), is(EMPTY_BANK_CODE_ID_VALIDATION_ERROR_MESSAGE));
    }

    @Test
    public void validate_Failure_BicIsNotValid() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithNotValidBic, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithNotValidBic);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(String.format(BIC_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BIC_NOT_VALID)));
    }

    @Test
    public void validate_Failure_BankCodeIsNotValid() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithNotValidBankCode, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithNotValidBankCode);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(String.format(BANK_CODE_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BANK_CODE_NOT_VALID)));
    }

    @Test
    public void validate_Failure_BicAndBankCodeAreNotValid() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspWithNotValidBicAndBankCode, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspWithNotValidBicAndBankCode);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(2));
        assertThat(actual.getValidationErrors().get(0), is(String.format(BIC_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BIC_NOT_VALID)));
        assertThat(actual.getValidationErrors().get(1), is(String.format(BANK_CODE_WITH_NOT_VALID_VALUE_VALIDATION_ERROR_MESSAGE, BANK_CODE_NOT_VALID)));
    }

    private AspspBO buildAspspWithBic(String bic) {
        AspspBO aspsp = new AspspBO();
        aspsp.setBic(bic);
        return aspsp;
    }

    private AspspBO buildAspspWithBankCode(String bankCode) {
        AspspBO aspsp = new AspspBO();
        aspsp.setBankCode(bankCode);
        return aspsp;
    }

    private AspspBO buildAspspWithBicAndBankCode(String bic, String bankCode) {
        AspspBO aspsp = new AspspBO();
        aspsp.setBic(bic);
        aspsp.setBankCode(bankCode);
        return aspsp;
    }
}