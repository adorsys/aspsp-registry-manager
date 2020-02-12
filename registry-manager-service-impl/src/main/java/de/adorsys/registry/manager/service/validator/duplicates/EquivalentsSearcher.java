package de.adorsys.registry.manager.service.validator.duplicates;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspEquivalentsReportBO;

import java.util.List;

public interface EquivalentsSearcher {

    /**
     * Generates a report that contains result of equivalents searching within provided
     * list of {@link AspspBO}.
     *
     *<p>
     *     Note: two or more records are considered as similar or equivalent if those
     *     have at least the same BIC and BankCode fields
     *</p>
     *
     * @param aspsps list of aspsps to be checked
     * @return {@link AspspEquivalentsReportBO} report that contains results
     */
    List<AspspEquivalentsReportBO> equivalentsLookup(List<AspspBO> aspsps);
}
