package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspRepositoryImplTest {
    private static final Long ID = 1234567L;

    @InjectMocks
    private AspspRepositoryImpl repository;

    @Mock
    private AspspJpaRepository jpaRepository;

    @Mock
    private AspspEntityConverter converter;
    private AspspEntity entity;
    private AspspPO po;

    @Before
    public void setUp() throws Exception {
        entity = mock(AspspEntity.class);
        po = mock(AspspPO.class);
    }

    @Test
    public void save() {

        when(converter.toAspspEntity(po)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(converter.toAspspPO(entity)).thenReturn(po);

        AspspPO actual = repository.save(po);

        assertThat(actual, is(po));

        verify(converter, times(1)).toAspspEntity(po);
        verify(jpaRepository, times(1)).save(entity);
        verify(converter, times(1)).toAspspPO(entity);
    }

    @Test
    public void delete() {

        doNothing().when(jpaRepository).deleteById(ID);

        repository.deleteById(ID);

        verify(jpaRepository, times(1)).deleteById(ID);
    }
}