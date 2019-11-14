package de.adorsys.registry.manager.model;

import java.util.List;
import java.util.Objects;

public class AspspValidationReportTO {
    private int lineNumberInCsv;
    private AspspTO aspsp;
    private List<String> validationErrors;

    public int getLineNumberInCsv() {
        return lineNumberInCsv;
    }

    public void setLineNumberInCsv(int lineNumberInCsv) {
        this.lineNumberInCsv = lineNumberInCsv;
    }

    public AspspTO getAspsp() {
        return aspsp;
    }

    public void setAspsp(AspspTO aspsp) {
        this.aspsp = aspsp;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    @Override
    public String toString() {
        return "AspspValidationReportTO{" +
                       "lineNumberInCsv=" + lineNumberInCsv +
                       ", aspsp=" + aspsp +
                       ", validationErrors=" + validationErrors +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspspValidationReportTO that = (AspspValidationReportTO) o;
        return lineNumberInCsv == that.lineNumberInCsv &&
                       Objects.equals(aspsp, that.aspsp) &&
                       Objects.equals(validationErrors, that.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumberInCsv, aspsp, validationErrors);
    }
}
