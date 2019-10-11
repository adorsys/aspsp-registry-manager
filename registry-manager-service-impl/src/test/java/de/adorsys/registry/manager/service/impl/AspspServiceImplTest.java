package de.adorsys.registry.manager.service.impl;

import de.adorsys.registry.manager.repository.AspspRepository;
import de.adorsys.registry.manager.repository.model.AspspPO;
import de.adorsys.registry.manager.service.converter.AspspBOConverter;
import de.adorsys.registry.manager.service.model.AspspBO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspServiceImplTest {
    private static final UUID ID = UUID.randomUUID();

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
        List<AspspPO> pos = Collections.singletonList(po);
        List<AspspBO> bos = Collections.singletonList(bo);

        doNothing().when(repository).saveAll(pos);
        when(converter.toAspspPOList(bos)).thenReturn(pos);

        aspspService.saveAll(Collections.singletonList(bo));

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
