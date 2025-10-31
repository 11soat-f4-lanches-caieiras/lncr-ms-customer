package br.com.tp.lncr.customer.datasource.mongo;

import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MongoCustomerReposityImplTest {

    @Mock
    private MongoCustomerRepository mongoCustomerRepository;
    @Mock
    private MongoCustomerMapper mongoCustomerMapper;
    @Mock
    private MongoOperations mongoOperations;

    private MongoCustomerReposityImpl repository;

    @BeforeEach
    void setUp() {
        repository = new MongoCustomerReposityImpl(mongoCustomerRepository, mongoCustomerMapper,mongoOperations);
    }

    @Test
    void shouldSaveCustomerSuccessfully() {
        CustomerDTO inputDto = new CustomerDTO(null, "12345678901", "João Silva", "joao@email.com");
        MongoCustomerDocument mappedEntity = new MongoCustomerDocument(null, "12345678901", "João Silva", "joao@email.com");
        MongoCustomerDocument savedEntity = new MongoCustomerDocument(1, "12345678901", "João Silva", "joao@email.com");
        CustomerDTO expectedDto = new CustomerDTO(1, "12345678901", "João Silva", "joao@email.com");

        when(mongoCustomerMapper.customerDtoToDocument(inputDto)).thenReturn(mappedEntity);
        when(mongoCustomerRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(mongoCustomerMapper.documentCustomerToDTO(savedEntity)).thenReturn(expectedDto);

        CustomerDTO result = repository.save(inputDto);

        assertEquals(expectedDto, result);
        verify(mongoCustomerMapper).customerDtoToDocument(inputDto);
        verify(mongoCustomerRepository).save(mappedEntity);
        verify(mongoCustomerMapper).documentCustomerToDTO(savedEntity);
    }

    @Test
    void shouldFindCustomerByIdWhenExists() {
        Integer customerId = 1;
        MongoCustomerDocument entity = new MongoCustomerDocument(1, "12345678901", "João Silva", "joao@email.com");
        CustomerDTO expectedDto = new CustomerDTO(1, "12345678901", "João Silva", "joao@email.com");

        when(mongoCustomerRepository.findById(customerId)).thenReturn(Optional.of(entity));
        when(mongoCustomerMapper.documentCustomerToDTO(entity)).thenReturn(expectedDto);

        Optional<CustomerDTO> result = repository.findById(customerId);

        assertTrue(result.isPresent());
        assertEquals(expectedDto, result.get());
        verify(mongoCustomerRepository).findById(customerId);
        verify(mongoCustomerMapper).documentCustomerToDTO(entity);
    }

    @Test
    void shouldReturnEmptyOptionalWhenCustomerNotFoundById() {
        Integer customerId = 999;

        when(mongoCustomerRepository.findById(customerId)).thenReturn(Optional.empty());

        Optional<CustomerDTO> result = repository.findById(customerId);

        assertFalse(result.isPresent());
        verify(mongoCustomerRepository).findById(customerId);
        verifyNoInteractions(mongoCustomerMapper);
    }

    @Test
    void shouldFindAllCustomersWithLimit() {
        Integer limit = 5;
        List<MongoCustomerDocument> entities = Arrays.asList(
                new MongoCustomerDocument(1, "12345678901", "João Silva", "joao@email.com"),
                new MongoCustomerDocument(2, "98765432100", "Maria Santos", "maria@email.com")
        );
        CustomerDTO dto1 = new CustomerDTO(1, "12345678901", "João Silva", "joao@email.com");
        CustomerDTO dto2 = new CustomerDTO(2, "98765432100", "Maria Santos", "maria@email.com");

        when(mongoCustomerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(entities));
        when(mongoCustomerMapper.documentCustomerToDTO(entities.get(0))).thenReturn(dto1);
        when(mongoCustomerMapper.documentCustomerToDTO(entities.get(1))).thenReturn(dto2);

        List<CustomerDTO> result = repository.findAll(limit);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(mongoCustomerRepository).findAll(Pageable.ofSize(limit));
    }

    @Test
    void shouldReturnEmptyListWhenNoCustomersFound() {
        Integer limit = 10;

        when(mongoCustomerRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(Collections.emptyList()));

        List<CustomerDTO> result = repository.findAll(limit);

        assertTrue(result.isEmpty());
        verify(mongoCustomerRepository).findAll(Pageable.ofSize(limit));
        verifyNoInteractions(mongoCustomerMapper);
    }

    @Test
    void shouldFindCustomerByDocumentNumberWhenExists() {
        String documentNumber = "12345678901";
        MongoCustomerDocument entity = new MongoCustomerDocument(1, documentNumber, "João Silva", "joao@email.com");
        CustomerDTO expectedDto = new CustomerDTO(1, documentNumber, "João Silva", "joao@email.com");

        when(mongoCustomerRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.of(entity));
        when(mongoCustomerMapper.documentCustomerToDTO(entity)).thenReturn(expectedDto);

        Optional<CustomerDTO> result = repository.findByDocumentNumber(documentNumber);

        assertTrue(result.isPresent());
        assertEquals(expectedDto, result.get());
        verify(mongoCustomerRepository).findByDocumentNumber(documentNumber);
        verify(mongoCustomerMapper).documentCustomerToDTO(entity);
    }

    @Test
    void shouldReturnEmptyOptionalWhenDocumentNumberNotFound() {
        String documentNumber = "99999999999";

        when(mongoCustomerRepository.findByDocumentNumber(documentNumber)).thenReturn(Optional.empty());

        Optional<CustomerDTO> result = repository.findByDocumentNumber(documentNumber);

        assertFalse(result.isPresent());
        verify(mongoCustomerRepository).findByDocumentNumber(documentNumber);
        verifyNoInteractions(mongoCustomerMapper);
    }

    @Test
    void shouldDeleteCustomerById() {
        Integer customerId = 1;

        repository.deleteById(customerId);

        verify(mongoCustomerRepository).deleteById(customerId);
    }

    @Test
    void shouldFindCustomersByIdList() {
        List<Integer> customerIds = Arrays.asList(1, 2);
        List<MongoCustomerDocument> entities = Arrays.asList(
                new MongoCustomerDocument(1, "12345678901", "João Silva", "joao@email.com"),
                new MongoCustomerDocument(2, "98765432100", "Maria Santos", "maria@email.com")
        );
        CustomerDTO dto1 = new CustomerDTO(1, "12345678901", "João Silva", "joao@email.com");
        CustomerDTO dto2 = new CustomerDTO(2, "98765432100", "Maria Santos", "maria@email.com");

        when(mongoCustomerRepository.findByIdIn(customerIds)).thenReturn(entities);
        when(mongoCustomerMapper.documentCustomerToDTO(entities.get(0))).thenReturn(dto1);
        when(mongoCustomerMapper.documentCustomerToDTO(entities.get(1))).thenReturn(dto2);

        List<CustomerDTO> result = repository.findByIdList(customerIds);

        assertEquals(2, result.size());
        assertEquals(dto1, result.get(0));
        assertEquals(dto2, result.get(1));
        verify(mongoCustomerRepository).findByIdIn(customerIds);
    }

    @Test
    void shouldReturnEmptyListWhenIdListReturnsNoResults() {
        List<Integer> customerIds = Arrays.asList(999, 998);

        when(mongoCustomerRepository.findByIdIn(customerIds)).thenReturn(Collections.emptyList());

        List<CustomerDTO> result = repository.findByIdList(customerIds);

        assertTrue(result.isEmpty());
        verify(mongoCustomerRepository).findByIdIn(customerIds);
        verifyNoInteractions(mongoCustomerMapper);
    }

    @Test
    void shouldReturnTrueWhenCustomerExistsByDocumentNumber() {
        String documentNumber = "12345678901";

        when(mongoCustomerRepository.existsByDocumentNumber(documentNumber)).thenReturn(true);

        boolean result = repository.existsByDocumentNumber(documentNumber);

        assertTrue(result);
        verify(mongoCustomerRepository).existsByDocumentNumber(documentNumber);
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExistByDocumentNumber() {
        String documentNumber = "99999999999";

        when(mongoCustomerRepository.existsByDocumentNumber(documentNumber)).thenReturn(false);

        boolean result = repository.existsByDocumentNumber(documentNumber);

        assertFalse(result);
        verify(mongoCustomerRepository).existsByDocumentNumber(documentNumber);
    }

    @Test
    void shouldReturnTrueWhenCustomerExistsByEmail() {
        String email = "joao@email.com";

        when(mongoCustomerRepository.existsByEmail(email)).thenReturn(true);

        boolean result = repository.existsByEmail(email);

        assertTrue(result);
        verify(mongoCustomerRepository).existsByEmail(email);
    }

    @Test
    void shouldReturnFalseWhenCustomerDoesNotExistByEmail() {
        String email = "inexistente@email.com";

        when(mongoCustomerRepository.existsByEmail(email)).thenReturn(false);

        boolean result = repository.existsByEmail(email);

        assertFalse(result);
        verify(mongoCustomerRepository).existsByEmail(email);
    }
}
