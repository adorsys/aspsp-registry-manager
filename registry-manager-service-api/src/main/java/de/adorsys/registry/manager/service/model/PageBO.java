package de.adorsys.registry.manager.service.model;

import java.util.List;
import java.util.Objects;

public class PageBO {

    private List<AspspBO> content;
    private long totalElements;

    public PageBO() {
    }

    public PageBO(List<AspspBO> content, long totalElements) {
        this.content = content;
        this.totalElements = totalElements;
    }

    public List<AspspBO> getContent() {
        return content;
    }

    public void setContent(List<AspspBO> content) {
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
        PageBO pageBO = (PageBO) o;
        return totalElements == pageBO.totalElements &&
            Objects.equals(content, pageBO.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, totalElements);
    }

    @Override
    public String toString() {
        return "PageBO{" +
            "bos=" + content +
            ", pages=" + totalElements +
            '}';
    }
}
