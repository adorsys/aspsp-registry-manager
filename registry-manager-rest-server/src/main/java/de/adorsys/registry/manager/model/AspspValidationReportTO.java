package de.adorsys.registry.manager.model;

import java.util.List;

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
}
