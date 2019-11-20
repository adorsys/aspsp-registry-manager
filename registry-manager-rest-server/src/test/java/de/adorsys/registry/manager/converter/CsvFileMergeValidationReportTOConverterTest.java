package de.adorsys.registry.manager.converter;

import de.adorsys.registry.manager.model.CsvFileMergeValidationReportTO;
import de.adorsys.registry.manager.service.model.CsvFileMergeValidationReportBO;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class CsvFileMergeValidationReportTOConverterTest {

    private CsvFileMergeValidationReportTOConverter converter = Mappers.getMapper(CsvFileMergeValidationReportTOConverter.class);
    private CsvFileMergeValidationReportTO to = readYml(CsvFileMergeValidationReportTO.class, "csv-file-merge-validation-report-to.yml");
    private CsvFileMergeValidationReportBO bo = readYml(CsvFileMergeValidationReportBO.class, "csv-file-merge-validation-report-bo.yml");

    @Test
    public void toCsvFileMergeValidationReportTO() {
        CsvFileMergeValidationReportTO actual = converter.toCsvFileMergeValidationReportTO(bo);
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