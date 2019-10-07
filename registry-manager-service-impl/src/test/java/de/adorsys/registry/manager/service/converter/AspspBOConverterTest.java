package de.adorsys.registry.manager.service.converter;

import de.adorsys.registry.manager.repository.model.AspspPO;
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

public class AspspBOConverterTest {

    private AspspBOConverter converter;
    private AspspBO bo;
    private AspspPO po;

    @Before
    public void setUp() throws Exception {
        converter = Mappers.getMapper(AspspBOConverter.class);
        bo = readYml(AspspBO.class, "aspsp-bo.yml");
        po = readYml(AspspPO.class, "aspsp-po.yml");
    }


    @Test
    public void toAspspBO() {
        AspspBO actual = converter.toAspspBO(po);

        assertThat(actual, is(bo));
    }

    @Test
    public void toAspspPO() {
        AspspPO actual = converter.toAspspPO(bo);

        assertThat(actual, is(po));
    }

    @Test
    public void toAspspBOList() {
        List<AspspBO> actualList = converter.toAspspBOList(Collections.singletonList(po));

        assertThat(actualList, hasSize(1));
        assertThat(actualList.get(0), is(bo));
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