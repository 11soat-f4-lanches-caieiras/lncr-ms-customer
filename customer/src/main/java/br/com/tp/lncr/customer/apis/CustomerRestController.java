package br.com.tp.lncr.customer.apis;

import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CustomerRestController {

    ResponseEntity<ResponseModel<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDto);

    ResponseEntity<ResponseListModel<CustomerDTO>> getAllCustomers(Integer limit);

    ResponseEntity<ResponseModel<CustomerDTO>> getCustomerById(@PathVariable Integer id);

    ResponseEntity<ResponseListModel<CustomerDTO>> getCustomerByIdList(@PathVariable List<Integer> customerIdList);

    ResponseEntity<ResponseModel<CustomerDTO>> getCustomerByDocumentNumber(@PathVariable String documentNumber);

    ResponseEntity<ResponseModel<CustomerDTO>> partialUpdateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable Integer id);

    ResponseEntity<ResponseModel<CustomerDTO>> deleteCustomer(@PathVariable Integer id);
}
