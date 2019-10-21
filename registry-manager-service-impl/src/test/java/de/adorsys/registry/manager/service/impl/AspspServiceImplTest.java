package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.exception.IbanException;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.iban4j.Iban;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspServiceImplTest {
    private static final UUID ID = UUID.randomUUID();
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String WRONG_IBAN = "wrong iban";
    private static final String CORRECT_IBAN = "DE89370400440532013000";

    @InjectMocks
    private AspspServiceImpl aspspService;

    @Mock
    private AspspRepository repository;

    @Mock
    private AspspBOConverter converter;
    private AspspPO po;
    private AspspBO bo;

    @Before
    public void setUp() throws Exception {
        po = mock(AspspPO.class);
        bo = mock(AspspBO.class);
    }

    @Test
    public void getByAspsp() {
        List<AspspPO> pos = List.of(po);
        List<AspspBO> bos = List.of(bo);

        when(converter.toAspspPO(bo)).thenReturn(po);
        when(repository.findByExample(po, PAGE, SIZE)).thenReturn(pos);
        when(converter.toAspspBOList(pos)).thenReturn(bos);

        List<AspspBO> result = aspspService.getByAspsp(bo, PAGE, SIZE);

        assertNotNull(result);
        assertThat(result.size(), is(1));
        assertEquals(bo, result.get(0));
    }

    @Test(expected = IbanException.class)
    public void getByIban_failure_wrongIbanFormat() {
        aspspService.getByIban(WRONG_IBAN, PAGE, SIZE);
    }

    @Test
    public void getByIban() {
        List<AspspPO> pos = List.of(po);
        List<AspspBO> bos = List.of(bo);

        when(repository.findByBankCode(Iban.valueOf(CORRECT_IBAN).getBankCode(), PAGE, SIZE)).thenReturn(pos);
        when(converter.toAspspBOList(pos)).thenReturn(bos);

        List<AspspBO> result = aspspService.getByIban(CORRECT_IBAN, PAGE, SIZE);

        assertNotNull(result);
        assertThat(result.size(), is(1));
        assertEquals(bo, result.get(0));
    }

    @Test
    public void save() {
        when(converter.toAspspPO(bo)).thenReturn(po);
        when(repository.save(po)).thenReturn(po);
        when(converter.toAspspBO(po)).thenReturn(bo);

        AspspBO actual = aspspService.save(bo);

        assertThat(actual, is(bo));

        verify(converter, times(1)).toAspspPO(bo);
        verify(repository, times(1)).save(po);
        verify(converter, times(1)).toAspspBO(po);
    }

    @Test
    public void saveAll() {
        List<AspspPO> pos = List.of(po);
        List<AspspBO> bos = List.of(bo);

        doNothing().when(repository).saveAll(pos);
        when(converter.toAspspPOList(bos)).thenReturn(pos);

        aspspService.saveAll(List.of(bo));

        verify(converter, times(1)).toAspspPOList(bos);
        verify(repository, times(1)).saveAll(pos);
    }

    @Test
    public void delete() {
        doNothing().when(repository).deleteById(ID);

        aspspService.deleteById(ID);

        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    public void deleteAll() {
        doNothing().when(repository).deleteAll();

        aspspService.deleteAll();

        verify(repository, times(1)).deleteAll();
    }
}
