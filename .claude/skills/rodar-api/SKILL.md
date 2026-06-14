---
name: rodar-api
description: Sobe a API despesas-manager localmente com um MySQL via Docker, valida que respondeu e mostra como testar os endpoints. Use ao rodar, subir ou testar a API localmente.
---

# Rodar a API localmente

Objetivo: subir a `despesas-manager` num ambiente local funcional (API + MySQL) e
confirmar que está respondendo, para o desenvolvedor testar mudanças.

## Passo 1 — Subir um MySQL local

Se ainda não houver MySQL na porta 3306, suba um com Docker:

```bash
docker run -d --name despesas-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=mydb \
  -p 3306:3306 mysql:8
```

> A app usa `ddl-auto=validate`, então **as tabelas precisam existir**. Enquanto a
> migration (spec-004) não estiver pronta, crie o schema manualmente a partir do
> modelo em `docs/especificacao/04-modelo-de-dominio.md`, ou rode temporariamente
> com `--spring.jpa.hibernate.ddl-auto=update` só para criar:
>
> ```bash
> ./mvnw spring-boot:run -Dspring-boot.run.arguments=--spring.jpa.hibernate.ddl-auto=update
> ```

## Passo 2 — Subir a API

```bash
./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

## Passo 3 — Validar (smoke test)

Não diga que "está rodando" sem checar. Faça o fluxo mínimo:

```bash
# registrar
curl -s -X POST localhost:8080/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"email":"teste@ex.com","senha":"123456"}'

# login → pega o token
TOKEN=$(curl -s -X POST localhost:8080/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"email":"teste@ex.com","senha":"123456"}' | sed 's/.*"token":"\([^"]*\)".*/\1/')

# criar categoria e listar despesas (autenticado)
curl -s -X POST localhost:8080/categorias -H "Authorization: Bearer $TOKEN" \
  -H 'Content-Type: application/json' -d '{"nome":"Alimentação"}'
curl -s localhost:8080/despesas -H "Authorization: Bearer $TOKEN"
```

- Swagger UI: <http://localhost:8080/swagger-ui.html>

## Critério de sucesso

- `register` retorna **201**; `login` retorna um **token**; chamada autenticada retorna **200**;
- chamada **sem** token a `/despesas` retorna **401** (segurança ativa).

## Limpeza

```bash
docker rm -f despesas-mysql
```
