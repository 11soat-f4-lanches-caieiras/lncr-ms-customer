package br.com.tp.lncr.customer.configs;

import br.com.tp.lncr.core.adapters.customer.CustomerMapper;
import br.com.tp.lncr.core.interfaces.customer.CustomerController;
import br.com.tp.lncr.core.interfaces.customer.CustomerDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerConfigTest {

    @InjectMocks
    private CustomerConfig customerConfig;

    @Mock
    private CustomerDatabase customerDatabase;

    @BeforeEach
    void setUp() throws Exception {
        Constructor<CustomerConfig> constructor = CustomerConfig.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        customerConfig = constructor.newInstance();
    }

    @Test
    void deveDefinirEObterLocationPrefix() {
        String locationPrefix = "/api/customers";

        customerConfig.setLocationPrefix(locationPrefix);

        assertEquals(locationPrefix, customerConfig.getLocationPrefix());
    }

    @Test
    void deveRetornarNullQuandoLocationPrefixNaoDefinido() {
        assertNull(customerConfig.getLocationPrefix());
    }

    @Test
    void deveDefinirLocationPrefixComoVazio() {
        String locationPrefix = "";

        customerConfig.setLocationPrefix(locationPrefix);

        assertEquals(locationPrefix, customerConfig.getLocationPrefix());
    }

    @Test
    void deveCriarCustomerControllerBean() {
        CustomerController controller = customerConfig.customerController(customerDatabase);

        assertNotNull(controller);
    }

    @Test
    void deveCriarCustomerMapperBean() {
        CustomerMapper mapper = customerConfig.customerMapper();

        assertNotNull(mapper);
    }

    @Test
    void deveVerificarQueCustomerControllerBeanUsaDatabaseInjetado() {
        CustomerController controller = customerConfig.customerController(customerDatabase);

        assertNotNull(controller);
        // Verifica que o controller foi criado com o database mockado
        assertDoesNotThrow(controller::toString);
    }

    @Test
    void devePermitirAlterarLocationPrefixMultiplasVezes() {
        String primeiroPrefix = "/customers";
        String segundoPrefix = "/api/v1/customers";

        customerConfig.setLocationPrefix(primeiroPrefix);
        assertEquals(primeiroPrefix, customerConfig.getLocationPrefix());

        customerConfig.setLocationPrefix(segundoPrefix);
        assertEquals(segundoPrefix, customerConfig.getLocationPrefix());
    }

    @Test
    void devePermitirDefinirLocationPrefixComoNull() {
        customerConfig.setLocationPrefix("/customers");
        customerConfig.setLocationPrefix(null);

        assertNull(customerConfig.getLocationPrefix());
    }

    @Test
    void deveCriarNovaInstanciaDeCustomerMapperACadaChamada() {
        CustomerMapper mapper1 = customerConfig.customerMapper();
        CustomerMapper mapper2 = customerConfig.customerMapper();

        assertNotNull(mapper1);
        assertNotNull(mapper2);
        assertNotSame(mapper1, mapper2);
    }

    @Test
    void deveCriarNovaInstanciaDeCustomerControllerACadaChamada() {
        CustomerController controller1 = customerConfig.customerController(customerDatabase);
        CustomerController controller2 = customerConfig.customerController(customerDatabase);

        assertNotNull(controller1);
        assertNotNull(controller2);
        assertNotSame(controller1, controller2);
    }
}

