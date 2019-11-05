package de.adorsys.registry.manager.repository.model;

import java.util.List;
import java.util.Objects;

public class PagePO {

    private List<AspspPO> content;
    private long totalElements;

    public PagePO() {
    }

    public PagePO(List<AspspPO> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<AspspPO> getContent() {
        return content;
    }

    public void setContent(List<AspspPO> content) {
        this.content = content;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagePO pagePO = (PagePO) o;
        return totalElements == pagePO.totalElements &&
            Objects.equals(content, pagePO.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements);
    }

    @Override
    public String toString() {
        return "PagePO{" +
            "aspspPOS=" + content +
            ", totalElements=" + totalElements +
            '}';
    }
}
