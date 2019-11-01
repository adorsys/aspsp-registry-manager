package de.adorsys.registry.manager.resource;

import de.adorsys.registry.manager.client.AspspOutboundClient;
import de.adorsys.registry.manager.exception.ExceptionHandlingAdvisor;
import de.adorsys.registry.manager.service.AspspOutboundService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AspspOutboundResourceTest {
    private static final String BASE_URI = "/v1/aspsps/outbound";

    private MockMvc mockMvc;

    @InjectMocks
    private AspspOutboundResource resource;

    @Mock
    private AspspOutboundService service;

    @Mock
    private AspspOutboundClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders.standaloneSetup(resource)
            .setMessageConverters()
            .setControllerAdvice(new ExceptionHandlingAdvisor())
            .build();
    }

    @Test
    public void importData() throws Exception {
        doNothing().when(service).importData(any());

        mockMvc.perform(MockMvcRequestBuilders.get(BASE_URI + "/import"))
            .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
            .andReturn();

        verify(service, times(1)).importData(any());
    }

    @Test
    public void exportData() throws Exception {
        doNothing().when(client).exportFile(any());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_URI + "/export"))
            .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
            .andReturn();

        verify(client, times(1)).exportFile(any());
    }
}
