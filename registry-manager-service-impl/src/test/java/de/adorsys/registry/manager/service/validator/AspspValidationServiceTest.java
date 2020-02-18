package de.adorsys.registry.manager.service.validator;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.FileValidationReportBO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class AspspValidationServiceTest {
    private static final String BANK_NAME_VALID = "test-bank-adapter";
    private static final String ADAPTER_ID_VALID = "test-bank-adapter";
    private static final String BIC_VALID = "BANKABICXXX";
    private static final String BIC_VALID_2 = "BANKABICXXX";
    private static final String BANK_CODE_VALID = "11111111";
    private static final String BANK_CODE_VALID_2 = "22222222";
    private static final String URL_VALID = "https://example.com";

    private static final String ADAPTER_ID_NOT_VALID = "test-bankadapter";
    private static final String BIC_NOT_VALID = "BANKABICXX";
    private static final String BIC_NOT_VALID_2 = "BANKABICYY";
    private static final String BANK_CODE_NOT_VALID = "111111";
    private static final String BANK_CODE_NOT_VALID_2 = "222222";
    private static final String URL_NOT_VALID = "example";

    private AspspValidationService service = new AspspValidationService();

    private List<AspspBO> validAspsps = buildValidAspsps();
    private List<AspspBO> validAspspsWithDuplicates = buildValidAspspsWithDuplicates();
    private List<AspspBO> notValidAspsps = buildNotValidAspsps();
    private List<AspspBO> notValidAspspsWithoutDuplicates = buildNotValidAspspsWithoutDuplicates();

    @Test
    public void validate_Success() {
        FileValidationReportBO actual = service.validate(validAspsps);

        assertThat(actual.isNotValid(), is(false));
        assertThat(actual.containsErrors(), is(false));
        assertNull(actual.getTotalNotValidRecords());
        assertThat(actual.getAspspValidationErrorReports().isEmpty(), is(true));
        assertThat(actual.getAspspEquivalentsReports().isEmpty(), is(true));
    }

    @Test
    public void validate_Failure() {
        FileValidationReportBO actual = service.validate(notValidAspsps);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.containsErrors(), is(true));
        assertNotNull(actual.getTotalNotValidRecords());
        assertThat(actual.getTotalNotValidRecords(), is(notValidAspsps.size()));
        assertThat(actual.getAspspValidationErrorReports().size(), is(notValidAspsps.size()));
        assertThat(actual.getAspspEquivalentsReports().size(), is(1));
        assertThat(actual.getAspspValidationErrorReports().get(0).getAspsp(), is(notValidAspsps.get(0)));
        assertThat(actual.getAspspValidationErrorReports().get(1).getAspsp(), is(notValidAspsps.get(1)));
        assertThat(actual.getAspspEquivalentsReports().get(0).getAspsp(), is(notValidAspsps.get(0)));
    }

    @Test
    public void validate_Failure_passedValidationButHasDuplicates() {
        FileValidationReportBO actual = service.validate(validAspspsWithDuplicates);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.containsErrors(), is(true));
        assertThat(actual.getTotalNotValidRecords(), is(0));
        assertThat(actual.getEquivalentRecords(), is(1));
        assertThat(actual.getAspspValidationErrorReports().isEmpty(), is(true));
        assertThat(actual.getAspspEquivalentsReports().size(), is(1));
        assertThat(actual.getAspspEquivalentsReports().get(0).getAspsp(), is(validAspspsWithDuplicates.get(0)));
    }

    @Test
    public void validate_Failure_failedValidationButNoDuplicates() {
        FileValidationReportBO actual = service.validate(notValidAspspsWithoutDuplicates);

        assertThat(actual.isNotValid(), is(true));
        assertThat(actual.containsErrors(), is(true));
        assertThat(actual.getTotalNotValidRecords(), is(2));
        assertThat(actual.getEquivalentRecords(), is(0));
        assertThat(actual.getAspspEquivalentsReports().isEmpty(), is(true));
        assertThat(actual.getAspspValidationErrorReports().size(), is(notValidAspspsWithoutDuplicates.size()));
        assertThat(actual.getAspspValidationErrorReports().get(0).getAspsp(), is(notValidAspspsWithoutDuplicates.get(0)));
        assertThat(actual.getAspspValidationErrorReports().get(1).getAspsp(), is(notValidAspspsWithoutDuplicates.get(1)));
    }

    private List<AspspBO> buildValidAspsps() {
        return Arrays.asList(buildValidAspsp(), buildSecondValidAspsp());
    }

    private List<AspspBO> buildValidAspspsWithDuplicates() {
        return Arrays.asList(buildValidAspsp(), buildValidAspsp());
    }

    private List<AspspBO> buildNotValidAspsps() {
        return Arrays.asList(buildNotValidAspsp(), buildNotValidAspsp());
    }

    private List<AspspBO> buildNotValidAspspsWithoutDuplicates() {
        return Arrays.asList(buildNotValidAspsp(), buildSecondNotValidAspsp());
    }

    private AspspBO buildValidAspsp() {
        AspspBO aspsp = new AspspBO();

        aspsp.setId(UUID.randomUUID());
        aspsp.setName(BANK_NAME_VALID);
        aspsp.setAdapterId(ADAPTER_ID_VALID);
        aspsp.setBic(BIC_VALID);
        aspsp.setBankCode(BANK_CODE_VALID);
        aspsp.setUrl(URL_VALID);

        return aspsp;
    }

    private AspspBO buildSecondValidAspsp() {
        AspspBO aspsp = buildValidAspsp();

        aspsp.setBankCode(BANK_CODE_VALID_2);
        aspsp.setBic(BIC_VALID_2);

        return aspsp;
    }

    private AspspBO buildNotValidAspsp() {
        AspspBO aspsp = new AspspBO();

        aspsp.setId(UUID.randomUUID());
        aspsp.setAdapterId(ADAPTER_ID_NOT_VALID);
        aspsp.setBic(BIC_NOT_VALID);
        aspsp.setBankCode(BANK_CODE_NOT_VALID);
        aspsp.setUrl(URL_NOT_VALID);

        return aspsp;
    }

    private AspspBO buildSecondNotValidAspsp() {
        AspspBO aspsp = buildNotValidAspsp();

        aspsp.setBic(BIC_NOT_VALID_2);
        aspsp.setBankCode(BANK_CODE_NOT_VALID_2);

        return aspsp;
    }
}
