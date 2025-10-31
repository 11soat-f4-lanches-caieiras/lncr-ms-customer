#language: pt

Funcionalidade: Sequência de Banco de Dados MongoDB
  Como um sistema de banco de dados MongoDB
  Eu preciso gerenciar sequências de documentos
  Para gerar identificadores únicos de forma incremental

  Cenário: Criar uma sequência de banco de dados
    Dado que tenho um id de sequência "customer_seq"
    E tenho um valor de sequência 1
    Quando eu crio uma MongoDatabaseSequence
    Então a sequência deve ser criada com sucesso
    E o id da sequência deve ser "customer_seq"
    E o valor da sequência deve ser 1

  Cenário: Atualizar o valor de uma sequência existente
    Dado que tenho uma sequência existente com id "order_seq"
    E o valor inicial é 5
    Quando eu atualizo o valor da sequência para 10
    Então o novo valor da sequência deve ser 10

  Cenário: Validar estrutura da MongoDatabaseSequence
    Dado que tenho uma MongoDatabaseSequence criada
    Quando eu verifico os campos da sequência
    Então todos os campos obrigatórios devem estar presentes

