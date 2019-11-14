package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.repository.model.AspspScaApproachPO;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.converter.AspspCsvRecordConverter;
import de.adorsys.registry.manager.service.converter.AspspCsvRecordConverterImpl;
import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.AspspCsvRecord;
import de.adorsys.registry.manager.service.model.AspspScaApproachBO;
import de.adorsys.registry.manager.service.model.CsvFileValidationReportBO;
import de.adorsys.registry.manager.service.validator.AspspValidationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import static de.adorsys.registry.manager.repository.model.AspspScaApproachPO.EMBEDDED;
import static de.adorsys.registry.manager.repository.model.AspspScaApproachPO.REDIRECT;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
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
    private static final AspspBO BO = buildAspspBO();
    private static final AspspCsvRecord CSV_RECORD = buildAspspCsvRecord();
    private static final List<AspspPO> POS = List.of(PO);
    private static final List<AspspBO> BOS = List.of(BO);
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
    AspspBOConverter aspspBOConverter;
    @Spy
    private AspspCsvRecordConverter converter = new AspspCsvRecordConverterImpl();
    @Spy
    private UUIDGeneratorService uuidGeneratorService;
    @Mock
    private AspspValidationService aspspValidationService;

    @Captor
    private ArgumentCaptor<List<AspspPO>> captor;

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
        doNothing().when(repository).delete();
        when(converter.toAspspPOList(CSV_RECORDS)).thenReturn(POS);
        when(uuidGeneratorService.checkAndUpdateUUID(anyListOf(AspspPO.class))).thenReturn(POS);
        doNothing().when(repository).saveAll(POS);

        service.importCsv(STORED_BYTES_TEMPLATE);

        verify(uuidGeneratorService, times(1)).checkAndUpdateUUID(anyListOf(AspspPO.class));
        verify(repository, times(1)).delete();
        verify(repository, times(1)).saveAll(POS);
    }

    @Test
    public void importCsv_scaApproachesWithSpaces() {
        doNothing().when(repository).delete();
        when(converter.toAspspPOList(CSV_RECORDS)).thenReturn(POS);
        when(uuidGeneratorService.checkAndUpdateUUID(anyListOf(AspspPO.class))).thenReturn(POS);
        doNothing().when(repository).saveAll(POS);

        service.importCsv(STORED_BYTES_TEMPLATE_WITH_SPACES_IN_SCA_APPROACHES);

        verify(uuidGeneratorService, times(1)).checkAndUpdateUUID(anyListOf(AspspPO.class));
        verify(repository, times(1)).delete();
        verify(repository, times(1)).saveAll(POS);
    }

    @Test
    public void importCsv_lowercaseScaApproaches() {
        doNothing().when(repository).delete();
        when(converter.toAspspPOList(CSV_RECORDS)).thenReturn(POS);
        when(uuidGeneratorService.checkAndUpdateUUID(anyListOf(AspspPO.class))).thenReturn(POS);
        doNothing().when(repository).saveAll(POS);

        service.importCsv(STORED_BYTES_TEMPLATE_WITH_LOWERCASE_SCA_APPROACHES);

        verify(uuidGeneratorService, times(1)).checkAndUpdateUUID(anyListOf(AspspPO.class));
        verify(repository, times(1)).delete();
        verify(repository, times(1)).saveAll(POS);
    }

    @Test
    public void merge_forSave() {
        List<AspspPO> database;
        byte[] input = generateInput();

        database = Arrays.asList(PO, PO, PO, PO);

        doNothing().when(repository).saveAll(anyListOf(AspspPO.class));
        doNothing().when(repository).delete(anyListOf(AspspPO.class));
        when(repository.findAll()).thenReturn(database);

        service.merge(input);

        verify(uuidGeneratorService, times(1)).checkAndUpdateUUID(anyListOf(AspspPO.class));
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).saveAll(captor.capture());
        verify(repository, times(1)).delete(anyListOf(AspspPO.class));

//        get forSave list
        assertEquals(3, captor.getValue().size());
        assertTrue(captor.getValue().contains(PO));
    }

    @Test
    public void merge_forDeleting() {
        AspspPO POS_updated = buildAspspPO();
        POS_updated.setId(UUID.randomUUID());
        List<AspspPO> test = new LinkedList<>();
        test.add(POS_updated);

        doNothing().when(repository).saveAll(anyListOf(AspspPO.class));
        doNothing().when(repository).delete(anyListOf(AspspPO.class));
        when(repository.findAll()).thenReturn(POS);
        when(converter.toAspspPOList(anyListOf(AspspCsvRecord.class))).thenReturn(test);
        when(uuidGeneratorService.checkAndUpdateUUID(anyListOf(AspspPO.class))).thenReturn(test);

        service.merge(STORED_BYTES_TEMPLATE);

        verify(uuidGeneratorService, times(1)).checkAndUpdateUUID(anyListOf(AspspPO.class));
        verify(converter, times(1)).toAspspPOList(anyListOf(AspspCsvRecord.class));
        verify(repository, times(1)).findAll();
        verify(repository, times(1)).saveAll(captor.capture());
        verify(repository, times(1)).delete(captor.capture());

//        get forDeleting list
        assertEquals(POS.size(), captor.getAllValues().get(1).size());
        assertThat(PO, is(captor.getAllValues().get(1).get(0)));
//        get forSave list
        assertEquals(test.size(), captor.getAllValues().get(0).size());
        assertThat(POS_updated, is(captor.getAllValues().get(0).get(0)));
    }

    @Test
    public void validateCsv_Success() {
        CsvFileValidationReportBO validationReport = new CsvFileValidationReportBO();
        validationReport.valid();

        when(aspspBOConverter.csvRecordListToAspspBOList(List.of(CSV_RECORD))).thenReturn(BOS);
        when(aspspValidationService.validate(BOS)).thenReturn(validationReport);

        CsvFileValidationReportBO actual = service.validateCsv(STORED_BYTES_TEMPLATE);

        assertEquals(validationReport, actual);
    }

    @Test
    public void validateCsv_Failure() {
        CsvFileValidationReportBO validationReport = new CsvFileValidationReportBO();
        validationReport.notValid();

        when(aspspBOConverter.csvRecordListToAspspBOList(List.of(CSV_RECORD))).thenReturn(BOS);
        when(aspspValidationService.validate(BOS)).thenReturn(validationReport);

        CsvFileValidationReportBO actual = service.validateCsv(STORED_BYTES_TEMPLATE);

        assertEquals(validationReport, actual);
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

    private static AspspBO buildAspspBO() {
        AspspBO aspsp = new AspspBO();

        aspsp.setId(ID);
        aspsp.setName(ASPSP_NAME);
        aspsp.setBic(BIC);
        aspsp.setUrl(URL);
        aspsp.setAdapterId(ADAPTER_ID);
        aspsp.setBankCode(BANK_CODE);
        aspsp.setIdpUrl(IDP_URL);
        aspsp.setScaApproaches(SCA_APPROACHES_BO);

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

    private byte[] generateInput() {
        String records =
            "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,EMBEDDED;REDIRECT\n" +
            "9ec1702e-fa39-11e9-8f0b-362b9e155667,\"BNP Paribas Germany, Consorsbank\",CSDBDE71111,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,EMBEDDED;REDIRECT\n" +
            "9ec1729a-fa39-11e9-8f0b-362b9e155667,\"BNP Paribas Germany, Consorsbank\",CSDBDE71222,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,EMBEDDED;REDIRECT\n";

        return records.getBytes();
    }
}
