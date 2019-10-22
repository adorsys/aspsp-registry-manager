package de.adorsys.registry.manager.service.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonPropertyOrder({"id", "aspspName", "bic", "url", "adapterId", "bankCode", "idpUrl", "aspspScaApproaches"})
public class AspspCsvRecord {
    private UUID id;
    private String aspspName;
    private String bic;
    private String url;
    private String bankCode;
    private String adapterId;
    private String idpUrl;
    private List<AspspScaApproachBO> aspspScaApproaches;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAspspName() {
        return aspspName;
    }

    public void setAspspName(String aspspName) {
        this.aspspName = aspspName;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAdapterId() {
        return adapterId;
    }

    public void setAdapterId(String adapterId) {
        this.adapterId = adapterId;
    }

    public String getIdpUrl() {
        return idpUrl;
    }

    public void setIdpUrl(String idpUrl) {
        this.idpUrl = idpUrl;
    }

    public List<AspspScaApproachBO> getAspspScaApproaches() {
        return aspspScaApproaches;
    }

    public void setAspspScaApproaches(List<AspspScaApproachBO> aspspScaApproaches) {
        this.aspspScaApproaches = aspspScaApproaches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspspCsvRecord that = (AspspCsvRecord) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(aspspName, that.aspspName) &&
            Objects.equals(bic, that.bic) &&
            Objects.equals(url, that.url) &&
            Objects.equals(bankCode, that.bankCode) &&
            Objects.equals(adapterId, that.adapterId) &&
            Objects.equals(idpUrl, that.idpUrl) &&
            Objects.equals(aspspScaApproaches, that.aspspScaApproaches);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, aspspName, bic, url, bankCode, adapterId, idpUrl, aspspScaApproaches);
    }

    @Override
    public String toString() {
        return "AspspCsvRecord{" +
                   "id='" + id + '\'' +
                   ", aspspName='" + aspspName + '\'' +
                   ", bic='" + bic + '\'' +
                   ", url='" + url + '\'' +
                   ", bankCode='" + bankCode + '\'' +
                   ", adapterId='" + adapterId + '\'' +
                   ", idpUrl='" + idpUrl + '\'' +
                   ", aspspScaApproaches=" + aspspScaApproaches +
                   '}';
    }
}
