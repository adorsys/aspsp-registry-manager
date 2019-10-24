package de.adorsys.registry.manager.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonPropertyOrder({"id", "name", "bic", "url", "adapterId", "bankCode", "idpUrl", "aspspScaApproaches"})
public class AspspBO {
    private UUID id;
    private String name;
    private String bic;
    private String bankCode;
    private String url;
    private String adapterId;
    private String idpUrl;
    private List<AspspScaApproachBO> scaApproaches;
    private String paginationId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public List<AspspScaApproachBO> getScaApproaches() {
        return scaApproaches;
    }

    public void setScaApproaches(List<AspspScaApproachBO> scaApproaches) {
        this.scaApproaches = scaApproaches;
    }

    @JsonIgnore
    public String getPaginationId() {
        return paginationId;
    }

    public void setPaginationId(String paginationId) {
        this.paginationId = paginationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspspBO aspspBO = (AspspBO) o;
        return Objects.equals(id, aspspBO.id) &&
                       Objects.equals(name, aspspBO.name) &&
                       Objects.equals(bic, aspspBO.bic) &&
                       Objects.equals(bankCode, aspspBO.bankCode) &&
                       Objects.equals(url, aspspBO.url) &&
                       Objects.equals(adapterId, aspspBO.adapterId) &&
                       Objects.equals(idpUrl, aspspBO.idpUrl) &&
                       Objects.equals(scaApproaches, aspspBO.scaApproaches) &&
                       Objects.equals(paginationId, aspspBO.paginationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bic, bankCode, url, adapterId, idpUrl, scaApproaches, paginationId);
    }

    @Override
    public String toString() {
        return "AspspBO{" +
                       "id='" + id + '\'' +
                       ", name='" + name + '\'' +
                       ", bic='" + bic + '\'' +
                       ", bankCode='" + bankCode + '\'' +
                       ", url='" + url + '\'' +
                       ", adapterId='" + adapterId + '\'' +
                       ", idpUrl='" + idpUrl + '\'' +
                       ", scaApproaches=" + scaApproaches +
                       ", paginationId='" + paginationId + '\'' +
                       '}';
    }
}
