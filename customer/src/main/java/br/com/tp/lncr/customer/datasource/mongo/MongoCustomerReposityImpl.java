package br.com.tp.lncr.customer.datasource.mongo;

import br.com.tp.lncr.core.dtos.customer.CustomerDTO;
import br.com.tp.lncr.core.interfaces.customer.CustomerDatabase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoCustomerReposityImpl implements CustomerDatabase {

    public final MongoCustomerRepository mongoCustomerRepository;
    public final MongoCustomerMapper mongoCustomerMapper;
    public final MongoOperations mongoOperations;

    public MongoCustomerReposityImpl(MongoCustomerRepository mongoCustomerRepository, MongoCustomerMapper mongoCustomerMapper, MongoOperations mongoOperations) {
        this.mongoCustomerRepository = mongoCustomerRepository;
        this.mongoCustomerMapper = mongoCustomerMapper;
        this.mongoOperations = mongoOperations;
    }

    @Override
    public CustomerDTO save(CustomerDTO customerDto) {
        customerDto.setId(customerDto.getId() == null ? generateCustomerSequence() : customerDto.getId());
        MongoCustomerDocument mongoCustomerDocument = this.mongoCustomerRepository.save(mongoCustomerMapper.customerDtoToDocument(customerDto));
        return mongoCustomerMapper.documentCustomerToDTO(mongoCustomerDocument);
    }

    @Override
    public Optional<CustomerDTO> findById(Integer id) {
        return this.mongoCustomerRepository.findById(id)
                .map(mongoCustomerMapper::documentCustomerToDTO);
    }

    @Override
    public List<CustomerDTO> findAll(Integer _limit) {
        return mongoCustomerRepository.findAll(Pageable.ofSize(_limit))
                .stream()
                .map(mongoCustomerMapper::documentCustomerToDTO)
                .toList();
    }

    @Override
    public Optional<CustomerDTO> findByDocumentNumber(String documentNumber) {
        return this.mongoCustomerRepository.findByDocumentNumber(documentNumber)
                .map(mongoCustomerMapper::documentCustomerToDTO);
    }

    @Override
    public void deleteById(Integer id) {
        this.mongoCustomerRepository.deleteById(id);
    }

    @Override
    public List<CustomerDTO> findByIdList(List<Integer> customerIdList) {
        List<MongoCustomerDocument> jpaCustomerList = this.mongoCustomerRepository.findByIdIn(customerIdList);
        return jpaCustomerList.stream().map(mongoCustomerMapper::documentCustomerToDTO).toList();

    }

    @Override
    public boolean existsByDocumentNumber(String documentNumber) {
        return this.mongoCustomerRepository.existsByDocumentNumber(documentNumber);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.mongoCustomerRepository.existsByEmail(email);
    }


    private Integer generateCustomerSequence() {
        MongoDatabaseSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("id").is("customer_sequence")),
                new Update().inc("seq", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                MongoDatabaseSequence.class);
        return counter != null ? counter.getSeq() : 1;
    }
}
