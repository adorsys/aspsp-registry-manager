package de.adorsys.registry.manager.service.model;

import java.util.Objects;
import java.util.Set;

public class CsvFileMergeValidationReportBO {
    private int numberOfNewRecords;
    private Set<AspspBO> difference;
    private FileValidationReportBO fileValidationReport;

    public CsvFileMergeValidationReportBO(int numberOfNewRecords,
                                          Set<AspspBO> difference,
                                          FileValidationReportBO fileValidationReport) {
        this.numberOfNewRecords = numberOfNewRecords;
        this.difference = difference;
        this.fileValidationReport = fileValidationReport;
    }

    public CsvFileMergeValidationReportBO() {
    }

    public int getNumberOfNewRecords() {
        return numberOfNewRecords;
    }

    public Set<AspspBO> getDifference() {
        return difference;
    }

    public FileValidationReportBO getFileValidationReport() {
        return fileValidationReport;
    }

    public boolean isNotValid() {
        return fileValidationReport.isNotValid();
    }

    @Override
    public String toString() {
        return "CsvFileMergeValidationReportBO{" +
                       "numberOfNewRecords=" + numberOfNewRecords +
                       ", difference=" + difference +
                       ", fileValidationReport=" + fileValidationReport +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFileMergeValidationReportBO that = (CsvFileMergeValidationReportBO) o;
        return numberOfNewRecords == that.numberOfNewRecords &&
                       Objects.equals(difference, that.difference) &&
                       Objects.equals(fileValidationReport, that.fileValidationReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfNewRecords, difference, fileValidationReport);
    }
}
