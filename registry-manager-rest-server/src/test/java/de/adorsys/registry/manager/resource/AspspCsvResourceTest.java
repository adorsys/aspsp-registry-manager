package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.exception.ExceptionHandlingAdvisor;
import de.adorsys.registry.manager.service.AspspCsvService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AspspCsvResourceTest {
    private static final byte[] STORED_BYTES_TEMPLATE
            = "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080"
                      .getBytes();

    private MockMvc mockMvc;

    @Mock
    private AspspCsvService service;

    @InjectMocks
    private AspspCsvResource resource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(resource)
                          .setMessageConverters()
                          .setControllerAdvice(new ExceptionHandlingAdvisor())
                          .build();
    }

    @Test
    public void export() throws Exception {
        when(service.exportCsv()).thenReturn(STORED_BYTES_TEMPLATE);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .get("/v1/aspsps/csv/export"))
                                      .andExpect(status().is(HttpStatus.OK.value()))
                                      .andReturn();

        byte[] result = mvcResult.getResponse().getContentAsByteArray();

        assertThat(Arrays.equals(result, STORED_BYTES_TEMPLATE), is(true));
    }

    @Test
    public void importCsv() throws Exception {
        doNothing().when(service).importCsv(any());

        mockMvc.perform(multipart("/v1/aspsps/csv/import")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.OK.value()));

        verify(service, times(1)).importCsv(any());
    }
}