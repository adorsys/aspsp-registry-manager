package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.CsvFileImportValidationReportTO;
import de.adorsys.registry.manager.service.model.CsvFileImportValidationReportBO;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CsvFileImportValidationReportTOConverterTest {

    private CsvFileImportValidationReportTOConverter converter = Mappers.getMapper(CsvFileImportValidationReportTOConverter.class);
    private CsvFileImportValidationReportTO to = readYml(CsvFileImportValidationReportTO.class, "csv-file-import-validation-report-to.yml");
    private CsvFileImportValidationReportBO bo = readYml(CsvFileImportValidationReportBO.class, "csv-file-import-validation-report-bo.yml");

    @Test
    public void toCsvFileImportValidationReportTO() {
        CsvFileImportValidationReportTO actual = converter.toCsvFileImportValidationReportTO(bo);
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