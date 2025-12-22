package br.com.tp.lncr.customer.configs;

import br.com.tp.lncr.core.adapters.customer.CustomerControllerImpl;
import br.com.tp.lncr.core.adapters.customer.CustomerMapper;
import br.com.tp.lncr.core.interfaces.customer.CustomerController;
import br.com.tp.lncr.core.interfaces.customer.CustomerDatabase;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lncr.customer")
public class CustomerConfig {
    private String locationPrefix;
    public String getLocationPrefix() {
        return locationPrefix;
    }
    public void setLocationPrefix(String locationPrefix) {
        this.locationPrefix = locationPrefix;
    }

    @Bean
    public CustomerController customerController(CustomerDatabase customerDatabase) {
        return new CustomerControllerImpl(customerDatabase);
    }

    @Bean
    public CustomerMapper customerMapper(){
        return new CustomerMapper();
    }




}
