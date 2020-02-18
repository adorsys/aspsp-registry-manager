package de.adorsys.registry.manager.service.validator.duplicates;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspEquivalentsReportBO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EquivalentsSearcherImpl implements EquivalentsSearcher {

    @Override
    public List<AspspEquivalentsReportBO> equivalentsLookup(List<AspspBO> aspsps) {
        List<AspspEquivalentsReportBO> output = new ArrayList<>();

        // determining if there are any similar entries within input list
        List<AspspWrapper> duplicateEntities = convertToAspspWrapperList(aspsps)
            .stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
            .entrySet()
            .stream()
            .filter(item -> item.getValue() > 1L)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        // determining on which line duplicates are
        duplicateEntities.forEach(item -> {
            AspspBO example = item.getAspsp();

            int[] lines = IntStream
                .range(0, aspsps.size())
                .filter(index -> isAlike(aspsps.get(index), example))
                .map(index -> ++index)
                .toArray();

            output.add(new AspspEquivalentsReportBO(item.getAspsp(), lines));
        });

        return output;
    }

    private boolean isAlike(AspspBO item, AspspBO aspsp) {
        return Objects.equals(item.getBic(), aspsp.getBic())
            && Objects.equals(item.getBankCode(), aspsp.getBankCode());
    };

    private class AspspWrapper {
        private final AspspBO aspsp;
        private final String bic;
        private final String bankCode;

        public AspspWrapper(AspspBO aspsp) {
            this.aspsp = aspsp;
            this.bic = aspsp.getBic();
            this.bankCode = aspsp.getBankCode();
        }

        public AspspBO getAspsp() {
            return aspsp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AspspWrapper that = (AspspWrapper) o;
            return Objects.equals(bic, that.bic) &&
                Objects.equals(bankCode, that.bankCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bic, bankCode);
        }
    }

    private List<AspspWrapper> convertToAspspWrapperList(List<AspspBO> list) {
        return list.stream().map(AspspWrapper::new).collect(Collectors.toList());
    }
}
