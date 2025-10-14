package br.com.tp.lncr.customer.datasource.mongo;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@CompoundIndexes({
        @CompoundIndex(name = "name_email_idx", def = "{'documentNumber': 1, 'email': 1}")
})
public class MongoCustomerDocument {
    @Id
    Integer id;

    @Indexed(unique = true)
    String documentNumber;

    String name;

    @Indexed(unique = true)
    String email;

    public MongoCustomerDocument() {
    }

    public MongoCustomerDocument(Integer id, String documentNumber, String name, String email) {
        this.id = id;
        this.documentNumber = documentNumber;
        this.name = name;
        this.email = email;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
