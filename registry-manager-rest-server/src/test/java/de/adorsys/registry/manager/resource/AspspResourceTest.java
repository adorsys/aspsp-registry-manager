package de.adorsys.registry.manager.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.registry.manager.converter.AspspTOConverter;
import de.adorsys.registry.manager.exception.ExceptionHandlingAdvisor;
import de.adorsys.registry.manager.model.AspspTO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pro.javatar.commons.reader.JsonReader;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AspspResourceTest {
    private static final UUID ID = UUID.randomUUID();

    private MockMvc mockMvc;

    @InjectMocks
    private AspspResource resource;

    @Mock
    private AspspService aspspService;

    @Mock
    private AspspTOConverter converter;
    private AspspBO bo;
    private AspspTO to;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        mockMvc = MockMvcBuilders
                          .standaloneSetup(resource)
                          .setControllerAdvice(new ExceptionHandlingAdvisor())
                          .setMessageConverters(new MappingJackson2HttpMessageConverter())
                          .build();

        bo = readYml(AspspBO.class, "aspsp-bo.yml");
        to = readYml(AspspTO.class, "aspsp-to.yml");
    }

    @Test
    public void create() throws Exception {

        when(converter.toAspspBO(to)).thenReturn(bo);
        when(aspspService.save(bo)).thenReturn(bo);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .post("/v1/aspsps")
                                                      .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                                      .content(serialize(to)))
                                      .andDo(print())
                                      .andExpect(status().is(HttpStatus.CREATED.value()))
                                      .andReturn();

        verify(converter, times(1)).toAspspBO(to);
        verify(aspspService, times(1)).save(bo);
    }

    @Test
    public void update() throws Exception {
        when(converter.toAspspBO(to)).thenReturn(bo);
        when(aspspService.save(bo)).thenReturn(bo);
        when(converter.toAspspTO(bo)).thenReturn(to);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                                                      .put("/v1/aspsps")
                                                      .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                                      .content(serialize(to)))
                                      .andDo(print())
                                      .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                                      .andReturn();

        verify(converter, times(1)).toAspspBO(to);
        verify(aspspService, times(1)).save(bo);
    }

    @Test
    public void delete() throws Exception {

        doNothing().when(aspspService).deleteById(ID);

        mockMvc.perform(MockMvcRequestBuilders
                                .delete("/v1/aspsps/{id}", ID)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();

        verify(aspspService, times(1)).deleteById(ID);
    }

    @Test
    public void deleteAll() throws Exception {

        doNothing().when(aspspService).deleteById(ID);

        mockMvc.perform(MockMvcRequestBuilders
                                .delete("/v1/aspsps")
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();

        verify(aspspService, times(1)).deleteAll();
    }

    private <T> T readYml(Class<T> aClass, String fileName) {
        try {
            return YamlReader.getInstance().getObjectFromResource(AspspTOConverter.class, fileName, aClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] serialize(Object obj) {
        try {
            return new ObjectMapper().writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't serialize object");
        }
    }

    private <T> T deserialize(String source, Class<T> tClass) {
        try {
            return JsonReader.getInstance().getObjectFromString(source, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Can't deserialize object", e);
        }
    }
}
