# CLAUDE.md — Guia do projeto para o Claude Code

> Este arquivo é lido automaticamente pelo Claude Code no início de cada sessão dentro
> deste repositório. Ele orienta o Claude sobre o projeto, convenções e fluxo de trabalho.
> Mantenha-o curto e factual — é contexto, não documentação extensa.

## O que é este projeto

**despesas-manager** — API REST de gerenciamento de despesas pessoais.
Faz parte de um sistema com dois repositórios:

- **despesas-manager** (este) — back-end Spring Boot.
- **despesas-front** — front-end SPA (HTML/CSS/JS puro), em repositório separado.

## Stack

- **Java 21**, **Spring Boot 3.5.3** (Maven, com wrapper `./mvnw`)
- Spring Web (MVC) · Spring Security · Spring Data JPA / Hibernate
- **MySQL 8** (driver `mysql-connector-j`)
- Autenticação **JWT** (JJWT 0.12.3, HS256) · senhas com **BCrypt**
- `springdoc-openapi` (Swagger UI em `/swagger-ui.html`)
- Lombok

## Arquitetura (resumo)

Camadas em `src/main/java/com/felipe/despesas`:
`controller` → `service` → `repository` → `model`, com `dto`, `config` (segurança) e `exception`.

📐 **A arquitetura completa está documentada em [`docs/especificacao/`](docs/especificacao/README.md)**
(C4, modelo de domínio, casos de uso, fluxos de sequência). Leia antes de mudanças estruturais.

## Comandos

```bash
./mvnw spring-boot:run          # sobe a API (porta 8080)
./mvnw clean package            # build do JAR
./mvnw test                     # roda os testes
./mvnw -q -DskipTests package   # build rápido sem testes
```

> ⚠️ Os testes atuais (`@SpringBootTest contextLoads`) sobem o contexto e **precisam de um
> MySQL acessível** (defaults: `localhost:3306/mydb`, user `root`). Sem banco, falham.
> Suba um MySQL local (ou Docker) ou veja [`docs/specs/spec-004`](docs/specs/spec-004-migrations-flyway.md).

## Configuração (variáveis de ambiente)

`src/main/resources/application.properties` lê env vars com defaults de dev:
`PORT`, `DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `JWT_SECRET`.
**Nunca** comite segredos reais. `JWT_SECRET` precisa ter **≥ 32 bytes** (HS256).

## Convenções

- Idioma do código e dos commits: **português** (mantenha o padrão existente).
- Commits no estilo `tipo: descrição` (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`).
- Branch base: **`main`**. Trabalhe em branches `tipo/descricao` e abra PR.
- Regras de negócio ficam nos **services**; controllers são finos.
- Use **DTOs** na borda HTTP (não exponha entidades JPA diretamente).
- Dinheiro = `BigDecimal`. Datas = `LocalDate`.

## Fluxo de trabalho: Spec-Driven Development (SDD)

Para qualquer mudança não-trivial, siga o ciclo (detalhado em [`docs/ia/PLANO-IA-SDD.md`](docs/ia/PLANO-IA-SDD.md)):

1. **Spec primeiro** — escreva/atualize uma spec em [`docs/specs/`](docs/specs/README.md) usando o [template](docs/specs/TEMPLATE.md).
2. **Plano** — quebre em passos pequenos e verificáveis.
3. **TDD** — escreva o teste que falha, depois o código que o faz passar.
4. **Verificação** — rode `./mvnw test` e confirme antes de dizer "pronto".
5. **PR pequeno** — uma spec por PR sempre que possível.

## Lacunas conhecidas (NÃO regredir)

Há specs prontas para corrigir os pontos abaixo — veja [`docs/specs/`](docs/specs/README.md):

- 🔴 **IDOR**: `GET/PUT/DELETE /despesas/{id}` não validam o dono → [spec-001](docs/specs/spec-001-ownership-despesas.md)
- 🔴 **Front sem JWT**: a SPA não envia `Authorization: Bearer` → [spec-002](docs/specs/spec-002-autenticacao-no-front.md)
- 🟠 **Sem validação no registro** → [spec-003](docs/specs/spec-003-validacao-bean-validation.md)
- 🟠 **Sem migrations** (`ddl-auto=validate`) → [spec-004](docs/specs/spec-004-migrations-flyway.md)
- 🟠 **XSS** no front (`innerHTML`) → [spec-005](docs/specs/spec-005-saneamento-xss-front.md)

Ao mexer em despesas, **sempre** filtre pelo usuário autenticado.
