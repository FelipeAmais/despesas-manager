# 💸 API de Gerenciamento de Despesas

API REST desenvolvida em **Spring Boot** para controle de despesas pessoais, com categorização, persistência em banco de dados MySQL e estrutura baseada em boas práticas de desenvolvimento.

---

## ✅ Funcionalidades

- Cadastro de categorias de despesas
- Cadastro de despesas vinculadas a uma categoria
- Edição, listagem e exclusão de categorias e despesas
- Relacionamento entre entidades
- Tratamento centralizado de erros
- Separação entre Controller, Service e Repository
- Testado com Postman

---

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.3 |
| Spring Web | - |
| Spring Data JPA | - |
| MySQL | 8 |
| Hibernate ORM | - |
| Postman | (testes) |

---

## 👤 Autor

Desenvolvido por **Felipe de Oliveira Romeiro Amais**  
Acadêmico de Sistemas de Informação — UNIPAR  
[github.com/FelipeAmais](https://github.com/FelipeAmais)

---

## 🔮 Futuras Melhorias

- [ ] Autenticação com Spring Security + JWT
- [ ] Filtros por data e categoria
- [ ] Integração com frontend (React)
- [ ] Documentação Swagger/OpenAPI

---

## 🚀 Como Usar

**URL Base:** `https://dispesas-manager-production.up.railway.app`

---

## 📋 Endpoints

### 💰 Despesas

#### `POST /despesas` — Criar despesa

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

#### `GET /despesas` — Listar despesas

#### `GET /despesas/{id}` — Buscar despesa por ID

#### `PUT /despesas` — Atualizar despesa

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

#### `DELETE /despesas/{id}` — Deletar despesa

---

### 🏷️ Categorias

#### `POST /categorias` — Criar categoria

```json
{
  "nome": "Alimentação"
}
```

#### `GET /categorias` — Listar categorias

#### `GET /categorias/{id}` — Buscar categoria por ID

#### `PUT /categorias` — Atualizar categoria

```json
{
  "id": 1,
  "nome": "Transporte"
}
```

#### `DELETE /categorias/{id}` — Deletar categoria
