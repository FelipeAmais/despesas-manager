# 💸 API de Gerenciamento de Despesas

API REST desenvolvida em **Spring Boot** para controle de despesas pessoais, com autenticação JWT, categorização de despesas, paginação e persistência em banco de dados MySQL.

---

## ✅ Funcionalidades

* Cadastro de usuários com validação de email único
* Autenticação com JWT (JSON Web Token)
* Senhas criptografadas com BCrypt
* Cadastro de categorias de despesas
* CRUD completo de categorias e despesas
* Despesas vinculadas ao usuário autenticado (isolamento de dados)
* Paginação na listagem de despesas
* Relatório de despesas por período
* Tratamento centralizado de exceções com status HTTP semânticos
* Validação de entrada com Bean Validation
* Proteção de rotas com Spring Security
* Migrations de banco de dados com Flyway
* Documentação interativa com Swagger UI

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia              | Versão |
| ----------------------- | ------ |
| Java                    | 21     |
| Spring Boot             | 3.5.3  |
| Spring Web              | -      |
| Spring Security         | -      |
| JWT (JJWT)              | 0.12.3 |
| Spring Data JPA         | -      |
| Hibernate ORM           | -      |
| MySQL                   | 8      |
| Flyway                  | -      |
| Lombok                  | -      |
| Bean Validation         | -      |
| Springdoc OpenAPI       | 2.8.5  |
| Docker                  | -      |
| BCrypt Password Encoder | -      |

---

## 🚀 Como Rodar Localmente

### Pré-requisitos

* Java 21
* MySQL 8 rodando localmente
* Maven (ou use o `./mvnw` incluído)

### Passos

```bash
git clone https://github.com/FelipeAmais/despesas-manager
cd despesas-manager
```

Crie o banco de dados:

```sql
CREATE DATABASE mydb;
```

Configure as variáveis de ambiente (ou edite `application.properties`):

```bash
DATABASE_URL=jdbc:mysql://localhost:3306/mydb

DATABASE_USERNAME=root

DATABASE_PASSWORD=

JWT_SECRET=sua-chave-secreta-com-mais-de-32-caracteres

```
Execute:

```bash
./mvnw spring-boot:run
```

O Flyway criará as tabelas automaticamente ao subir.

---

## 🐳 Rodando com Docker

```bash
docker build -t despesas-manager .

docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:mysql://host.docker.internal:3306/mydb \
  -e DATABASE_USERNAME=root \
  -e DATABASE_PASSWORD= \
  -e JWT_SECRET=sua-chave-secreta-com-mais-de-32-caracteres \
  despesas-manager
```

---

## 📄 Documentação

Com a aplicação rodando, acesse o Swagger UI:

http://localhost:8080/swagger-ui/index.html

---

## 🔐 Autenticação

### Registrar Usuário

**POST** `/auth/register`

```json
{
  "email": "usuario@email.com",
  "senha": "12345678"
}
```

### Login

**POST** `/auth/login`

```json
{
  "email": "usuario@email.com",
  "senha": "12345678"
}
```

**Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

Envie o token em todas as requisições protegidas:

```http
Authorization: Bearer SEU_TOKEN
```

---

## 💰 Despesas

### Criar Despesa

**POST** `/despesas`

```json
{
  "descricao": "Mercado",
  "valor": 150.00,
  "data": "2026-05-01",
  "categoriaId": 1
}
```

### Listar Despesas (paginado)

**GET** `/despesas?page=0&size=10`

### Buscar por ID

**GET** `/despesas/{id}`

### Relatório por Período

**GET** `/despesas/relatorio?inicio=2026-01-01&fim=2026-12-31`

### Atualizar Despesa

**PUT** `/despesas/{id}`

```json
{
  "descricao": "Mercado atualizado",
  "valor": 200.00,
  "data": "2026-05-01",
  "categoriaId": 1
}
```

### Deletar Despesa

**DELETE** `/despesas/{id}`

---

## 🏷️ Categorias

### Criar Categoria

**POST** `/categorias`

```json
{
  "nome": "Alimentação"
}
```

### Listar Categorias

**GET** `/categorias`

### Buscar por ID

**GET** `/categorias/{id}`

### Atualizar Categoria

**PUT** `/categorias`

```json
{
  "id": 1,
  "nome": "Transporte"
}
```

### Deletar Categoria

**DELETE** `/categorias/{id}`

---

## 🧪 Testes

```bash
./mvnw test
```

Os testes usam H2 em memória — não é necessário MySQL rodando. O Flyway é desabilitado automaticamente no perfil de teste.

---

## 📁 Estrutura do Projeto

```text
src/main/java/com/felipe/despesas
├── config
│   ├── JwtAuthFilter
│   ├── PasswordConfig
│   └── SecurityConfig
├── controller
│   ├── AuthController
│   ├── CategoriaController
│   └── DespesaController
├── dto
│   ├── DespesaRequest
│   ├── DespesaResponse
│   ├── LoginRequest
│   └── LoginResponse
├── exception
│   ├── GlobalExceptionHandler
│   ├── InvalidCredentialsException
│   └── NotFoundException
├── model
│   ├── Categoria
│   ├── Despesa
│   └── Usuario
├── repository
│   ├── CategoriaRepository
│   ├── DespesaRepository
│   └── UsuarioRepository
├── services
│   ├── CategoriaService
│   ├── DespesaService
│   ├── JwtService
│   └── UsuarioService
└── DespesasApplication

src/main/resources
├── application.properties
└── db/migration
    └── V1__criar_tabelas.sql
```

---

## 👤 Autor

**Felipe de Oliveira Romeiro Amais**
Acadêmico de Sistemas de Informação — UNIPAR

GitHub: https://github.com/FelipeAmais

---

## 🔮 Futuras Melhorias

* [ ] Rate limiting nos endpoints de autenticação
* [ ] Refresh Token
* [ ] Recuperação de senha
* [ ] Autorização por papel (ROLE_ADMIN para categorias)
* [ ] Filtros por categoria nas despesas
