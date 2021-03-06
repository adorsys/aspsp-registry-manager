package de.adorsys.registry.manager.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.adorsys.registry.manager.config.SecurityConfig;
import de.adorsys.registry.manager.converter.AspspTOConverter;
import de.adorsys.registry.manager.model.AspspTO;
import de.adorsys.registry.manager.service.AspspService;
import de.adorsys.registry.manager.service.model.AspspBO;
import de.adorsys.registry.manager.service.model.PageBO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.javatar.commons.reader.JsonReader;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static de.adorsys.registry.manager.resource.AspspResource.ASPSP_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AspspResource.class)
@Import(SecurityConfig.class)
public class AspspResourceTest {
    private static final UUID ID = UUID.randomUUID();
    private static final String ASPSP_BY_ID_URI = ASPSP_URI + "/{id}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AspspService aspspService;

    private AspspBO bo = readYml(AspspBO.class, "aspsp-bo.yml");
    private AspspTO to = readYml(AspspTO.class, "aspsp-to.yml");


    @WithMockUser("user")
    @Test
    public void getAspsps() throws Exception {

        when(aspspService.getByAspsp(any(), anyInt(), anyInt())).thenReturn(new PageBO(List.of(bo), 1L));

        mockMvc.perform(MockMvcRequestBuilders
                                .get(ASPSP_URI)
                                .param("bic", "00000000"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(header().exists("X-Total-Elements"))
                .andExpect(header().longValue("X-Total-Elements", 1L))
                .andReturn();
        verify(aspspService, times(1)).getByAspsp(any(), anyInt(), anyInt());
    }

    @Test
    public void getAspspsRedirectToLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                                .get(ASPSP_URI)
                                .param("bic", "00000000"))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn();
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void create() throws Exception {

        when(aspspService.save(bo)).thenReturn(bo);

        mockMvc.perform(MockMvcRequestBuilders
                                .post(ASPSP_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.CREATED.value()))
                .andReturn();

        verify(aspspService, times(1)).save(bo);
    }

    @Test
    public void createRedirectToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .post(ASPSP_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn();
    }

    @WithMockUser(roles = "READER")
    @Test
    public void createForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .post(ASPSP_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @Test
    public void checkNewAspsp() throws Exception {
        when(aspspService.hasDuplicate(bo)).thenReturn(true);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .post(ASPSP_URI + "/validate")
            .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
            .content(serialize(to)))
            .andDo(print())
            .andExpect(status().is(HttpStatus.OK.value()))
            .andReturn();

        verify(aspspService, times(1)).hasDuplicate(any());

        assertThat(result.getResponse().getContentAsByteArray()).isEqualTo("true".getBytes());
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void update() throws Exception {
        when(aspspService.save(bo)).thenReturn(bo);

        mockMvc.perform(MockMvcRequestBuilders
                                .put(ASPSP_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andReturn();

        verify(aspspService, times(1)).save(bo);
    }

    @Test
    public void updateRedirectToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .put(ASPSP_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn();
    }

    @WithMockUser(roles = "READER")
    @Test
    public void updateForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .put(ASPSP_URI)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @WithMockUser(roles = {"MANAGER", "DEPLOYER"})
    @Test
    public void delete() throws Exception {

        doNothing().when(aspspService).deleteById(ID);

        mockMvc.perform(MockMvcRequestBuilders
                                .delete(ASPSP_BY_ID_URI, ID)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.NO_CONTENT.value()))
                .andReturn();

        verify(aspspService, times(1)).deleteById(ID);
    }

    @Test
    public void deleteRedirectToLoginPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .delete(ASPSP_BY_ID_URI, ID)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andReturn();
    }

    @WithMockUser(roles = "READER")
    @Test
    public void deleteForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                                .delete(ASPSP_BY_ID_URI, ID)
                                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .content(serialize(to)))
                .andDo(print())
                .andExpect(status().is(HttpStatus.FORBIDDEN.value()))
                .andReturn();
    }

    @WithMockUser
    @Test
    public void count() throws Exception {
        long total = 10L;

        when(aspspService.count()).thenReturn(total);

        mockMvc.perform(MockMvcRequestBuilders.get(ASPSP_URI + "/count"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().string(String.valueOf(total)))
            .andReturn();
    }

    @WithMockUser
    @Test
    public void countEmptyBase() throws Exception {
        long total = 0;

        when(aspspService.count()).thenReturn(total);

        mockMvc.perform(MockMvcRequestBuilders.get(ASPSP_URI + "/count"))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().string(String.valueOf(total)))
            .andReturn();
    }

    @Test
    public void countRedirectToLoginPage() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get(ASPSP_URI + "/count"))
            .andExpect(status().is(HttpStatus.FOUND.value()))
            .andReturn();
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
