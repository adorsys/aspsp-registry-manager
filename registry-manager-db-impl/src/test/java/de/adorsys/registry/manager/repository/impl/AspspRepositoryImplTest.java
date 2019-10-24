package de.adorsys.registry.manager.repository.impl;

import de.adorsys.registry.manager.repository.AspspJpaRepository;
import de.adorsys.registry.manager.repository.converter.AspspEntityConverter;
import de.adorsys.registry.manager.repository.model.AspspEntity;
import de.adorsys.registry.manager.repository.model.AspspPO;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AspspRepositoryImplTest {
    private static final UUID ID = UUID.randomUUID();
    private static final int PAGE = 0;
    private static final int SIZE = 10;
    private static final String BANK_CODE = "111111";
    private static final String BIC = "AABBCC";

    @Spy
    @InjectMocks
    private AspspRepositoryImpl repository;

    @Mock
    private AspspJpaRepository jpaRepository;

    @Captor
    ArgumentCaptor<List<AspspPO>> captor;

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
    public void findAll() {
        List<AspspEntity> entities = List.of(entity);
        List<AspspPO> pos = List.of(po);

        when(jpaRepository.findAll()).thenReturn(entities);
        when(converter.toAspspPOList(entities)).thenReturn(pos);

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

        when(converter.toAspspEntity(po)).thenReturn(entity);
        when(jpaRepository.findAll(Example.of(entity, matcher), PageRequest.of(PAGE, SIZE))).thenReturn(new PageImpl<>(entities));
        when(converter.toAspspPOList(entities)).thenReturn(pos);

        List<AspspPO> result = repository.findByExample(po, PAGE, SIZE);

        assertNotNull(result);
        assertThat(result.size(), CoreMatchers.is(1));
        assertEquals(po, result.get(0));
    }

    @Test
    public void findByBankCode() {
        List<AspspEntity> entities = List.of(entity);
        List<AspspPO> pos = List.of(po);

        when(jpaRepository.findByBankCode(BANK_CODE, PageRequest.of(PAGE, SIZE))).thenReturn(entities);
        when(converter.toAspspPOList(entities)).thenReturn(pos);

        List<AspspPO> result = repository.findByBankCode(BANK_CODE, PAGE, SIZE);

        assertNotNull(result);
        assertThat(result.size(), CoreMatchers.is(1));
        assertEquals(po, result.get(0));
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

    @Test
    public void deleteAll() {
        List<AspspPO> target = Arrays.asList(po, po);

        when(jpaRepository.findOneByBicAndBankCode(any(), any())).thenReturn(any());

        repository.deleteAll(target);

        verify(repository, times(1)).deleteAll(captor.capture());
        verify(jpaRepository, times(2)).findOneByBicAndBankCode(any(), any());

        assertEquals(target.size(), captor.getValue().size());
        assertThat(target.get(0), is(captor.getValue().get(0)));
    }
}
