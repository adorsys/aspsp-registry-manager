package de.adorsys.registry.manager.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

public class FileValidationReportTO {
    private ValidationResultTO validationResult;
    private Integer totalNotValidRecords;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AspspValidationReportTO> aspspValidationErrorReports;
    private Integer equivalentRecords;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<AspspEquivalentsReportTO> aspspEquivalentsReports;

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

    public Integer getEquivalentRecords() {
        return equivalentRecords;
    }

    public void setEquivalentRecords(Integer equivalentRecords) {
        this.equivalentRecords = equivalentRecords;
    }

    public List<AspspEquivalentsReportTO> getAspspEquivalentsReports() {
        return aspspEquivalentsReports;
    }

    public void setAspspEquivalentsReports(List<AspspEquivalentsReportTO> aspspEquivalentsReports) {
        this.aspspEquivalentsReports = aspspEquivalentsReports;
    }

    @Override
    public String toString() {
        return "FileValidationReportTO{" +
            "validationResult=" + validationResult +
            ", totalNotValidRecords=" + totalNotValidRecords +
            ", aspspValidationErrorReports=" + aspspValidationErrorReports +
            ", totalEquivalentRecords=" + equivalentRecords +
            ", aspspEquivalentsReport=" + aspspEquivalentsReports +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileValidationReportTO that = (FileValidationReportTO) o;
        return validationResult == that.validationResult &&
            Objects.equals(totalNotValidRecords, that.totalNotValidRecords) &&
            Objects.equals(aspspValidationErrorReports, that.aspspValidationErrorReports) &&
            Objects.equals(equivalentRecords, that.equivalentRecords) &&
            Objects.equals(aspspEquivalentsReports, that.aspspEquivalentsReports);
    }

    @Override
    public int hashCode() {
        return Objects.hash(validationResult, totalNotValidRecords, aspspValidationErrorReports, equivalentRecords, aspspEquivalentsReports);
    }

    public enum ValidationResultTO {
        VALID,
        NOT_VALID
    }
}
