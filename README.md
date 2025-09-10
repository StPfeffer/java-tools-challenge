# API de Pagamentos - Java Tools Challenge

## Objetivo

Esta API REST foi desenvolvida para processar pagamentos com cartão de crédito, oferecendo funcionalidades de
autorização, consulta e estorno de transações.

## Arquitetura

O projeto segue uma arquitetura em camadas baseada nos princípios do **Domain Driven Design (DDD)** e **Clean
Architecture**, com clara separação de responsabilidades:

- **Controller Layer**: Responsável pelos endpoints REST e validações de entrada
- **Service Layer**: Contém a lógica de negócio das transações
- **Mapper Layer**: Responsável pela conversão entre DTOs, Requests e Entities
- **Repository Layer**: Gerencia a persistência dos dados (em memória)
- **Domain Layer**: Contém entidades, DTOs, requests e enums do domínio
- **Exception Layer**: Trata exceções específicas do domínio

### Padrões Utilizados

- **Repository Pattern**: Para abstração da camada de dados
- **Service Pattern**: Para encapsulamento da lógica de negócio
- **Mapper Pattern**: Para conversão entre camadas de dados
- **DTO Pattern**: Para transferência de dados entre camadas
- **Builder Pattern**: Para construção consistente de objetos
- **Exception Handler**: Para tratamento global de exceções

## Tecnologias

- **Java 21**
- **Spring Boot 3.5.5**
- **Spring Web** - Para criação dos endpoints REST
- **Lombok** - Para redução de código boilerplate e Builder pattern
- **JUnit 5** - Para testes unitários
- **Mockito** - Para mocks nos testes
- **Maven** - Para gerenciamento de dependências

## Estrutura do Projeto

```
src/
├── main/java/com/pfeffer/javatoolschallenge/
│   ├── controller/          # Endpoints REST
│   ├── domain/              # Camada de domínio
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # Entidades do domínio
│   │   ├── enums/           # Enumerações do domínio
│   │   └── request/         # Objetos de requisição
│   ├── exception/           # Exceções customizadas e handlers
│   ├── mapper/              # Conversores entre camadas
│   ├── repository/          # Camada de persistência
│   └── service/             # Lógica de negócio
└── test/java/               # Testes unitários
    ├── controller/          # Testes dos controllers
    ├── exception/           # Testes dos exception handlers
    ├── mapper/              # Testes dos mappers
    ├── repository/          # Testes dos repositories
    └── service/             # Testes dos services
```

## Funcionalidades

### 1. Processamento de Pagamento

- **Endpoint**: `POST /api/v1/pagamentos`
- **Funcionalidade**: Processa uma nova transação de pagamento
- **Validações**:
  - ID da transação deve ser único
  - Forma de pagamento deve ser válida (AVISTA, PARCELADO_LOJA, PARCELADO_EMISSOR)
  - À vista deve ter 1 parcela, parcelado deve ter > 1 parcela
- **Resposta**: Transação enriquecida com NSU, código de autorização e status

### 2. Consulta de Transação

- **Endpoint**: `GET /api/v1/pagamentos/{id}`
- **Funcionalidade**: Consulta uma transação específica por ID
- **Resposta**: Dados completos da transação

### 3. Consulta de Todas as Transações

- **Endpoint**: `GET /api/v1/pagamentos`
- **Funcionalidade**: Lista todas as transações processadas
- **Resposta**: Array com todas as transações

### 4. Estorno de Transação

- **Endpoint**: `PUT /api/v1/pagamentos/{id}/estorno`
- **Funcionalidade**: Estorna uma transação autorizada
- **Validação**: Apenas transações com status AUTORIZADO podem ser estornadas
- **Resposta**: Transação com status alterado para CANCELADO

## Status e Tipos

### Status de Transação

- `AUTORIZADO`: Transação aprovada
- `NEGADO`: Transação rejeitada
- `CANCELADO`: Transação estornada

### Tipos de Forma de Pagamento

- `AVISTA`: Pagamento à vista (1 parcela)
- `PARCELADO_LOJA`: Parcelamento pela loja
- `PARCELADO_EMISSOR`: Parcelamento pelo emissor do cartão

## Como Executar

### Pré-requisitos

- Java 21+
- Maven 3.8+

### Passos

1. **Clone o repositório**

```bash
git clone <url-do-repositorio>
cd java-tools-challenge
```

2. **Compile e execute os testes**

```bash
./mvnw clean test
```

3. **Execute a aplicação**

```bash
./mvnw spring-boot:run
```

4. **A API estará disponível em**: `http://localhost:8080`

## Exemplos de Uso

### Processamento de Pagamento

**Request:**

```bash
POST http://localhost:8080/api/v1/pagamentos
Content-Type: application/json

{
  "cartao": "4444********1234",
  "id": "1000235689000001",
  "descricao": {
    "valor": "500.50",
    "dataHora": "2021-05-01T18:30:00",
    "estabelecimento": "PetShop Mundo cão"
  },
  "formaPagamento": {
    "tipo": "AVISTA",
    "parcelas": 1
  }
}
```

**Response (201 Created):**

```json
{
  "transacao": {
    "cartao": "4444********1234",
    "id": "1000235689000001",
    "descricao": {
      "valor": "500.50",
      "dataHora": "01/05/2021 18:30:00",
      "estabelecimento": "PetShop Mundo cão",
      "nsu": "1234567890",
      "codigoAutorizacao": "147258369",
      "status": "AUTORIZADO"
    },
    "formaPagamento": {
      "tipo": "AVISTA",
      "parcelas": 1
    }
  }
}
```

### Consulta de Transação

**Request:**

```bash
GET http://localhost:8080/api/v1/pagamentos/1000235689000001
```

**Response (200 OK):**

```json
{
  "transacao": {
    "cartao": "4444********1234",
    "id": "1000235689000001",
    "descricao": {
      "valor": "500.50",
      "dataHora": "01/05/2021 18:30:00",
      "estabelecimento": "PetShop Mundo cão",
      "nsu": "1234567890",
      "codigoAutorizacao": "147258369",
      "status": "AUTORIZADO"
    },
    "formaPagamento": {
      "tipo": "AVISTA",
      "parcelas": 1
    }
  }
}
```

### Estorno de Transação

**Request:**

```bash
PUT http://localhost:8080/api/v1/pagamentos/1000235689000001/estorno
```

**Response (200 OK):**

```json
{
  "transacao": {
    "cartao": "4444********1234",
    "id": "1000235689000001",
    "descricao": {
      "valor": "500.50",
      "dataHora": "01/05/2021 18:30:00",
      "estabelecimento": "PetShop Mundo cão",
      "nsu": "1234567890",
      "codigoAutorizacao": "147258369",
      "status": "CANCELADO"
    },
    "formaPagamento": {
      "tipo": "AVISTA",
      "parcelas": 1
    }
  }
}
```

## Qualidade e Testes

O projeto possui **cobertura completa de testes** incluindo:

- **Testes Unitários**: Para todos os services, mappers, repositories e exception handlers
- **Testes de Integração**: Para validação das camadas de controller
- **Mocking**: Uso extensivo do Mockito para isolamento de dependências
- **Padrões de Teste**: Builder pattern nos testes para criação consistente de objetos
- **Cobertura de Código**: Relatórios com JaCoCo
- **Análise de Qualidade**: Checkstyle e Qodana configurados
