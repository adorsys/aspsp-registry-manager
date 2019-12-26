package de.adorsys.registry.manager.service.converter;

import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.model.AspspCsvRecord;
import org.junit.Before;
import org.junit.Test;
import org.mapstruct.factory.Mappers;
import pro.javatar.commons.reader.YamlReader;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class AspspCsvRecordConverterTest {

    private AspspCsvRecordConverter converter;
    private AspspPO po;
    private AspspCsvRecord csvRecord;

    @Before
    public void setUp() throws Exception {
        converter = Mappers.getMapper(AspspCsvRecordConverter.class);
        po = readYml(AspspPO.class, "aspsp-po-for-csv.yml");
        csvRecord = readYml(AspspCsvRecord.class, "aspsp-csv-record.yml");
    }

    @Test
    public void toAspspPO() {
        AspspPO actual = converter.toAspspPO(csvRecord);

        assertThat(actual, is(po));
    }

    @Test
    public void toAspspPO_withNoId() {
        AspspCsvRecord withNoId = csvRecord;
        withNoId.setId(null);

        AspspPO actual = converter.toAspspPO(withNoId);

        assertNotNull(actual.getId());
    }

    @Test
    public void toAspspPOList() {
        List<AspspPO> actualList = converter.toAspspPOList(List.of(csvRecord));

        assertThat(actualList, hasSize(1));
        assertThat(actualList.get(0), is(po));
    }

    @Test
    public void toAspspCsvRecord() {
        AspspCsvRecord actual = converter.toAspspCsvRecord(po);

        assertThat(actual, is(csvRecord));
    }

    @Test
    public void toAspspCsvRecordList() {
        List<AspspCsvRecord> actualList = converter.toAspspCsvRecordList(List.of(po));

        assertThat(actualList, hasSize(1));
        assertThat(actualList.get(0), is(csvRecord));
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
