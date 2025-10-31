package br.com.tp.lncr.customer.apis;

import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.customer.configs.CustomerConfig;
import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.interfaces.customer.CustomerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerRestControllerImplTest {

    @Mock
    private CustomerController customerController;

    @Mock
    private CustomerConfig customerConfig;

    @InjectMocks
    private CustomerRestControllerImpl customerRestController;

    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerDTO = new CustomerDTO();
        customerDTO.setId(1);
        customerDTO.setName("João Silva");
        customerDTO.setDocumentNumber("12345678909");
        customerDTO.setEmail("joao@email.com");
    }

    @Test
    void deveRetornarCustomerCriadoComSucesso() {
        when(customerController.create(any(CustomerDTO.class))).thenReturn(customerDTO);
        when(customerConfig.getLocationPrefix()).thenReturn("/customers");

        ResponseEntity<ResponseModel<CustomerDTO>> response = customerRestController.createCustomer(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerDTO, response.getBody().getContent());
        assertTrue(Objects.requireNonNull(response.getHeaders().getLocation()).toString().contains("/customers"));
        verify(customerController).create(customerDTO);
    }

    @Test
    void deveRetornarTodosCustomersComLimite() {
        List<CustomerDTO> customers = List.of(customerDTO);
        when(customerController.getAll(10)).thenReturn(customers);

        ResponseEntity<ResponseListModel<CustomerDTO>> response = customerRestController.getAllCustomers(10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customers, response.getBody().getContent());
        verify(customerController).getAll(10);
    }

    @Test
    void deveRetornarTodosCustomersSemLimite() {
        List<CustomerDTO> customers = List.of(customerDTO);
        when(customerController.getAll(null)).thenReturn(customers);

        ResponseEntity<ResponseListModel<CustomerDTO>> response = customerRestController.getAllCustomers(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customers, response.getBody().getContent());
        verify(customerController).getAll(null);
    }

    @Test
    void deveRetornarCustomerPorId() {
        when(customerController.getById(1)).thenReturn(customerDTO);

        ResponseEntity<ResponseModel<CustomerDTO>> response = customerRestController.getCustomerById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerDTO, response.getBody().getContent());
        verify(customerController).getById(1);
    }

    @Test
    void deveRetornarCustomerPorDocumentNumber() {
        when(customerController.getByDocumentNumber("12345678909")).thenReturn(customerDTO);

        ResponseEntity<ResponseModel<CustomerDTO>> response = customerRestController.getCustomerByDocumentNumber("12345678909");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customerDTO, response.getBody().getContent());
        verify(customerController).getByDocumentNumber("12345678909");
    }

    @Test
    void deveRetornarCustomersPorListaDeIds() {
        List<Integer> ids = List.of(1, 2);
        List<CustomerDTO> customers = List.of(customerDTO);
        when(customerController.getByIdList(ids)).thenReturn(customers);

        ResponseEntity<ResponseListModel<CustomerDTO>> response = customerRestController.getCustomerByIdList(ids);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customers, response.getBody().getContent());
        verify(customerController).getByIdList(ids);
    }

    @Test
    void deveAtualizarCustomerParcialmente() {
        CustomerDTO updatedCustomer = new CustomerDTO();
        updatedCustomer.setId(1);
        updatedCustomer.setName("João Santos");
        when(customerController.partialUpdateById(1, customerDTO)).thenReturn(updatedCustomer);

        ResponseEntity<ResponseModel<CustomerDTO>> response = customerRestController.partialUpdateCustomer(customerDTO, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(updatedCustomer, response.getBody().getContent());
        verify(customerController).partialUpdateById(1, customerDTO);
    }

    @Test
    void deveDeletarCustomerComSucesso() {
        doNothing().when(customerController).delete(1);

        ResponseEntity<ResponseModel<CustomerDTO>> response = customerRestController.deleteCustomer(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNull(response.getBody().getContent());
        verify(customerController).delete(1);
    }
}
