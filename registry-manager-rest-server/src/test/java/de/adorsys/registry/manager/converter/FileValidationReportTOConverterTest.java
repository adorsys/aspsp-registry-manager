package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.FileValidationReportTO;
import de.adorsys.registry.manager.service.model.FileValidationReportBO;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class FileValidationReportTOConverterTest {

    private FileValidationReportTOConverter converter = Mappers.getMapper(FileValidationReportTOConverter.class);
    private FileValidationReportTO to = readYml(FileValidationReportTO.class, "csv-file-validation-report-to.yml");
    private FileValidationReportBO bo = readYml(FileValidationReportBO.class, "csv-file-validation-report-bo.yml");

    @Test
    public void toCsvFileValidationReportTO() {
        FileValidationReportTO actual = converter.toFileValidationReportTO(bo);
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