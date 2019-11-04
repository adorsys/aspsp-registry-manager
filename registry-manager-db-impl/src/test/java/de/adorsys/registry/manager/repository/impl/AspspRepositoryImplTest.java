package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverterImpl;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.util.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspRepositoryImplTest {
    private static final UUID ID = UUID.randomUUID();
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String BANK_CODE = "111111";

    @InjectMocks
    private AspspRepositoryImpl repository;

    @Mock
    private AspspJpaRepository jpaRepository;

    @Captor
    ArgumentCaptor<List<AspspEntity>> captor;

    @Spy
    private AspspEntityConverter converter = new AspspEntityConverterImpl();
    private AspspEntity entity;
    private AspspPO po;

    @Before
    public void setUp() throws Exception {
        entity = mock(AspspEntity.class);
        po = mock(AspspPO.class);
    }

    @Test
    public void findAll() {
        List<AspspEntity> entities = List.of(entity);
        List<AspspPO> pos = List.of(po);

        when(jpaRepository.findAll()).thenReturn(entities);
        when(converter.toAspspPOList(any())).thenReturn(pos);

        List<AspspPO> result = repository.findAll();

        assertNotNull(result);
        assertThat(result.size(), CoreMatchers.is(1));
        assertEquals(po, result.get(0));
    }

    @Test
    public void findByExample() {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                                         .withStringMatcher(ExampleMatcher.StringMatcher.STARTING)
                                         .withIgnoreCase()
                                         .withIgnoreNullValues();

        List<AspspEntity> entities = List.of(entity);
        List<AspspPO> pos = List.of(po);

        when(converter.toAspspEntity(any())).thenReturn(entity);
        when(jpaRepository.findAll(Example.of(entity, matcher), PageRequest.of(PAGE, SIZE))).thenReturn(new PageImpl<>(entities));
        when(converter.toAspspPOList(any())).thenReturn(pos);

        Page<AspspPO> result = repository.findByExample(po, PageRequest.of(PAGE, SIZE));

        assertNotNull(result);
        assertThat(result.getContent().size(), CoreMatchers.is(1));
        assertEquals(po, result.getContent().get(0));
    }

    @Test
    public void findByBankCode() {
        List<AspspEntity> entities = List.of(entity);
        List<AspspPO> pos = List.of(po);

        when(jpaRepository.findByBankCode(BANK_CODE, PageRequest.of(PAGE, SIZE))).thenReturn(new PageImpl<>(entities));
        when(converter.toAspspPOList(any())).thenReturn(pos);

        Page<AspspPO> result = repository.findByBankCode(BANK_CODE, PageRequest.of(PAGE, SIZE));

        assertNotNull(result);
        assertThat(result.getContent().size(), CoreMatchers.is(1));
        assertEquals(po, result.getContent().get(0));
    }

    @Test
    public void save() {

        when(converter.toAspspEntity(any())).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);
        when(converter.toAspspPO(any())).thenReturn(po);

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

    @Test
    public void delete_list() {
        List<AspspEntity> target = Arrays.asList(entity, entity);

        doNothing().when(jpaRepository).deleteAll(any());
        when(converter.toAspspEntityList(any())).thenReturn(target);

        repository.delete(Collections.singletonList(po));

        verify(converter, times(1)).toAspspEntityList(any());
        verify(jpaRepository, times(1)).deleteAll(captor.capture());

        assertEquals(target.size(), captor.getValue().size());
        assertThat(target.get(0), is(captor.getValue().get(0)));
        assertThat(target.get(1), is(captor.getValue().get(1)));
    }
}
