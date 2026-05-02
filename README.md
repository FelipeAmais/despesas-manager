# 💸 API de Gerenciamento de Despesas

API REST desenvolvida em **Spring Boot** para o controle de despesas pessoais, com categorização, persistência em banco de dados MySQL e estrutura baseada em boas práticas de desenvolvimento.

---

## 📌 Funcionalidades

- ✅ Cadastro de **categorias de despesas**
- ✅ Cadastro de **despesas**, vinculadas a uma categoria
- ✅ Edição, listagem e exclusão de categorias e despesas
- ✅ Relacionamento entre entidades
- ✅ Tratamento básico de erros
- ✅ Separação entre Controller, Service e Repository
- ✅ Testado com Postman

---

## 🧱 Tecnologias Utilizadas

- ✅ Java 21
- ✅ Spring Boot 3.5.3
- ✅ Spring Web
- ✅ Spring Data JPA
- ✅ MySQL 8
- ✅ Hibernate ORM
- ✅ Postman (para testes)

---

### 📌 Autor  
Desenvolvido por Felipe de Oliveira Romeiro Amais  
Acadêmico de Sistemas de Informação - UNIPAR  
https://github.com/FelipeAmais

---

## 📈 Futuras melhorias  
🔐 Autenticação com Spring Security + JWT

📊 Filtros por data e categoria

🌐 Integração com frontend (React)

📄 Documentação Swagger/OpenAPI

## Como Usar

URL: https://dispesas-manager-production.up.railway.app

GET Despesas: https://dispesas-manager-production.up.railway.app/despesas
POST Despesas: https://dispesas-manager-production.up.railway.app/despesas
body {
descricao,
valor,
data(YYYY/MM/DD),,
categoria_id
}
PUT Despesas: https://dispesas-manager-production.up.railway.app/despesas
body {
id,
descricao,
valor,
data(YYYY/MM/DD),
categoria_id
}
DELETE Despesas: https://dispesas-manager-production.up.railway.app/despesas/{id}

GET Categorias https://dispesas-manager-production.up.railway.app/categorias
POST Categorias: https://dispesas-manager-production.up.railway.app/categorias
body {
nome
}
DELETE Categorias: https://dispesas-manager-production.up.railway.app/categorias/{id}
