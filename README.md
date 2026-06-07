# 💸 API de Gerenciamento de Despesas

API REST desenvolvida em **Spring Boot** para controle de despesas pessoais, com autenticação JWT, categorização de despesas e persistência em banco de dados MySQL.

---

## ✅ Funcionalidades

* Cadastro de usuários
* Autenticação com JWT (JSON Web Token)
* Senhas criptografadas com BCrypt
* Cadastro de categorias de despesas
* Cadastro de despesas vinculadas a categorias
* CRUD completo de categorias
* CRUD completo de despesas
* Relacionamento entre entidades
* Tratamento centralizado de exceções
* Proteção de rotas com Spring Security
* Persistência em banco de dados MySQL

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia              | Versão |
| ----------------------- | ------ |
| Java                    | 21     |
| Spring Boot             | 3.5.3  |
| Spring Web              | -      |
| Spring Security         | -      |
| JWT (JJWT)              | -      |
| Spring Data JPA         | -      |
| Hibernate ORM           | -      |
| MySQL                   | 8      |
| BCrypt Password Encoder | -      |
| Postman                 | Testes |

---

## 🚀 Como Usar

**URL Base**

```text
https://dispesas-manager-production.up.railway.app
```

---

## 🔐 Autenticação

A API utiliza autenticação baseada em JWT (JSON Web Token).

### Registrar Usuário

**POST** `/auth/register`

#### Request

```json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```

#### Response

```json
{
  "id": 1,
  "email": "usuario@email.com"
}
```

---

### Login

**POST** `/auth/login`

#### Request

```json
{
  "email": "usuario@email.com",
  "senha": "123456"
}
```

#### Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 🔒 Endpoints Protegidos

Após realizar login, envie o token JWT no cabeçalho das requisições:

```http
Authorization: Bearer SEU_TOKEN
```

### Endpoints Públicos

| Método | Endpoint       |
| ------ | -------------- |
| POST   | /auth/register |
| POST   | /auth/login    |

### Endpoints Protegidos

Todos os endpoints abaixo exigem autenticação.

---

## 💰 Despesas

### Criar Despesa

**POST** `/despesas`

#### Request

```json
{
  "descricao": "Mercado",
  "valor": 150.00,
  "data": "2026-05-01",
  "categoria": {
    "id": 1
  }
}
```

---

### Listar Despesas

**GET** `/despesas`

---

### Buscar Despesa por ID

**GET** `/despesas/{id}`

---

### Atualizar Despesa

**PUT** `/despesas`

#### Request

```json
{
  "id": 1,
  "descricao": "Mercado atualizado",
  "valor": 200.00,
  "data": "2026-05-01",
  "categoria": {
    "id": 1
  }
}
```

---

### Deletar Despesa

**DELETE** `/despesas/{id}`

---

## 🏷️ Categorias

### Criar Categoria

**POST** `/categorias`

#### Request

```json
{
  "nome": "Alimentação"
}
```

---

### Listar Categorias

**GET** `/categorias`

---

### Buscar Categoria por ID

**GET** `/categorias/{id}`

---

### Atualizar Categoria

**PUT** `/categorias`

#### Request

```json
{
  "id": 1,
  "nome": "Transporte"
}
```

---

### Deletar Categoria

**DELETE** `/categorias/{id}`

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
│   ├── LoginRequest
│   └── LoginResponse
├── exception
│   └── GlobalExceptionHandler
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
```

---

## 👤 Autor

**Felipe de Oliveira Romeiro Amais**
Acadêmico de Sistemas de Informação — UNIPAR

GitHub: https://github.com/FelipeAmais

---

## 🔮 Futuras Melhorias

* [ ] Validação de usuários
* [ ] Swagger/OpenAPI
* [ ] Testes unitários
* [ ] Docker
* [ ] Refresh Token
* [ ] Filtros por período
* [ ] Filtros por categoria
* [ ] Recuperação de senha

```
```
