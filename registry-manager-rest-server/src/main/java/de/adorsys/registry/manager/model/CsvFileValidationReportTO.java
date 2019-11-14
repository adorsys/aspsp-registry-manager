package de.adorsys.registry.manager.model;

import java.util.List;

public class CsvFileValidationReportTO {
    private ValidationResultTO validationResult;
    private Integer totalNotValidRecords;
    private List<AspspValidationReportTO> aspspValidationErrorReports;

    public ValidationResultTO getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(ValidationResultTO validationResult) {
        this.validationResult = validationResult;
    }

    public Integer getTotalNotValidRecords() {
        return totalNotValidRecords;
    }

    public void setTotalNotValidRecords(Integer totalNotValidRecords) {
        this.totalNotValidRecords = totalNotValidRecords;
    }

    public List<AspspValidationReportTO> getAspspValidationErrorReports() {
        return aspspValidationErrorReports;
    }

    public void setAspspValidationErrorReports(List<AspspValidationReportTO> aspspValidationErrorReports) {
        this.aspspValidationErrorReports = aspspValidationErrorReports;
    }

    public enum ValidationResultTO {
        VALID,
        NOT_VALID
    }
}
