# 📋 Specs — Spec-Driven Development

Cada mudança não-trivial neste projeto começa por uma **spec**: um contrato curto que
descreve *o problema*, *os critérios de aceite* e *o plano de teste* antes de qualquer código.

> Veja o fluxo completo em [`docs/ia/PLANO-IA-SDD.md`](../ia/PLANO-IA-SDD.md).
> Para criar uma nova spec, use a skill `criar-spec` ou copie [`TEMPLATE.md`](TEMPLATE.md).

## Convenções

- Arquivos: `spec-NNN-titulo-curto.md` (numeração sequencial).
- **Um PR por spec.** Status na própria spec: `Proposta` → `Em progresso` → `Concluída`.
- Critérios de aceite são **verificáveis** (status HTTP, comportamento), não de implementação.
- TDD: o teste do plano vem **antes** do código.

## Índice

| Spec | Prioridade | Alvo | Status | Tema |
|------|-----------|------|--------|------|
| [spec-001](spec-001-ownership-despesas.md) | 🔴 Crítica | back-end | Proposta | Isolar despesas por usuário (corrige IDOR) |
| [spec-002](spec-002-autenticacao-no-front.md) | 🔴 Crítica | front-end | Proposta | Login + envio de JWT no front |
| [spec-003](spec-003-validacao-bean-validation.md) | 🟠 Alta | back-end | Proposta | Validação de entrada (Bean Validation) |
| [spec-004](spec-004-migrations-flyway.md) | 🟠 Alta | back-end | Proposta | Migrations de banco com Flyway |
| [spec-005](spec-005-saneamento-xss-front.md) | 🟠 Alta | front-end | Proposta | Sanear XSS na renderização |

## Backlog (próximas specs a escrever)

- 🟡 CORS explícito e restrito (origens conhecidas) + revisar `@CrossOrigin("*")`.
- 🟡 `JWT_SECRET` forte (≥32 bytes) e validação na subida da app.
- 🟡 `register` retornar DTO (não a entidade `Usuario`).
- 🟡 Categorias por usuário (decisão de produto).
- 🟢 Refresh token · filtros por período/categoria · Docker · gráficos no front.
