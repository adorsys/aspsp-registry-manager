package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.config.SecurityConfig;
import de.adorsys.registry.manager.converter.CsvFileImportValidationReportTOConverterImpl;
import de.adorsys.registry.manager.converter.CsvFileMergeValidationReportTOConverterImpl;
import de.adorsys.registry.manager.model.CsvFileImportValidationReportTO;
import de.adorsys.registry.manager.model.CsvFileMergeValidationReportTO;
import de.adorsys.registry.manager.model.FileValidationReportTO;
import de.adorsys.registry.manager.model.FileValidationReportTO.ValidationResultTO;
import de.adorsys.registry.manager.service.AspspCsvService;
import de.adorsys.registry.manager.service.model.CsvFileImportValidationReportBO;
import de.adorsys.registry.manager.service.model.CsvFileMergeValidationReportBO;
import de.adorsys.registry.manager.service.model.FileValidationReportBO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.javatar.commons.reader.JsonReader;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
    @SpyBean
    private CsvFileImportValidationReportTOConverterImpl importValidationReportConverter;
    @SpyBean
    private CsvFileMergeValidationReportTOConverterImpl mergeValidationReportConverter;

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
                                .get(BASE_URI + "/download"))
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
        mockMvc.perform(multipart(BASE_URI + "/upload")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    @WithMockUser(roles = "READER")
    @Test
    public void importCsvForbidden() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/upload")
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

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void validateImportCsv_Success() throws Exception {
        FileValidationReportBO fileValidationReportBO = new FileValidationReportBO(aspspDuplicatsReports);
        fileValidationReportBO.valid();

        CsvFileImportValidationReportBO validationReportBO = new CsvFileImportValidationReportBO(1, 1, fileValidationReportBO);

        FileValidationReportTO fileValidationReportTO = new FileValidationReportTO();
        fileValidationReportTO.setValidationResult(ValidationResultTO.VALID);

        CsvFileImportValidationReportTO validationReportTO = new CsvFileImportValidationReportTO();
        validationReportTO.setCsvFileRecordsNumber(1);
        validationReportTO.setDbRecordsNumber(1);
        validationReportTO.setFileValidationReport(fileValidationReportTO);

        when(service.validateImportCsv(any())).thenReturn(validationReportBO);

        MvcResult mvcResult = mockMvc.perform(multipart(BASE_URI + "/validate/upload")
                                                      .file("file", "content".getBytes()))
                                      .andExpect(status().is(HttpStatus.OK.value()))
                                      .andReturn();

        CsvFileImportValidationReportTO actual = JsonReader.getInstance()
                                                         .getObjectFromString(mvcResult.getResponse().getContentAsString(), CsvFileImportValidationReportTO.class);

        assertEquals(validationReportTO, actual);
        verify(service, times(1)).validateImportCsv(any());
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void validateImportCsv_Failure() throws Exception {
        FileValidationReportBO fileValidationReportBO = new FileValidationReportBO(aspspDuplicatsReports);
        fileValidationReportBO.notValid();

        CsvFileImportValidationReportBO validationReportBO = new CsvFileImportValidationReportBO(1, 1, fileValidationReportBO);

        FileValidationReportTO fileValidationReportTO = new FileValidationReportTO();
        fileValidationReportTO.setValidationResult(ValidationResultTO.NOT_VALID);
        fileValidationReportTO.setTotalNotValidRecords(0);

        CsvFileImportValidationReportTO validationReportTO = new CsvFileImportValidationReportTO();
        validationReportTO.setCsvFileRecordsNumber(1);
        validationReportTO.setDbRecordsNumber(1);
        validationReportTO.setFileValidationReport(fileValidationReportTO);

        when(service.validateImportCsv(any())).thenReturn(validationReportBO);

        MvcResult mvcResult = mockMvc.perform(multipart(BASE_URI + "/validate/upload")
                                                      .file("file", "content".getBytes()))
                                      .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                                      .andReturn();

        CsvFileImportValidationReportTO actual = JsonReader.getInstance()
                                                         .getObjectFromString(mvcResult.getResponse().getContentAsString(), CsvFileImportValidationReportTO.class);

        assertEquals(validationReportTO, actual);
        verify(service, times(1)).validateImportCsv(any());
    }

    @WithMockUser(roles = "READER")
    @Test
    public void validateImportCsvForbidden() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/validate/upload")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void validateImportCsvRedirectToLoginPage() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/validate/upload")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FOUND.value()));
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void validateMergeCsv_Success() throws Exception {
        FileValidationReportBO fileValidationReportBO = new FileValidationReportBO(aspspDuplicatsReports);
        fileValidationReportBO.valid();

        CsvFileMergeValidationReportBO validationReportBO = new CsvFileMergeValidationReportBO(0, Set.of(), fileValidationReportBO);

        FileValidationReportTO fileValidationReportTO = new FileValidationReportTO();
        fileValidationReportTO.setValidationResult(ValidationResultTO.VALID);

        CsvFileMergeValidationReportTO validationReportTO = new CsvFileMergeValidationReportTO();
        validationReportTO.setNumberOfNewRecords(0);
        validationReportTO.setDifference(Set.of());
        validationReportTO.setFileValidationReport(fileValidationReportTO);

        when(service.validateMergeCsv(any())).thenReturn(validationReportBO);

        MvcResult mvcResult = mockMvc.perform(multipart(BASE_URI + "/validate/merge")
                                                      .file("file", "content".getBytes()))
                                      .andExpect(status().is(HttpStatus.OK.value()))
                                      .andReturn();

        CsvFileMergeValidationReportTO actual = JsonReader.getInstance()
                                                        .getObjectFromString(mvcResult.getResponse().getContentAsString(), CsvFileMergeValidationReportTO.class);

        assertEquals(validationReportTO, actual);
        verify(service, times(1)).validateMergeCsv(any());
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void validateMergeCsv_Failure() throws Exception {
        FileValidationReportBO fileValidationReportBO = new FileValidationReportBO(aspspDuplicatsReports);
        fileValidationReportBO.notValid();

        CsvFileMergeValidationReportBO validationReportBO = new CsvFileMergeValidationReportBO(0, Set.of(), fileValidationReportBO);

        FileValidationReportTO fileValidationReportTO = new FileValidationReportTO();
        fileValidationReportTO.setValidationResult(ValidationResultTO.NOT_VALID);
        fileValidationReportTO.setTotalNotValidRecords(0);

        CsvFileMergeValidationReportTO validationReportTO = new CsvFileMergeValidationReportTO();
        validationReportTO.setNumberOfNewRecords(0);
        validationReportTO.setDifference(Set.of());
        validationReportTO.setFileValidationReport(fileValidationReportTO);

        when(service.validateMergeCsv(any())).thenReturn(validationReportBO);

        MvcResult mvcResult = mockMvc.perform(multipart(BASE_URI + "/validate/merge")
                                                      .file("file", "content".getBytes()))
                                      .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                                      .andReturn();

        CsvFileMergeValidationReportTO actual = JsonReader.getInstance()
                                                        .getObjectFromString(mvcResult.getResponse().getContentAsString(), CsvFileMergeValidationReportTO.class);

        assertEquals(validationReportTO, actual);
        verify(service, times(1)).validateMergeCsv(any());
    }

    @WithMockUser(roles = "READER")
    @Test
    public void validateMergeCsvForbidden() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/validate/merge")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    public void validateMergeCsvRedirectToLoginPage() throws Exception {
        mockMvc.perform(multipart(BASE_URI + "/validate/merge")
                                .file("file", "content".getBytes()))
                .andExpect(status().is(HttpStatus.FOUND.value()));
    }
}
