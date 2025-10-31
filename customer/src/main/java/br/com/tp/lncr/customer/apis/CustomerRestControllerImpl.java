package br.com.tp.lncr.customer.apis;

import br.com.tp.lncr.commons.model.ResponseListModel;
import br.com.tp.lncr.commons.model.ResponseModel;
import br.com.tp.lncr.commons.utils.ResponseEntityModelUtil;
import br.com.tp.lncr.customer.configs.CustomerConfig;
import br.com.tp.lncr.customer.datasource.mongo.MongoCustomerReposityImpl;
import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.interfaces.customer.CustomerController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/customers")
public class CustomerRestControllerImpl implements CustomerRestController {

    public final CustomerController customerController;
    public final MongoCustomerReposityImpl jpaCustomerRepository;
    public final CustomerConfig customerConfig;

    public CustomerRestControllerImpl(CustomerController customerController,
                                      MongoCustomerReposityImpl jpaCustomerRepository,
                                      CustomerConfig customerConfig) {
        this.customerController = customerController;
        this.jpaCustomerRepository = jpaCustomerRepository;
        this.customerConfig = customerConfig;
    }

    @Override
    @PostMapping
    public ResponseEntity<ResponseModel<CustomerDTO>> createCustomer(@RequestBody CustomerDTO customerDto) {
        customerDto = this.customerController.create(customerDto);
        return ResponseEntityModelUtil.created(customerDto, customerConfig.getLocationPrefix());
    }

    @Override
    @GetMapping
    public ResponseEntity<ResponseListModel<CustomerDTO>> getAllCustomers(@RequestParam(value = "limit", required = false) Integer limit) {
        List<CustomerDTO> listCustomerDTO = this.customerController.getAll(limit);
        return ResponseEntityModelUtil.listOK(listCustomerDTO);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<ResponseModel<CustomerDTO>> getCustomerById(@PathVariable("id") Integer id) {
        CustomerDTO customerDTO = this.customerController.getById(id);
        return ResponseEntityModelUtil.ok(customerDTO);
    }

    @Override
    @GetMapping("/documentNumber/{documentNumber}")
    public ResponseEntity<ResponseModel<CustomerDTO>> getCustomerByDocumentNumber(@PathVariable("documentNumber") String documentNumber) {
        CustomerDTO customerDTO = this.customerController.getByDocumentNumber(documentNumber);
        return ResponseEntityModelUtil.ok(customerDTO);
    }

    @Override
    @GetMapping("/listIds/{customerIdList}")
    public ResponseEntity<ResponseListModel<CustomerDTO>> getCustomerByIdList(@PathVariable(name="customerIdList") List<Integer> customerIdList) {
        List<CustomerDTO> customerDTOList = this.customerController.getByIdList(customerIdList);
        return ResponseEntityModelUtil.listOK(customerDTOList);
    }

    @Override
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseModel<CustomerDTO>> partialUpdateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable("id") Integer id) {
        customerDTO = this.customerController.partialUpdateById(id, customerDTO);
        return ResponseEntityModelUtil.ok(customerDTO);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseModel<CustomerDTO>> deleteCustomer(@PathVariable("id") Integer id) {
        this.customerController.delete(id);
        return ResponseEntityModelUtil.ok(null);
    }
}
