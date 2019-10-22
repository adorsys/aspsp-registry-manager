package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.repository.model.AspspScaApproachPO;
import de.adorsys.registry.manager.service.converter.AspspCsvRecordConverter;
import de.adorsys.registry.manager.service.model.AspspCsvRecord;
import de.adorsys.registry.manager.service.model.AspspScaApproachBO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static de.adorsys.registry.manager.repository.model.AspspScaApproachPO.EMBEDDED;
import static de.adorsys.registry.manager.repository.model.AspspScaApproachPO.REDIRECT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspCsvServiceImplTest {
    private static final UUID ID = UUID.fromString("81cecc67-6d1b-4169-b67c-2de52b99a0cc");
    private static final String ASPSP_NAME = "BNP Paribas Germany, Consorsbank";
    private static final String BIC = "CSDBDE71XXX";
    private static final String URL = "https://xs2a-sndbx.consorsbank.de";
    private static final String ADAPTER_ID = "consors-bank-adapter";
    private static final String BANK_CODE = "76030080";
    private static final String IDP_URL = "https://example.com";
    private static final List<AspspScaApproachPO> SCA_APPROACHES_PO = Arrays.asList(EMBEDDED, REDIRECT);
    private static final List<AspspScaApproachBO> SCA_APPROACHES_BO = Arrays.asList(AspspScaApproachBO.EMBEDDED, AspspScaApproachBO.REDIRECT);

    private static final AspspPO PO = buildAspspPO();
    private static final AspspCsvRecord CSV_RECORD = buildAspspCsvRecord();
    private static final List<AspspPO> POS = List.of(PO);
    private static final List<AspspCsvRecord> CSV_RECORDS = List.of(CSV_RECORD);

    private static final byte[] STORED_BYTES_TEMPLATE
            = "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,EMBEDDED;REDIRECT\n".getBytes();
    private static final byte[] STORED_BYTES_TEMPLATE_WITH_SPACES_IN_SCA_APPROACHES
            = "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,EMBEDDED; REDIRECT\n".getBytes();
    private static final byte[] STORED_BYTES_TEMPLATE_WITH_LOWERCASE_SCA_APPROACHES
            = "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,embedded;redirect\n".getBytes();

    @Mock
    private AspspRepository repository;
    @Mock
    private AspspCsvRecordConverter converter;

    @InjectMocks
    private AspspCsvServiceImpl service;

    @Test
    public void exportCsv() {
        when(repository.findAll()).thenReturn(List.of(PO));
        when(converter.toAspspCsvRecord(PO)).thenReturn(CSV_RECORD);

        byte[] result = service.exportCsv();

        assertThat(result, is(STORED_BYTES_TEMPLATE));
    }

    @Test
    public void importCsv() {
        doNothing().when(repository).deleteAll();
        when(converter.toAspspPOList(CSV_RECORDS)).thenReturn(POS);
        doNothing().when(repository).saveAll(POS);

        service.importCsv(STORED_BYTES_TEMPLATE);

        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).saveAll(POS);
    }

    @Test
    public void importCsv_scaApproachesWithSpaces() {
        doNothing().when(repository).deleteAll();
        when(converter.toAspspPOList(CSV_RECORDS)).thenReturn(POS);
        doNothing().when(repository).saveAll(POS);

        service.importCsv(STORED_BYTES_TEMPLATE_WITH_SPACES_IN_SCA_APPROACHES);

        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).saveAll(POS);
    }

    @Test
    public void importCsv_lowercaseScaApproaches() {
        doNothing().when(repository).deleteAll();
        when(converter.toAspspPOList(CSV_RECORDS)).thenReturn(POS);
        doNothing().when(repository).saveAll(POS);

        service.importCsv(STORED_BYTES_TEMPLATE_WITH_LOWERCASE_SCA_APPROACHES);

        verify(repository, times(1)).deleteAll();
        verify(repository, times(1)).saveAll(POS);
    }

    private static AspspPO buildAspspPO() {
        AspspPO aspsp = new AspspPO();

        aspsp.setId(ID);
        aspsp.setName(ASPSP_NAME);
        aspsp.setBic(BIC);
        aspsp.setUrl(URL);
        aspsp.setAdapterId(ADAPTER_ID);
        aspsp.setBankCode(BANK_CODE);
        aspsp.setIdpUrl(IDP_URL);
        aspsp.setScaApproaches(SCA_APPROACHES_PO);

        return aspsp;
    }

    private static AspspCsvRecord buildAspspCsvRecord() {
        AspspCsvRecord aspsp = new AspspCsvRecord();

        aspsp.setId(ID);
        aspsp.setAspspName(ASPSP_NAME);
        aspsp.setBic(BIC);
        aspsp.setUrl(URL);
        aspsp.setAdapterId(ADAPTER_ID);
        aspsp.setBankCode(BANK_CODE);
        aspsp.setIdpUrl(IDP_URL);
        aspsp.setAspspScaApproaches(SCA_APPROACHES_BO);

        return aspsp;
    }
}