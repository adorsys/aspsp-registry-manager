package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.service.AspspCsvService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.google.common.base.CharMatcher.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspOutboundServiceImplTest {

    private static final byte[] STORED_BYTES_TEMPLATE
        = "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080,https://example.com,EMBEDDED;REDIRECT\n".getBytes();

    @InjectMocks
    private AspspOutboundServiceImpl service;

    @Mock
    private AspspCsvService aspspCsvService;

    @Captor
    private ArgumentCaptor<byte[]> captor;

    @Test
    public void importData() {

        doNothing().when(aspspCsvService).importCsv(any());

        service.importData(STORED_BYTES_TEMPLATE);

        verify(aspspCsvService, times(1)).importCsv(captor.capture());

        assertEquals(STORED_BYTES_TEMPLATE.length, captor.getValue().length);
        assertEquals(STORED_BYTES_TEMPLATE, captor.getValue());
    }

    @Test
    public void exportData() throws IOException {
        when(aspspCsvService.exportCsv()).thenReturn(STORED_BYTES_TEMPLATE);

        MultipartFile output = service.exportData();

        assertNotNull(output);
        assertEquals("multipart/form-data", output.getContentType());
    }
}
