package de.adorsys.registry.manager.repository.model;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "aspsps")
public class AspspEntity {

    @Id
    private UUID id;
    private String name;
    private String bic;
    private String bankCode;
    private String url;
    private String adapterId;
    private String idpUrl;

    // TODO discuss this field design and look for better approaches (String?)
    @ElementCollection(targetClass = AspspScaApproachPO.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "sca_approaches", joinColumns = @JoinColumn(name = "aspsp_id"))
    private List<AspspScaApproachPO> scaApproaches;
    private String paginationId;

    @Column(insertable = false, updatable = false, columnDefinition = "serial")
    private Integer lineNumber;

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

    public Integer getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspspEntity that = (AspspEntity) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(bic, that.bic) &&
            Objects.equals(bankCode, that.bankCode) &&
            Objects.equals(url, that.url) &&
            Objects.equals(adapterId, that.adapterId) &&
            Objects.equals(idpUrl, that.idpUrl) &&
            Objects.equals(scaApproaches, that.scaApproaches) &&
            Objects.equals(paginationId, that.paginationId) &&
            Objects.equals(lineNumber, that.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, bic, bankCode, url, adapterId, idpUrl, scaApproaches, paginationId, lineNumber);
    }

    @Override
    public String toString() {
        return "AspspEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", bic='" + bic + '\'' +
            ", bankCode='" + bankCode + '\'' +
            ", url='" + url + '\'' +
            ", adapterId='" + adapterId + '\'' +
            ", idpUrl='" + idpUrl + '\'' +
            ", scaApproaches=" + scaApproaches +
            ", paginationId='" + paginationId + '\'' +
            ", lineNumber=" + lineNumber +
            '}';
    }
}
