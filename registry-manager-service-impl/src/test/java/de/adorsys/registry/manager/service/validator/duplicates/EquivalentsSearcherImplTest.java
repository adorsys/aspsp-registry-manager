package de.adorsys.registry.manager.service.validator.duplicates;

import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspEquivalentsReportBO;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class EquivalentsSearcherImplTest {

    private static final String BANK_NAME = "test-bank-adapter";
    private static final String ADAPTER_ID = "test-bank-adapter";
    private static final String BIC_1 = "BANKABICXXX";
    private static final String BANK_CODE_1 = "11111111";
    private static final String BIC_2 = "BANKABICYYY";
    private static final String BANK_CODE_2 = "22222222";
    private static final String URL_VALID = "https://example.com";

    private AspspBO aspspFirst = buildFirstAspsp();
    private AspspBO aspspSecond = buildSecondAspsp();
    private AspspBO aspspBicOnly = buildBicOnlyAspsp();
    private AspspBO aspspBankCodeOnly = buildBankCodeOnlyAspsp();

    private EquivalentsSearcher searcher = new EquivalentsSearcherImpl();

    @Test
    public void equivalentsLookup_returnListWithOneDuplicate() {
        List<AspspBO> aspspsList = Arrays.asList(aspspFirst, aspspSecond, aspspFirst);

        List<AspspEquivalentsReportBO> actual = searcher.equivalentsLookup(aspspsList);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(actual.get(0).getAspsp(), aspspFirst);
        assertEquals(actual.get(0).getLinesWithSimilarEntities().length, 2);
        assertEquals(actual.get(0).getLinesWithSimilarEntities()[1], 3);
    }

    @Test
    public void equivalentsLookup_returnListWithOneDuplicate_bankCodeOnly() {
        List<AspspBO> aspspsList = Arrays.asList(aspspBankCodeOnly, aspspSecond, aspspBankCodeOnly, new AspspBO());

        List<AspspEquivalentsReportBO> actual = searcher.equivalentsLookup(aspspsList);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(actual.get(0).getAspsp(), aspspBankCodeOnly);
        assertEquals(actual.get(0).getLinesWithSimilarEntities().length, 2);
        assertEquals(actual.get(0).getLinesWithSimilarEntities()[1], 3);
    }

    @Test
    public void equivalentsLookup_returnListWithOneDuplicate_bicOnly() {
        List<AspspBO> aspspsList = Arrays.asList(aspspBicOnly, aspspSecond, aspspBicOnly, new AspspBO());

        List<AspspEquivalentsReportBO> actual = searcher.equivalentsLookup(aspspsList);

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(actual.get(0).getAspsp(), aspspBicOnly);
        assertEquals(actual.get(0).getLinesWithSimilarEntities().length, 2);
        assertEquals(actual.get(0).getLinesWithSimilarEntities()[1], 3);
    }

    @Test
    public void equivalentsLookup_returnListWithMoreThanOneDuplicate() {
        List<AspspBO> aspspsList = Arrays.asList(aspspFirst, aspspSecond, aspspFirst, aspspSecond, aspspFirst);

        List<AspspEquivalentsReportBO> actual = searcher.equivalentsLookup(aspspsList);

        assertNotNull(actual);
        assertEquals(2, actual.size());

        actual.forEach(item -> {
            if (item.getAspsp().equals(aspspFirst)) {
                assertEquals(item.getLinesWithSimilarEntities().length, 3);
                assertEquals(item.getLinesWithSimilarEntities()[2], 5);
            }
            if (item.getAspsp().equals(aspspSecond)) {
                assertEquals(item.getLinesWithSimilarEntities().length, 2);
                assertEquals(item.getLinesWithSimilarEntities()[1], 4);
            }
        });
    }

    @Test
    public void equivalentsLookup_noDuplicates() {
        List<AspspBO> aspspsList = Arrays.asList(aspspFirst, aspspSecond, new AspspBO());

        List<AspspEquivalentsReportBO> actual = searcher.equivalentsLookup(aspspsList);

        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    private AspspBO buildFirstAspsp() {
        AspspBO aspsp = new AspspBO();

        aspsp.setId(UUID.randomUUID());
        aspsp.setBankCode(BANK_CODE_1);
        aspsp.setName(BANK_NAME);
        aspsp.setAdapterId(ADAPTER_ID);
        aspsp.setBic(BIC_1);
        aspsp.setUrl(URL_VALID);

        return aspsp;
    }

    private AspspBO buildSecondAspsp() {
        AspspBO aspsp = buildFirstAspsp();

        aspsp.setBic(BIC_2);
        aspsp.setBankCode(BANK_CODE_2);

        return aspsp;
    }

    private AspspBO buildBicOnlyAspsp() {
        AspspBO aspsp = buildFirstAspsp();

        aspsp.setBankCode(null);

        return aspsp;
    }

    private AspspBO buildBankCodeOnlyAspsp() {
        AspspBO aspsp = buildFirstAspsp();

        aspsp.setBic(null);

        return aspsp;
    }
}
