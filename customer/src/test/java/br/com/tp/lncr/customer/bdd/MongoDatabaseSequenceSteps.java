package br.com.tp.lncr.customer.bdd;

import br.com.tp.lncr.customer.datasource.mongo.MongoDatabaseSequence;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class MongoDatabaseSequenceSteps {

    private String sequenceId;
    private Integer sequenceValue;
    private MongoDatabaseSequence databaseSequence;

    @Dado("que tenho um id de sequência {string}")
    public void queTenhoUmIdDeSequencia(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Dado("tenho um valor de sequência {int}")
    public void tenhoUmValorDeSequencia(Integer sequenceValue) {
        this.sequenceValue = sequenceValue;
    }

    @Quando("eu crio uma MongoDatabaseSequence")
    public void euCrioUmaMongoDatabaseSequence() throws Exception {
        databaseSequence = createMongoDatabaseSequence(sequenceId, sequenceValue);
    }

    @Então("a sequência deve ser criada com sucesso")
    public void aSequenciaDeveSerCriadaComSucesso() {
        assertNotNull(databaseSequence);
    }

    @Então("o id da sequência deve ser {string}")
    public void oIdDaSequenciaDeveSer(String expectedId) {
        assertEquals(expectedId, databaseSequence.getId());
    }

    @Então("o valor da sequência deve ser {int}")
    public void oValorDaSequenciaDeveSer(Integer expectedValue) {
        assertEquals(expectedValue, databaseSequence.getSeq());
    }

    @Dado("que tenho uma sequência existente com id {string}")
    public void queTenhoUmaSequenciaExistenteComId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    @Dado("o valor inicial é {int}")
    public void oValorInicialE(Integer initialValue) throws Exception {
        this.sequenceValue = initialValue;
        this.databaseSequence = createMongoDatabaseSequence(sequenceId, sequenceValue);
    }

    @Quando("eu atualizo o valor da sequência para {int}")
    public void euAtualizoOValorDaSequenciaPara(Integer newValue) {
        databaseSequence.setSeq(newValue);
    }

    @Então("o novo valor da sequência deve ser {int}")
    public void oNovoValorDaSequenciaDeveSer(Integer expectedValue) {
        assertEquals(expectedValue, databaseSequence.getSeq());
    }

    @Dado("que tenho uma MongoDatabaseSequence criada")
    public void queTenhoUmaMongoDatabaseSequenceCriada() throws Exception {
        this.databaseSequence = createMongoDatabaseSequence("test_seq", 1);
    }

    @Quando("eu verifico os campos da sequência")
    public void euVerificoOsCamposDaSequencia() {
        // Verificação já realizada no próximo step
    }

    @Então("todos os campos obrigatórios devem estar presentes")
    public void todosOsCamposObrigatoriosDevemEstarPresentes() {
        assertNotNull(databaseSequence.getId());
        assertNotNull(databaseSequence.getSeq());
    }

    // Helper method para criar instância usando reflection devido ao construtor privado
    private MongoDatabaseSequence createMongoDatabaseSequence(String id, Integer seq) throws Exception {
        MongoDatabaseSequence sequence = MongoDatabaseSequence.class.getDeclaredConstructor().newInstance();

        Field idField = MongoDatabaseSequence.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(sequence, id);

        Field seqField = MongoDatabaseSequence.class.getDeclaredField("seq");
        seqField.setAccessible(true);
        seqField.set(sequence, seq);

        return sequence;
    }
}

