package br.com.tp.lncr.customer.datasource.mongo;

import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MongoCustomerMapperTest {

    private MongoCustomerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new MongoCustomerMapper();
    }

    @Test
    void shouldMapCustomerDtoToDocumentEntity() {
        CustomerDTO dto = new CustomerDTO(1, "12345678901", "João Silva", "joao@email.com");

        MongoCustomerDocument entity = mapper.customerDtoToDocument(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getDocumentNumber(), entity.getDocumentNumber());
        assertEquals(dto.getName(), entity.getName());
        assertEquals(dto.getEmail(), entity.getEmail());
    }

    @Test
    void shouldMapJpaEntityToCustomerDto() {
        MongoCustomerDocument entity = new MongoCustomerDocument(1, "12345678901", "João Silva", "joao@email.com");

        CustomerDTO dto = mapper.documentCustomerToDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getDocumentNumber(), dto.getDocumentNumber());
        assertEquals(entity.getName(), dto.getName());
        assertEquals(entity.getEmail(), dto.getEmail());
    }

    @Test
    void shouldReturnNullWhenCustomerDtoIsNull() {
        MongoCustomerDocument entity = mapper.customerDtoToDocument(null);

        assertNull(entity);
    }

    @Test
    void shouldReturnNullWhenJpaEntityIsNull() {
        CustomerDTO dto = mapper.documentCustomerToDTO(null);

        assertNull(dto);
    }

    @Test
    void shouldHandleNullValuesInCustomerDto() {
        CustomerDTO dto = new CustomerDTO(null, null, null, null);

        MongoCustomerDocument entity = mapper.customerDtoToDocument(dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getDocumentNumber());
        assertNull(entity.getName());
        assertNull(entity.getEmail());
    }

    @Test
    void shouldHandleNullValuesInJpaEntity() {
        MongoCustomerDocument entity = new MongoCustomerDocument(null, null, null, null);

        CustomerDTO dto = mapper.documentCustomerToDTO(entity);

        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getDocumentNumber());
        assertNull(dto.getName());
        assertNull(dto.getEmail());
    }

    @Test
    void shouldMapEmptyStringsFromDtoToEntity() {
        CustomerDTO dto = new CustomerDTO(1, "", "", "");

        MongoCustomerDocument entity = mapper.customerDtoToDocument(dto);

        assertNotNull(entity);
        assertEquals(1, entity.getId());
        assertEquals("", entity.getDocumentNumber());
        assertEquals("", entity.getName());
        assertEquals("", entity.getEmail());
    }

    @Test
    void shouldMapEmptyStringsFromEntityToDto() {
        MongoCustomerDocument entity = new MongoCustomerDocument(1, "", "", "");

        CustomerDTO dto = mapper.documentCustomerToDTO(entity);

        assertNotNull(dto);
        assertEquals(1, dto.getId());
        assertEquals("", dto.getDocumentNumber());
        assertEquals("", dto.getName());
        assertEquals("", dto.getEmail());
    }
}
