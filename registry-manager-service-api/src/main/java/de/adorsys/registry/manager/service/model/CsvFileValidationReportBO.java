package de.adorsys.registry.manager.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CsvFileValidationReportBO {
    private List<AspspValidationReportBO> aspspValidationErrorReports;
    private Integer totalNotValidRecords;
    private ValidationResultBO validationResult;

    public CsvFileValidationReportBO() {
        this.aspspValidationErrorReports = new ArrayList<>();
    }

    public void addAspspValidationReport(AspspValidationReportBO aspspValidationReport) {
        this.aspspValidationErrorReports.add(aspspValidationReport);
    }

    public List<AspspValidationReportBO> getAspspValidationErrorReports() {
        return aspspValidationErrorReports;
    }

    public Integer getTotalNotValidRecords() {
        return totalNotValidRecords;
    }

    public ValidationResultBO getValidationResult() {
        return validationResult;
    }

    public void valid() {
        this.validationResult = ValidationResultBO.VALID;
    }

    public void notValid() {
        this.validationResult = ValidationResultBO.NOT_VALID;
        this.totalNotValidRecords = aspspValidationErrorReports.size();
    }

    public boolean containsErrors() {
        return !aspspValidationErrorReports.isEmpty();
    }

    public boolean isNotValid() {
        return this.validationResult == ValidationResultBO.NOT_VALID;
    }

    @Override
    public String toString() {
        return "CsvFileValidationReportBO{" +
                       "aspspValidationErrorReports=" + aspspValidationErrorReports +
                       ", totalNotValidRecords=" + totalNotValidRecords +
                       ", validationResult=" + validationResult +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFileValidationReportBO that = (CsvFileValidationReportBO) o;
        return Objects.equals(aspspValidationErrorReports, that.aspspValidationErrorReports) &&
                       Objects.equals(totalNotValidRecords, that.totalNotValidRecords) &&
                       validationResult == that.validationResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspspValidationErrorReports, totalNotValidRecords, validationResult);
    }

    public enum ValidationResultBO {
        VALID,
        NOT_VALID
    }
}
