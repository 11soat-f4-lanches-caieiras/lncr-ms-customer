package br.com.tp.lncr.customer.datasource.mongo;

import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class MongoCustomerMapper {

    public MongoCustomerDocument customerDtoToDocument(CustomerDTO customerDTO) {
        if (customerDTO == null) {
            return null;
        }
        return new MongoCustomerDocument(
                customerDTO.getId(),
                customerDTO.getDocumentNumber(),
                customerDTO.getName(),
                customerDTO.getEmail());

    }

    public CustomerDTO documentCustomerToDTO(MongoCustomerDocument mongoCustomerDocument) {
        if (mongoCustomerDocument == null) {
            return null;
        }
        return new CustomerDTO(
                mongoCustomerDocument.getId(),
                mongoCustomerDocument.getDocumentNumber(),
                mongoCustomerDocument.getName(),
                mongoCustomerDocument.getEmail());
    }
}












