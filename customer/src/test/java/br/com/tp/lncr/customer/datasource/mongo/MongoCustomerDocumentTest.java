package br.com.tp.lncr.customer.datasource.mongo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MongoCustomerDocumentTest {

    @Test
    void shouldCreateEntityWithAllParameters() {
        Integer id = 1;
        String documentNumber = "12345678901";
        String name = "João Silva";
        String email = "joao@email.com";

        MongoCustomerDocument entity = new MongoCustomerDocument(id, documentNumber, name, email);

        assertEquals(id, entity.getId());
        assertEquals(documentNumber, entity.getDocumentNumber());
        assertEquals(name, entity.getName());
        assertEquals(email, entity.getEmail());
    }

    @Test
    void shouldCreateEmptyEntity() {
        MongoCustomerDocument entity = new MongoCustomerDocument();

        assertNull(entity.getId());
        assertNull(entity.getDocumentNumber());
        assertNull(entity.getName());
        assertNull(entity.getEmail());
    }

    @Test
    void shouldSetAndGetId() {
        MongoCustomerDocument entity = new MongoCustomerDocument();
        Integer id = 123;

        entity.setId(id);

        assertEquals(id, entity.getId());
    }

    @Test
    void shouldSetAndGetDocumentNumber() {
        MongoCustomerDocument entity = new MongoCustomerDocument();
        String documentNumber = "98765432100";

        entity.setDocumentNumber(documentNumber);

        assertEquals(documentNumber, entity.getDocumentNumber());
    }

    @Test
    void shouldSetAndGetName() {
        MongoCustomerDocument entity = new MongoCustomerDocument();
        String name = "Maria Santos";

        entity.setName(name);

        assertEquals(name, entity.getName());
    }

    @Test
    void shouldSetAndGetEmail() {
        MongoCustomerDocument entity = new MongoCustomerDocument();
        String email = "maria@email.com";

        entity.setEmail(email);

        assertEquals(email, entity.getEmail());
    }

    @Test
    void shouldHandleNullValues() {
        MongoCustomerDocument entity = new MongoCustomerDocument(null, null, null, null);

        assertNull(entity.getId());
        assertNull(entity.getDocumentNumber());
        assertNull(entity.getName());
        assertNull(entity.getEmail());
    }

    @Test
    void shouldHandleEmptyStrings() {
        MongoCustomerDocument entity = new MongoCustomerDocument(1, "", "", "");

        assertEquals(1, entity.getId());
        assertEquals("", entity.getDocumentNumber());
        assertEquals("", entity.getName());
        assertEquals("", entity.getEmail());
    }
}
