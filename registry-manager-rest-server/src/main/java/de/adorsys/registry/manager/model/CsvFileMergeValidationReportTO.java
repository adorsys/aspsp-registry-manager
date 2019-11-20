package de.adorsys.registry.manager.model;

import java.util.Objects;
import java.util.Set;

public class CsvFileMergeValidationReportTO {
    private int numberOfNewRecords;
    private Set<AspspTO> difference;
    private FileValidationReportTO fileValidationReport;

    public int getNumberOfNewRecords() {
        return numberOfNewRecords;
    }

    public void setNumberOfNewRecords(int numberOfNewRecords) {
        this.numberOfNewRecords = numberOfNewRecords;
    }

    public Set<AspspTO> getDifference() {
        return difference;
    }

    public void setDifference(Set<AspspTO> difference) {
        this.difference = difference;
    }

    public FileValidationReportTO getFileValidationReport() {
        return fileValidationReport;
    }

    public void setFileValidationReport(FileValidationReportTO fileValidationReport) {
        this.fileValidationReport = fileValidationReport;
    }

    @Override
    public String toString() {
        return "CsvFileMergeValidationReportTO{" +
                       "numberOfNewRecords=" + numberOfNewRecords +
                       ", difference=" + difference +
                       ", fileValidationReport=" + fileValidationReport +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFileMergeValidationReportTO that = (CsvFileMergeValidationReportTO) o;
        return numberOfNewRecords == that.numberOfNewRecords &&
                       Objects.equals(difference, that.difference) &&
                       Objects.equals(fileValidationReport, that.fileValidationReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfNewRecords, difference, fileValidationReport);
    }
}
