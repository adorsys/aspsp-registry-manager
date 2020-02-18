package de.adorsys.registry.manager.model;

import java.util.Arrays;
import java.util.Objects;

public class AspspEquivalentsReportTO {
    private AspspTO aspsp;
    private int[] linesWithSimilarEntities;

    public AspspTO getAspsp() {
        return aspsp;
    }

    public void setAspsp(AspspTO aspsp) {
        this.aspsp = aspsp;
    }

    public int[] getLinesWithSimilarEntities() {
        return linesWithSimilarEntities;
    }

    public void setLinesWithSimilarEntities(int[] linesWithSimilarEntities) {
        this.linesWithSimilarEntities = linesWithSimilarEntities;
    }

    public boolean hasDuplicates() {
        return linesWithSimilarEntities.length == 0;
    }

    @Override
    public String toString() {
        return "AspspDuplicatsReportBO{" +
            "aspsp=" + aspsp +
            ", lines=" + Arrays.toString(linesWithSimilarEntities) +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspspEquivalentsReportTO that = (AspspEquivalentsReportTO) o;
        return Objects.equals(aspsp, that.aspsp) &&
            Arrays.equals(linesWithSimilarEntities, that.linesWithSimilarEntities);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(aspsp);
        result = 31 * result + Arrays.hashCode(linesWithSimilarEntities);
        return result;
    }
}
