package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.AspspValidationReportTO;
import de.adorsys.registry.manager.service.model.AspspValidationReportBO;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class AspspValidationReportTOConverterTest {

    private AspspValidationReportTOConverter converter = Mappers.getMapper(AspspValidationReportTOConverter.class);
    private AspspValidationReportTO to = readYml(AspspValidationReportTO.class, "aspsp-validation-report-to.yml");
    private AspspValidationReportBO bo = readYml(AspspValidationReportBO.class, "aspsp-validation-report-bo.yml");

    @Test
    public void toAspspValidationReportTO() {
        AspspValidationReportTO actual = converter.toAspspValidationReportTO(bo);
        assertThat(actual, is(to));
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