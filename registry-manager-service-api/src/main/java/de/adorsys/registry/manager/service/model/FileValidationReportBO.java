package de.adorsys.registry.manager.service.model;

import java.util.*;

public class FileValidationReportBO {
    private List<AspspValidationReportBO> aspspValidationErrorReports;
    private Integer totalNotValidRecords;
    private List<AspspEquivalentsReportBO> aspspEquivalentsReports;
    private Integer equivalentRecords;
    private ValidationResultBO validationResult;

    public FileValidationReportBO() {
        this.aspspEquivalentsReports = new ArrayList<>();
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

    public Integer getEquivalentRecords() {
        return equivalentRecords;
    }

    public ValidationResultBO getValidationResult() {
        return validationResult;
    }

    public List<AspspEquivalentsReportBO> getAspspEquivalentsReports() {
        return aspspEquivalentsReports;
    }

    public void setAspspEquivalentsReports(List<AspspEquivalentsReportBO> aspspEquivalentsReports) {
        this.aspspEquivalentsReports = aspspEquivalentsReports;
    }

    public void valid() {
        this.validationResult = ValidationResultBO.VALID;
    }

    public void notValid() {
        this.validationResult = ValidationResultBO.NOT_VALID;
        this.totalNotValidRecords = aspspValidationErrorReports.size();
        this.equivalentRecords = aspspEquivalentsReports.size();
    }

    public boolean containsErrors() {
        return !aspspValidationErrorReports.isEmpty() || !aspspEquivalentsReports.isEmpty();
    }

    public boolean isNotValid() {
        return this.validationResult == ValidationResultBO.NOT_VALID;
    }

    @Override
    public String toString() {
        return "FileValidationReportBO{" +
            "aspspValidationErrorReports=" + aspspValidationErrorReports +
            ", totalNotValidRecords=" + totalNotValidRecords +
            ", aspspEquivalentsReport=" + aspspEquivalentsReports +
            ", totalEquivalentRecords=" + equivalentRecords +
            ", validationResult=" + validationResult +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileValidationReportBO that = (FileValidationReportBO) o;
        return Objects.equals(aspspValidationErrorReports, that.aspspValidationErrorReports) &&
            Objects.equals(totalNotValidRecords, that.totalNotValidRecords) &&
            Objects.equals(aspspEquivalentsReports, that.aspspEquivalentsReports) &&
            Objects.equals(equivalentRecords, that.equivalentRecords) &&
            validationResult == that.validationResult;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspspValidationErrorReports, totalNotValidRecords, aspspEquivalentsReports, equivalentRecords, validationResult);
    }

    public enum ValidationResultBO {
        VALID,
        NOT_VALID
    }
}
