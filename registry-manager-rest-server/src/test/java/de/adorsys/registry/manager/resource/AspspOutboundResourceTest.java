package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.client.AspspOutboundClient;
import de.adorsys.registry.manager.config.SecurityConfig;
import de.adorsys.registry.manager.exception.ExceptionHandlingAdvisor;
import de.adorsys.registry.manager.service.AspspOutboundService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AspspOutboundResource.class)
@Import(SecurityConfig.class)
public class AspspOutboundResourceTest {
    private static final String BASE_URI = "/v1/aspsps/outbound";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AspspOutboundService service;

    @MockBean
    private AspspOutboundClient client;

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
