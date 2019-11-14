package de.adorsys.registry.manager.service.model;

import java.util.ArrayList;
import java.util.List;

public class AspspValidationReportBO {
    private AspspBO aspsp;
    private List<String> validationErrors;
    private int lineNumberInCsv;

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
}
