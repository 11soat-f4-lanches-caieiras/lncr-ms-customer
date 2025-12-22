# lncr-ms-customer

## Descrição

Microserviço responsável pelo gerenciamento de **Clientes** no sistema Lanches Caieiras. Este serviço implementa as funcionalidades relacionadas ao cadastro, atualização, consulta e gerenciamento de informações dos clientes, incluindo validação de CPF, e-mail e dados pessoais.

## Funcionalidades

### Endpoints Disponíveis (`/customers`)

| Método | Path | Descrição |
|--------|------|-----------|
| `POST` | `/customers` | Criar novo cliente |
| `GET` | `/customers` | Listar todos os clientes |
| `GET` | `/customers/{id}` | Buscar cliente por ID |
| `GET` | `/customers/list/{customerIdList}` | Buscar clientes por lista de IDs |
| `GET` | `/customers/documentNumber/{documentNumber}` | Buscar cliente por CPF |
| `PATCH` | `/customers/{id}` | Atualizar parcialmente cliente |
| `DELETE` | `/customers/{id}` | Deletar cliente |

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.4.5
- MongoDB (DocumentDB)
- Maven
- Cucumber (BDD)
- JUnit 5

## Sonar Quality Gate

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=11soat-f4-lanches-caieiras_lncr-ms-customer&metric=alert_status)](https://sonarcloud.io/project/overview?id=11soat-f4-lanches-caieiras_lncr-ms-customer)

Acesse o dashboard completo: [SonarCloud - lncr-ms-customer](https://sonarcloud.io/project/overview?id=11soat-f4-lanches-caieiras_lncr-ms-customer)

## Dependências

- **lncr-core** (versão 3.0) - Biblioteca com regras de negócio e entidades de domínio
- **lncr-commons** (versão 1.0) - Biblioteca comum compartilhada com configurações e utilitários

## Guia de Download e Execução

### Pré-requisitos

- **Java 21** instalado
- **Maven 3.8+** instalado
- **MongoDB** (ou AWS DocumentDB) em execução
- **Git** instalado

### Variáveis de Ambiente

Crie um arquivo `.env` ou configure as seguintes variáveis de ambiente:

```bash
# Configuração do Servidor
SERVER_PORT=8080

# MongoDB/DocumentDB
MONGODB_URI=mongodb://localhost:27017/lncr_customer

# URLs da Aplicação
LNCR_INTERNAL_URL=http://localhost:8080
LNCR_EXTERNAL_URL=http://localhost:8080
```

### Download e Instalação

```bash
# Clone o repositório
git clone https://github.com/11soat-f4-lanches-caieiras/lncr-ms-customer.git

# Entre no diretório do projeto
cd lncr-ms-customer/customer

# Configure o GitHub Packages (necessário para dependências lncr-core e lncr-commons)
# Crie o arquivo ~/.m2/settings.xml com suas credenciais do GitHub

# Compile o projeto
mvn clean install

# Execute a aplicação
mvn spring-boot:run
```

### Executando com Docker

```bash
# Build da imagem
docker build -t lncr-ms-customer:latest .

# Execute o container
docker run -p 8080:8080 \
  -e MONGODB_URI=mongodb://host.docker.internal:27017/lncr_customer \
  -e LNCR_INTERNAL_URL=http://localhost:8080 \
  -e LNCR_EXTERNAL_URL=http://localhost:8080 \
  lncr-ms-customer:latest
```

### Executando os Testes

```bash
# Executar todos os testes
mvn test

# Executar testes com cobertura
mvn test -Pcoverage

# Executar apenas testes BDD
mvn test -Dcucumber.filter.tags="@bdd"
```

### Verificando a Aplicação

Após iniciar a aplicação, acesse:

- **Health Check**: http://localhost:8080/actuator/health
- **API Base**: http://localhost:8080/customers
