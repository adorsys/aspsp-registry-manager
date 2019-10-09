package de.adorsys.registry.manager.repository.model;

import java.util.List;
import java.util.Objects;

public class AspspPO {
    private Long id;
    private String name;
    private String bic;
    private String bankCode;
    private String url;
    private String adapterId;
    private String idpUrl;
    private List<AspspScaApproachPO> scaApproaches;
    private String paginationId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<AspspScaApproachPO> getScaApproaches() {
        return scaApproaches;
    }

    public void setScaApproaches(List<AspspScaApproachPO> scaApproaches) {
        this.scaApproaches = scaApproaches;
    }

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
        AspspPO aspspPO = (AspspPO) o;
        return Objects.equals(id, aspspPO.id) &&
                       Objects.equals(name, aspspPO.name) &&
                       Objects.equals(bic, aspspPO.bic) &&
                       Objects.equals(bankCode, aspspPO.bankCode) &&
                       Objects.equals(url, aspspPO.url) &&
                       Objects.equals(adapterId, aspspPO.adapterId) &&
                       Objects.equals(idpUrl, aspspPO.idpUrl) &&
                       Objects.equals(scaApproaches, aspspPO.scaApproaches) &&
                       Objects.equals(paginationId, aspspPO.paginationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bic, bankCode, url, adapterId, idpUrl, scaApproaches, paginationId);
    }

    @Override
    public String toString() {
        return "AspspPO{" +
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
