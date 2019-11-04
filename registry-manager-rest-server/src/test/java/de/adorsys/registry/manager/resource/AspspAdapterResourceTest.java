package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.client.AspspAdapterClient;
import de.adorsys.registry.manager.config.SecurityConfig;
import de.adorsys.registry.manager.service.AspspAdapterService;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AspspAdapterResource.class)
@Import(SecurityConfig.class)
public class AspspAdapterResourceTest {
    private static final String BASE_URI = "/v1/aspsps/adapter";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AspspAdapterService service;

    @MockBean
    private AspspAdapterClient client;

    @Test
    public void importDataRedirectToLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/import"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andReturn();
    }

    @WithMockUser(roles = "DEPLOYER")
    @Test
    public void importData() throws Exception {
        doNothing().when(service).importData(any());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/import"))
            .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
            .andReturn();

        verify(service, times(1)).importData(any());
    }

    @WithMockUser(roles = {"READER", "MANAGER"})
    @Test
    public void importDataForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/import"))
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
            .andReturn();
    }

    @Test
    public void exportDataRedirectToLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/export"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andReturn();
    }

    @WithMockUser(roles = "DEPLOYER")
    @Test
    public void exportData() throws Exception {
        doNothing().when(client).exportFile(any());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/export"))
            .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
            .andReturn();

        verify(client, times(1)).exportFile(any());
    }

    @WithMockUser(roles = {"READER", "MANAGER"})
    @Test
    public void exportDataForbidden() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/export"))
            .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
            .andReturn();
    }
}
