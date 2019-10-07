package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.AspspTO;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AspspTOConverterTest {

    private AspspTOConverter converter;
    private AspspBO bo;
    private AspspTO to;

    @Before
    public void setUp() throws Exception {
        converter = Mappers.getMapper(AspspTOConverter.class);
        bo = readYml(AspspBO.class, "aspsp-bo.yml");
        to = readYml(AspspTO.class, "aspsp-to.yml");
    }

    @Test
    public void toAspspTO() {
        AspspTO actual = converter.toAspspTO(bo);

        assertThat(actual, is(to));
    }

    @Test
    public void toAspspTOList() {
        List<AspspTO> actualList = converter.toAspspTOList(Collections.singletonList(bo));

        assertThat(actualList, hasSize(1));
        assertThat(actualList.get(0), is(to));
    }

    @Test
    public void toAspspBO() {
        AspspBO actual = converter.toAspspBO(to);

        assertThat(actual, is(bo));
    }

    private <T> T readYml(Class<T> aClass, String fileName) {
        try {
            return YamlReader.getInstance().getObjectFromResource(getClass(), fileName, aClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}