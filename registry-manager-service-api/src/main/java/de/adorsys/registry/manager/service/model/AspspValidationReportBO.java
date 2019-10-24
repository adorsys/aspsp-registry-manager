package de.adorsys.registry.manager.service.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AspspValidationReportBO {
    private AspspBO aspsp;
    private List<String> validationErrors;
    private int lineNumberInCsv;

    public AspspValidationReportBO() {
    }

    public AspspValidationReportBO(AspspBO aspsp, int lineNumberInCsv) {
        this.aspsp = aspsp;
        this.lineNumberInCsv = lineNumberInCsv;
        this.validationErrors = new ArrayList<>();
    }

    public AspspBO getAspsp() {
        return aspsp;
    }

    public void setAspsp(AspspBO aspsp) {
        this.aspsp = aspsp;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public int getLineNumberInCsv() {
        return lineNumberInCsv;
    }

    public void addValidationError(String errorMessage) {
        this.validationErrors.add(errorMessage);
    }

    public boolean isNotValid() {
        return !validationErrors.isEmpty();
    }

    @Override
    public String toString() {
        return "AspspValidationReportBO{" +
                       "aspsp=" + aspsp +
                       ", validationErrors=" + validationErrors +
                       ", lineNumberInCsv=" + lineNumberInCsv +
                       '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspspValidationReportBO that = (AspspValidationReportBO) o;
        return lineNumberInCsv == that.lineNumberInCsv &&
                       Objects.equals(aspsp, that.aspsp) &&
                       Objects.equals(validationErrors, that.validationErrors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(aspsp, validationErrors, lineNumberInCsv);
    }
}
