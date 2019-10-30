package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.config.SecurityConfig;
import de.adorsys.registry.manager.service.AspspCsvService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AspspCsvResource.class)
@Import(SecurityConfig.class)
public class AspspCsvResourceTest {
    private static final String BASE_URI = "/v1/aspsps/csv";

    private static final byte[] STORED_BYTES_TEMPLATE
            = "81cecc67-6d1b-4169-b67c-2de52b99a0cc,\"BNP Paribas Germany, Consorsbank\",CSDBDE71XXX,https://xs2a-sndbx.consorsbank.de,consors-bank-adapter,76030080"
                      .getBytes();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AspspCsvService service;

    @WithMockUser(roles = {"MANAGER", "DEPLOYER", "READER"})
    @Test
    public void export() throws Exception {
        when(service.exportCsv()).thenReturn(STORED_BYTES_TEMPLATE);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .get(BASE_URI + "/download"))
                                      .andExpect(status().is(HttpStatus.OK.value()))
                                      .andReturn();

        byte[] result = mvcResult.getResponse().getContentAsByteArray();

        assertThat(Arrays.equals(result, STORED_BYTES_TEMPLATE), is(true));
    }

    @Test
    public void exportToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .get(BASE_URI + "/export"))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn();
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void importCsv() throws Exception {
        doNothing().when(service).importCsv(any());

        mockMvc.perform(multipart(BASE_URI + "/upload")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.OK.value()));

        verify(service, times(1)).importCsv(any());
    }

    @Test
    public void importCsvRedirectToLoginPage() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/import")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    @WithMockUser(roles = "READER")
    @Test
    public void importCsvForbidden() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/import")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void merge() throws Exception {
        doNothing().when(service).merge(any());

        mockMvc.perform(multipart(BASE_URI + "/merge")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()));

        verify(service, times(1)).merge(any());
    }

    @Test
    public void mergeRedirectToLoginPage() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/merge")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    @WithMockUser(roles = "READER")
    @Test
    public void mergeForbidden() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/merge")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }
}
