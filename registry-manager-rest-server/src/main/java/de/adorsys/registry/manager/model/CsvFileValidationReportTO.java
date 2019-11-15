package de.adorsys.registry.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

public class CsvFileValidationReportTO {
    private ValidationResultTO validationResult;
    private Integer totalNotValidRecords;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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

    @Override
    public String toString() {
        return "CsvFileValidationReportTO{" +
                       "validationResult=" + validationResult +
                       ", totalNotValidRecords=" + totalNotValidRecords +
                       ", aspspValidationErrorReports=" + aspspValidationErrorReports +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFileValidationReportTO that = (CsvFileValidationReportTO) o;
        return validationResult == that.validationResult &&
                       Objects.equals(totalNotValidRecords, that.totalNotValidRecords) &&
                       Objects.equals(aspspValidationErrorReports, that.aspspValidationErrorReports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationResult, totalNotValidRecords, aspspValidationErrorReports);
    }

    public enum ValidationResultTO {
        VALID,
        NOT_VALID
    }
}
