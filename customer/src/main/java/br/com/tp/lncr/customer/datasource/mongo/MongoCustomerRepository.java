package br.com.tp.lncr.customer.datasource.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface MongoCustomerRepository extends MongoRepository<MongoCustomerDocument, Integer> {

    Optional<MongoCustomerDocument> findByDocumentNumber(String documentNumber);

    Boolean existsByDocumentNumber(String documentNumber);

    Boolean existsByEmail(String email);

    Optional<MongoCustomerDocument> findByIdIn(Integer customerId);

    List<MongoCustomerDocument> findByIdIn(List<Integer> customerIdList);

    Optional<MongoCustomerDocument> findByDocumentNumberAndEmail(String documentNumber, String email);



}
