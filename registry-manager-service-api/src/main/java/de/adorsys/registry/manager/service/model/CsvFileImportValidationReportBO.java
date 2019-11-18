package de.adorsys.registry.manager.service.model;

import java.util.Objects;

public class CsvFileImportValidationReportBO {
    private long csvFileRecordsNumber;
    private long dbRecordsNumber;
    private FileValidationReportBO fileValidationReport;

    public CsvFileImportValidationReportBO(long csvFileRecordsNumber,
                                           long dbRecordsNumber,
                                           FileValidationReportBO fileValidationReport) {
        this.csvFileRecordsNumber = csvFileRecordsNumber;
        this.dbRecordsNumber = dbRecordsNumber;
        this.fileValidationReport = fileValidationReport;
    }

    public CsvFileImportValidationReportBO() {
    }

    public long getCsvFileRecordsNumber() {
        return csvFileRecordsNumber;
    }

    public long getDbRecordsNumber() {
        return dbRecordsNumber;
    }

    public FileValidationReportBO getFileValidationReport() {
        return fileValidationReport;
    }

    public boolean isNotValid() {
        return fileValidationReport.isNotValid();
    }

    @Override
    public String toString() {
        return "CsvFileImportValidationReportBO{" +
                       "csvFileRecordsNumber=" + csvFileRecordsNumber +
                       ", dbRecordsNumber=" + dbRecordsNumber +
                       ", fileValidationReport=" + fileValidationReport +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CsvFileImportValidationReportBO that = (CsvFileImportValidationReportBO) o;
        return csvFileRecordsNumber == that.csvFileRecordsNumber &&
                       dbRecordsNumber == that.dbRecordsNumber &&
                       Objects.equals(fileValidationReport, that.fileValidationReport);
    }

    @Override
    public int hashCode() {
        return Objects.hash(csvFileRecordsNumber, dbRecordsNumber, fileValidationReport);
    }
}
