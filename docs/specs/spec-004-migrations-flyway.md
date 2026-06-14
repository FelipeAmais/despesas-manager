# spec-004 — Migrations de banco com Flyway

- **Prioridade:** 🟠 Alta
- **Alvo:** back-end (despesas-manager)
- **Status:** Proposta
- **Relacionada:** [07-deploy](../especificacao/07-deploy.md), [04-modelo-de-dominio](../especificacao/04-modelo-de-dominio.md)

## 1. Problema / contexto

A app usa `spring.jpa.hibernate.ddl-auto=validate` — não cria nem altera o schema. Não há
ferramenta de migração. Consequências:

- Um banco **novo** (ex.: Railway recriado) faz a app **falhar na subida** (schema ausente).
- Os testes `@SpringBootTest` precisam de um banco com o schema pronto.
- Evoluções de schema são manuais e não versionadas.

## 2. Objetivo

Versionar o schema com **Flyway**, criando-o de forma reprodutível, mantendo `ddl-auto=validate`
(Hibernate valida contra o schema que o Flyway garante).

## 3. Critérios de aceite

- [ ] Subir a app contra um banco **vazio** cria o schema automaticamente (via Flyway) e a app inicia.
- [ ] `ddl-auto` permanece `validate` (o Hibernate não altera schema).
- [ ] Existe `V1__baseline.sql` refletindo as tabelas atuais (`usuarios`, `categorias`, `despesas`) com chaves, FKs e `email` único.
- [ ] `./mvnw test` roda com um banco de teste sem setup manual de schema.

## 4. Design proposto

- Adicionar `flyway-core` (e `flyway-mysql`) ao `pom.xml`.
- Criar `src/main/resources/db/migration/V1__baseline.sql` com o schema atual (ver
  [modelo de domínio](../especificacao/04-modelo-de-dominio.md)): PKs `AUTO_INCREMENT`,
  `usuarios.email` `UNIQUE`, FKs `despesas.categoria_id NOT NULL` e `despesas.usuario_id`.
- Habilitar Flyway nas properties (padrão já habilita ao ter a dependência).
- Para testes: configurar um perfil de teste (Testcontainers MySQL **ou** H2 em modo MySQL)
  para o Flyway rodar isolado. Preferir **Testcontainers** para fidelidade ao MySQL.

## 5. Plano de teste (TDD)

| # | Teste | Prova qual critério |
|---|-------|---------------------|
| 1 | `contextLoads` contra banco vazio (Testcontainers) sobe sem erro | Flyway cria schema |
| 2 | Teste que verifica a existência das 3 tabelas e da constraint `UNIQUE(email)` | baseline correto |

## 6. Fora de escopo

- Dados de seed (categorias iniciais) — opcional, pode virar `V2`.
- Migração de dados de produção (coordenar com o Felipe antes).

## 7. Riscos / observações

- Em produção, a **primeira** execução do Flyway sobre um banco que já tenha tabelas exige
  `baseline-on-migrate=true` para não conflitar. Validar o estado do banco do Railway antes.
