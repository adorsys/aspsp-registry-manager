package de.adorsys.registry.manager.model;

import java.util.Objects;

public class CsvFileImportValidationReportTO {
    private long csvFileRecordsNumber;
    private long dbRecordsNumber;
    private FileValidationReportTO fileValidationReport;

    public long getCsvFileRecordsNumber() {
        return csvFileRecordsNumber;
    }

    public void setCsvFileRecordsNumber(long csvFileRecordsNumber) {
        this.csvFileRecordsNumber = csvFileRecordsNumber;
    }

    public long getDbRecordsNumber() {
        return dbRecordsNumber;
    }

    public void setDbRecordsNumber(long dbRecordsNumber) {
        this.dbRecordsNumber = dbRecordsNumber;
    }

    public FileValidationReportTO getFileValidationReport() {
        return fileValidationReport;
    }

    public void setFileValidationReport(FileValidationReportTO fileValidationReport) {
        this.fileValidationReport = fileValidationReport;
    }

    @Override
    public String toString() {
        return "CsvFileImportValidationReportTO{" +
                       "csvFileRecordsNumber=" + csvFileRecordsNumber +
                       ", dbRecordsNumber=" + dbRecordsNumber +
                       ", fileValidationReport=" + fileValidationReport +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFileImportValidationReportTO that = (CsvFileImportValidationReportTO) o;
        return csvFileRecordsNumber == that.csvFileRecordsNumber &&
                       dbRecordsNumber == that.dbRecordsNumber &&
                       Objects.equals(fileValidationReport, that.fileValidationReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csvFileRecordsNumber, dbRecordsNumber, fileValidationReport);
    }
}
