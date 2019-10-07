package de.adorsys.registry.manager.repository.converter;

import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
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

public class AspspEntityConverterTest {

    private AspspEntityConverter converter;
    private AspspEntity entity;
    private AspspPO po;

    @Before
    public void setUp() {
        converter = Mappers.getMapper(AspspEntityConverter.class);
        entity = readYml(AspspEntity.class, "aspsp-entity.yml");
        po = readYml(AspspPO.class, "aspsp-po.yml");
    }

    @Test
    public void toAspspPO() {
        AspspPO actual = converter.toAspspPO(entity);

        assertThat(actual, is(po));
    }

    @Test
    public void toAspspEntity() {
        AspspEntity actual = converter.toAspspEntity(po);

        assertThat(actual, is(entity));
    }

    @Test
    public void toAspspPOList() {
        List<AspspPO> actualList = converter.toAspspPOList(Collections.singletonList(entity));

        assertThat(actualList, hasSize(1));
        assertThat(actualList.get(0), is(po));
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