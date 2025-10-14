[![Construir e Upload da Aplicação lncr-app](https://github.com/11soat-f3-lanches-caieiras/lncr-app/actions/workflows/lncr-app-build-deploy.yaml/badge.svg?branch=develop&event=repository_dispatch)](https://github.com/11soat-f3-lanches-caieiras/lncr-app/actions/workflows/lncr-app-build-deploy.yaml)
# Lanches Caieiras

## Índice

## Índice

- [Descrição](#descrição)
- [Requisitos](#requisitos)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Arquitetura do Projeto](#arquitetura-do-projeto)
- 
    - [1. Arquitetura de Infraestrutura em Kubernetes](#1-arquitetura-de-infraestrutura-kubernetes)
    - [2. Arquitetura da Aplicação - Clean Architecture](#2-arquitetura-da-aplicação---clean-architecture)
    - [3. Arquitetura Funcional](#3-arquitetura-funcional)
        - [3.1 Cadastro de Clientes](#31-cadastro-de-clientes)
        - [3.2 Cadastro de Items de Alimentação](#32---cadastro-de-items-de-alimentação)
        - [3.3 Novo Pedido - Checkout](#33---novo-pedido---checkout)
        - [3.4 Pagamento](#34---pagamento)
        - [3.5 Pedido Recebido - Pagamento Confirmado](#35---pedido-recebido---pagamento-confirmado)
        - [3.6 Atualização Preparo](#36---atualização-preapro)
        - [3.7 Cancelamento Pedido](#37---cancelamento-pedido)
        - [3.8 Cancelamento Pagamento](#38---cancelamento-pagamento)
        - [3.9 Acompanhar Pedidos](#39---acompanhar-pedidos)
        - [3.10 Notificações](#310---notificações)
- [Contrato da API](#contrato-da-api)
- [Testes via Postman](#testes-via-postman)


## Descrição

Sistema para gerenciamento de pedidos de uma lanchonete, desenvolvido no Tech Challange - Fase 3 da pós-graduação FIAP 11SOAT para ambiente Cloud AWS.

Este projeto tem como objetivo simular o funcionamento de uma lanchonete, permitindo o cadastro clientes, items de alimentação, pedidos de clientes, pagamentos e notificações 
Foram aplicados os conceintos de Clean Archtecture e infraesturura em Kubernetes, utilizando Docker para containerização e Postgres como banco de dados.

Participantes: Gustavo Silva (361477) e Tito Parizotto (361184) 

# Estrutura do Projeto
## Arquitetura do Projeto
### 1. Arquitetura de Infraestrutura Kubernetes

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                                CLUSTER KUBERNETES                                   │
├─────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                     │
│                            ┌──────────────────────┐                                 │
│                            │  AWS LOAD BALANCER   │                                 │
│                            │    (Service)         │                                 │
│                            │                      │                                 │
│                            └──────────┬───────────┘                                 │
│                                       │                                             │
│                                       ▼                                             │
│  ┌──────────────────────────────────────────────────────────────────────────────┐   │
│  │                        LNCR-APP DEPLOYMENT                                   │   │
│  │  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐               │   │
│  │  │   APP POD 1     │  │   APP POD 2     │  │   APP POD N     │               │   │
│  │  │ ┌─────────────┐ │  │ ┌─────────────┐ │  │ ┌─────────────┐ │  ◄────────────┤   │
│  │  │ │Java Spring  │ │  │ │Java Spring  │ │  │ │Java Spring  │ │    HPA        │   │
│  │  │ │Boot App     │ │  │ │Boot App     │ │  │ │Boot App     │ │ (2-10 pods)   │   │
│  │  │ │Port: 8080   │ │  │ │Port: 8080   │ │  │ │Port: 8080   │ │               │   │
│  │  │ └─────────────┘ │  │ └─────────────┘ │  │ └─────────────┘ │               │   │
│  │  └─────────────────┘  └─────────────────┘  └─────────────────┘               │   │
│  └──────────────────────────────────────────────────────────────────────────────┘   │
│                                │                                                    │
│                                ▼                                                    │
│  ┌──────────────────────────────────────────────────────────────────────────────┐   │
│  │                         PERSISTENT VOLUMES                                   │   │
│  │                         ┌────────────────────┐                               │   │
│  │                         │  IMAGES STORAGE    │                               │   │
│  │                         │   (10Gi Volume)    │                               │   │
│  │                         │   /app/images      │                               │   │
│  │                         └────────────────────┘                               │   │
│  └──────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                     │
│  ┌──────────────────────────────────────────────────────────────────────────────┐   │
│  │                         CONFIGURATION                                        │   │
│  │  ┌────────────────────┐              ┌────────────────────┐                  │   │
│  │  │    CONFIGMAP       │              │      SECRETS       │                  │   │
│  │  │                    │              │                    │                  │   │
│  │  │ • Base URLs        │              │ • DB Credentials   │                  │   │
│  │  │ • Postgres Config  │              │ • MercadoPago Keys │                  │   │
│  │  │ • External APIs    │              │ • Sensitive Data   │                  │   │
│  │  └────────────────────┘              └────────────────────┘                  │   │
│  └──────────────────────────────────────────────────────────────────────────────┘   │
│                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────┘

Componentes principais:
• LoadBalancer Service: Expõe a aplicação externamente na porta 8080
• HPA (Horizontal Pod Autoscaler): Escala automaticamente de 2 a 10 pods baseado em CPU/Memória
• App Deployment: Pods da aplicação Java Spring Boot com health checks
• DB Service (ClusterIP): Comunicação interna com PostgreSQL
• DB Deployment: PostgreSQL com estratégia Recreate (single replica)
• Persistent Volumes: Armazenamento para banco de dados e imagens
• ConfigMap/Secrets: Configurações e credenciais da aplicação
```

### 2. Arquitetura da Aplicação - Clean Architecture

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│ ┌─────────────────────────────────────────────────────────────────────────────────┐ │
│ │                                  SECURITY LAYER                                 │ │
│ │                                AWS LAMBDA FUNCTION                              │ │
│ │                             lncr-prd-custom-authorizer                          │ │
│ └─────────────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                              LANCHES CAIEIRAS - CLEAN ARCHITECTURE                  │
├─────────────────────────────────────────────────────────────────────────────────────┤
│ ┌─────────────────────────────────────────────────────────────────────────────────┐ │
│ │                              MÓDULO APP (Infraestrutura)                        │ │
│ │ ┌─────────────────────────────────────────────────────────────────────────────┐ │ │
│ │ │                           CAMADA DE APRESENTAÇÃO                            │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │  REST API        │  │    WEBHOOKS      │  │    HANDLERS      │           │ │ │
│ │ │  │  Controllers     │  │                  │  │   (Inbound)      │           │ │ │
│ │ │  │ • Customer       │  │ • MercadoPago    │  │ • Customer       │           │ │ │
│ │ │  │ • FoodItem       │  │   Webhook        │  │ • FoodItem       │           │ │ │
│ │ │  │ • CustomerOrder  │  │                  │  │ • CustomerOrder  │           │ │ │
│ │ │  │ • KitchenOrder   │  │                  │  │ • KitchenOrder   │           │ │ │
│ │ │  │ • Payment        │  │                  │  │ • Payment        │           │ │ │
│ │ │  │ • Notification   │  │                  │  │ • Notification   │           │ │ │
│ │ │  │ • OauthToken     │  │                  │  │ • OauthToken     │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ └─────────────────────────────────────────────────────────────────────────────┘ │ │
│ │                                    │                                            │ │
│ │ ┌─────────────────────────────────────────────────────────────────────────────┐ │ │
│ │ │                          CAMADA DE INTEGRAÇÃO                               │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │   INTEGRAÇÕES    │  │  DATASOURCES     │  │    STORAGE       │           │ │ │
│ │ │  │                  │  │                  │  │                  │           │ │ │
│ │ │  │ • MercadoPago    │  │ • PostgreSQL     │  │ • File System    │           │ │ │
│ │ │  │   API Client     │  │   Repositories   │  │   (Images)       │           │ │ │
│ │ │  │ • External APIs  │  │ • JPA Entities   │  │                  │           │ │ │
│ │ │  │                  │  │ • Database       │  │                  │           │ │ │
│ │ │  │                  │  │   Config         │  │                  │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ └─────────────────────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────────────────┘ │
│                                    │                                                │
│                              INTERFACE BOUNDARY                                     │
│                                    │                                                │
│ ┌─────────────────────────────────────────────────────────────────────────────────┐ │
│ │                            MÓDULO CORE (Regras de Negócio)                      │ │
│ │                       Github Package -br.com.tp.lncr.core 2.0                   │ │
│ │ ┌─────────────────────────────────────────────────────────────────────────────┐ │ │
│ │ │                              ADAPTERS LAYER                                 │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │   CONTROLLERS    │  │    GATEWAYS      │  │   PRESENTERS     │           │ │ │
│ │ │  │  (Input Ports)   │  │ (Output Ports)   │  │  (Output Ports)  │           │ │ │
│ │ │  │                  │  │                  │  │                  │           │ │ │
│ │ │  │ • Customer       │  │ • Customer       │  │ • Customer       │           │ │ │
│ │ │  │ • FoodItem       │  │ • FoodItem       │  │ • FoodItem       │           │ │ │
│ │ │  │ • CustomerOrder  │  │ • CustomerOrder  │  │ • CustomerOrder  │           │ │ │
│ │ │  │ • KitchenOrder   │  │ • KitchenOrder   │  │ • KitchenOrder   │           │ │ │
│ │ │  │ • Payment        │  │ • Payment        │  │ • Payment        │           │ │ │
│ │ │  │ • Notification   │  │ • Notification   │  │ • Notification   │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ └─────────────────────────────────────────────────────────────────────────────┘ │ │
│ │                                    │                                            │ │
│ │ ┌─────────────────────────────────────────────────────────────────────────────┐ │ │
│ │ │                           APPLICATION LAYER                                 │ │ │
│ │ │                            (Use Cases)                                      │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │    CUSTOMER      │  │    FOOD ITEM     │  │ CUSTOMER ORDER   │           │ │ │
│ │ │  │   Use Cases      │  │   Use Cases      │  │   Use Cases      │           │ │ │
│ │ │  │                  │  │                  │  │                  │           │ │ │
│ │ │  │ • Create         │  │ • Create         │  │ • Create         │           │ │ │
│ │ │  │ • Get            │  │ • Get            │  │ • Get            │           │ │ │
│ │ │  │ • Update         │  │ • Update         │  │ • Update         │           │ │ │
│ │ │  │ • Delete         │  │ • Delete         │  │ • Delete         │           │ │ │
│ │ │  │                  │  │ • Image Upload   │  │                  │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │ KITCHEN ORDER    │  │    PAYMENT       │  │  NOTIFICATION    │           │ │ │
│ │ │  │   Use Cases      │  │   Use Cases      │  │   Use Cases      │           │ │ │
│ │ │  │                  │  │                  │  │                  │           │ │ │
│ │ │  │ • Create         │  │ • Create QR      │  │ • Create         │           │ │ │
│ │ │  │ • Get            │  │ • Get Status     │  │ • Get            │           │ │ │
│ │ │  │ • Update Status  │  │ • Update Status  │  │                  │           │ │ │
│ │ │  │                  │  │ • MercadoPago    │  │                  │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ └─────────────────────────────────────────────────────────────────────────────┘ │ │
│ │                                    │                                            │ │
│ │ ┌─────────────────────────────────────────────────────────────────────────────┐ │ │
│ │ │                             DOMAIN LAYER                                    │ │ │
│ │ │                       (Entidades de Negócio)                                │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │    CUSTOMER      │  │    FOOD ITEM     │  │ CUSTOMER ORDER   │           │ │ │
│ │ │  │                  │  │                  │  │                  │           │ │ │
│ │ │  │ • Customer       │  │ • FoodItem       │  │ • CustomerOrder  │           │ │ │
│ │ │  │ • CustomerCPF    │  │ • FoodItemImage  │  │ • OrderFoodItem  │           │ │ │
│ │ │  │ • CustomerEmail  │  │                  │  │ • OrderCustomer  │           │ │ │
│ │ │  │ • DocumentNumber │  │                  │  │                  │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ │                                                                             │ │ │
│ │ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐           │ │ │
│ │ │  │ KITCHEN ORDER    │  │    PAYMENT       │  │  NOTIFICATION    │           │ │ │
│ │ │  │                  │  │                  │  │                  │           │ │ │
│ │ │  │ • KitchenOrder   │  │ • Payment        │  │ • Notification   │           │ │ │
│ │ │  │ • OrderFoodItem  │  │ • PaymentQR      │  │                  │           │ │ │
│ │ │  │                  │  │   (MercadoPago)  │  │                  │           │ │ │
│ │ │  └──────────────────┘  └──────────────────┘  └──────────────────┘           │ │ │
│ │ └─────────────────────────────────────────────────────────────────────────────┘ │ │
│ └─────────────────────────────────────────────────────────────────────────────────┘ │
│ ┌─────────────────────────────────────────────────────────────────────────────────┐ │
│ │                              INFRASTRUCTURE                                     │ │
│ │                                                                                 │ │
│ │  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐               │ │
│ │  │   POSTGRESQL     │  │  FILE SYSTEM     │  │  MERCADOPAGO     │               │ │
│ │  │    DATABASE      │  │    STORAGE       │  │      API         │               │ │
│ │  │                  │  │                  │  │(Sistema Externo) │               │ │
│ │  │ • Tabelas        │  │ • /app/images    │  │ • OAuth Token    │               │ │
│ │  │ • Relacionamentos│  │ • Upload/Download│  │ • QR Code        │               │ │
│ │  │ • Transações     │  │ • CRUD Files     │  │ • Webhooks       │               │ │
│ │  └──────────────────┘  └──────────────────┘  └──────────────────┘               │ │
│ └─────────────────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────────────┘

Princípios da Clean Architecture aplicados:
• Independência de Frameworks: Regras de negócio não dependem de frameworks externos
• Testabilidade: Cada camada pode ser testada independentemente
• Independência da UI: Interface pode mudar sem afetar regras de negócio  
• Independência do Banco de Dados: Regras não conhecem detalhes de persistência
• Independência de Agentes Externos: Regras não dependem de APIs externas
• Regra de Dependência: Dependências apontam sempre para dentro (domínio)
```

**Detalhamento das Camadas:**

- **CORE (Regras de Negócio):**
   - **Domain**: Entidades de negócio puras (Customer, FoodItem, CustomerOrder, etc.)
   - **Application**: Casos de uso que orquestram as regras de negócio
   - **Adapters**: Implementações das interfaces (Controllers, Gateways, Presenters)

- **APP (Infraestrutura):**
   - **Handlers**: Controladores REST que recebem tratam exceções
   - **Integrations**: Clientes para APIs internas e externas (MercadoPago)
   - **Datasources**: Acesso a dados (PostgreSQL JPA, File System)
   - **Webhooks**: Endpoints para receber callbacks externos


### 3. Arquitetura Funcional

O diagrama das imagens abaixo pode ser melhor visualizado no [draw.io](https://app.diagrams.net/#) abrindo o arquivo `lanches-caieiras-arquitetura-fincional.drawio` no diretório `docs/funcional`.

#### 3.1 Cadastro de Clientes
![Cadastro de Clientes](docs/funcional/cadastro_cliente.png)

#### 3.2 - Cadastro de Items de Alimentação
![Cadastro de Items de Alimentação](docs/funcional/cadastro_item_alimentação.png)

#### 3.3 - Novo Pedido - Checkout
![Pedido Recebido](docs/funcional/checkout_pedido.png)

#### 3.4 - Pagamento
![Pagamento](docs/funcional/pagamento.png)

#### 3.5 - Pedido Recebido - Pagamento Confirmado
![Pedido Recebido - Pagamento Confirmado](docs/funcional/pedido_recebido.png)

#### 3.6 - Atualização Preapro
![Atualização Preapro](docs/funcional/atualizacao_preparo.png)

#### 3.7 - Cancelamento Pedido
![Cancelamento Pedido](docs/funcional/cancelamento_pedido.png)

#### 3.8 - Cancelamento Pagamento
![Cancelamento Pagamento](docs/funcional/cancelamento_pagamento.png)

#### 3.9 - Acompanhar Pedidos
![Acompanhar Pedidos](docs/funcional/acompanhar_pedidos.png)

#### 3.10 - Notificações
![Notificações](docs/funcional/notificacoes.png)

## Contrato da API

O contrato da API está disponível no arquivo `lanches-caieiras-api-v2.yaml` no diretório `/docs/api/`

Para visualizar e interagir com a documentação da API, siga os passos:

1. Acesse o [Swagger Editor](https://editor.swagger.io/).
2. Clique em "File" > "Import File" e selecione o arquivo `lanches-caieiras-api-v2.yaml` deste repositório.

Assim, você poderá visualizar e testar o contrato da API de forma interativa.

## Testes via Postman
Para testar a API, você pode usar o Postman. O arquivo de coleção do Postman está disponível no diretório `/docs/api/`.
1. Importe o arquivo `lanches-caieiras-f3-collection.json` no Postman.
2. Certifique-se de que o servidor esteja rodando.
3. A collection está organizada por Domínios:
    - Cliente
    - Items de Alimentação
    - Pedido do Cliente
    - Pagamento
    - Ordem de Preparo
    - Notificações
4. Execute as requisições na seguinte ordem
   - Solicitação de Token para os escopos admin, totem, monitor, já configurado na collection.
     - Para o escopo customer o client_id e secret_id do cliente com cpf e e-mail respectivamente.
   - Criação de Clientes
   - Criação de Items de Alimentação
   - Criar Pedido do Cliente
   - Processar pagamento
   - Atualizar Preparo
   - Finalizar Pedido do Cliente
