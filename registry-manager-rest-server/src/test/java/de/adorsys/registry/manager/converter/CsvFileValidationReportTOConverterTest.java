package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.CsvFileValidationReportTO;
import de.adorsys.registry.manager.service.model.CsvFileValidationReportBO;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CsvFileValidationReportTOConverterTest {

    private CsvFileValidationReportTOConverter converter = Mappers.getMapper(CsvFileValidationReportTOConverter.class);
    private CsvFileValidationReportTO to = readYml(CsvFileValidationReportTO.class, "csv-file-validation-report-to.yml");
    private CsvFileValidationReportBO bo = readYml(CsvFileValidationReportBO.class, "csv-file-validation-report-bo.yml");

    @Test
    public void toCsvFileValidationReportTO() {
        CsvFileValidationReportTO actual = converter.toCsvFileValidationReportTO(bo);
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