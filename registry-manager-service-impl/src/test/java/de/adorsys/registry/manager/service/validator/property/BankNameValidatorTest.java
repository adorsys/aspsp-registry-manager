package de.adorsys.registry.manager.service.validator.property;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BankNameValidatorTest {
    private static final int LINE_NUMBER_IN_CSV = 1;
    private static final String EMPTY_BANK_CODE_VALIDATION_ERROR_MESSAGE = "Bank name is empty";

    private BankNameValidator validator = new BankNameValidator();
    private AspspBO aspspValid = buildValidAspsp();
    private AspspBO aspspNotValid = new AspspBO();

    @Test
    public void validate_Success() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspValid, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspValid);

        assertThat(actual.isNotValid(), is(false));
    }

    @Test
    public void validate_Failure_BankCodeIsEmpty() {
        AspspValidationReportBO validationReport = new AspspValidationReportBO(aspspNotValid, LINE_NUMBER_IN_CSV);

        AspspValidationReportBO actual = validator.validate(validationReport, aspspNotValid);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.getLineNumberInCsv(), is(LINE_NUMBER_IN_CSV));
        assertThat(actual.getValidationErrors().size(), is(1));
        assertThat(actual.getValidationErrors().get(0), is(EMPTY_BANK_CODE_VALIDATION_ERROR_MESSAGE));
    }

    private AspspBO buildValidAspsp() {
        AspspBO aspsp = new AspspBO();
        aspsp.setName("Test Bank");
        return aspsp;
    }
}