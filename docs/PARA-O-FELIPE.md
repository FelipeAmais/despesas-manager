# 👋 Para o Felipe — o que foi feito e como continuar

Olá, Felipe! Este documento resume o trabalho de **análise + preparação do projeto para
desenvolvimento assistido por IA (Claude Code)** e te mostra por onde começar.

---

## 1. O que foi entregue (em 2 PRs)

| PR | Conteúdo | Pasta |
|----|----------|-------|
| **#13 — Especificação de arquitetura** | C4 (contexto/containers/componentes), modelo de domínio, casos de uso, sequência, deploy, frameworks — 14 diagramas Mermaid. | [`docs/especificacao/`](especificacao/README.md) |
| **Este PR — Fundação de IA + SDD** | `CLAUDE.md`, skills, plano + guia didático, e 5 specs para corrigir os gaps. | `CLAUDE.md`, `.claude/skills/`, `docs/ia/`, `docs/specs/` |

> ⚠️ Este PR foi criado **empilhado** sobre o #13 (stacked). Recomendo **mergear o #13 primeiro**;
> depois este PR mostra só o conteúdo novo de IA/SDD.

---

## 2. O que cada coisa faz

- **[`CLAUDE.md`](../CLAUDE.md)** — o Claude Code lê isso sozinho ao abrir o projeto. Tem stack,
  comandos, convenções e as lacunas a não regredir.
- **[`.claude/skills/`](../.claude/skills/)** — "receitas" que o Claude carrega quando precisa:
  - `rodar-api` — sobe a API + MySQL local e faz smoke test.
  - `criar-spec` — cria uma nova spec no padrão SDD.
- **[`docs/ia/PLANO-IA-SDD.md`](ia/PLANO-IA-SDD.md)** — **comece por aqui.** Explica, de forma
  didática, como desenvolver com o Claude usando Spec-Driven Development.
- **[`docs/specs/`](specs/README.md)** — template + 5 specs prontas para implementar.

---

## 3. As 5 correções priorizadas (specs prontas)

Da análise do código, os pontos mais importantes — cada um já tem uma spec com critérios de
aceite e plano de teste:

| Spec | Prioridade | O problema (resumido) |
|------|-----------|------------------------|
| [spec-001](specs/spec-001-ownership-despesas.md) | 🔴 | **IDOR**: `GET/PUT/DELETE /despesas/{id}` não checam o dono → qualquer usuário acessa despesa de outro. |
| [spec-002](specs/spec-002-autenticacao-no-front.md) | 🔴 | **Front sem login**: a SPA não envia o token → tudo dá 401. |
| [spec-003](specs/spec-003-validacao-bean-validation.md) | 🟠 | Registro sem validação (`validarUsuario()` vazio). |
| [spec-004](specs/spec-004-migrations-flyway.md) | 🟠 | Sem migrations → deploy/testes frágeis. |
| [spec-005](specs/spec-005-saneamento-xss-front.md) | 🟠 | XSS na renderização do front (`innerHTML`). |

> Estas duas em vermelho fazem o app **não funcionar com segurança ponta-a-ponta** hoje.
> Sugiro atacá-las primeiro (Fase 1 do plano).

---

## 4. Como começar (3 passos)

```text
1. Instale o Claude Code e abra-o na pasta deste projeto.
2. Leia docs/ia/PLANO-IA-SDD.md (10 min).
3. Peça ao Claude:
   "Leia o CLAUDE.md e a docs/specs/spec-001. Liste o plano e os testes antes de codar."
```

A partir daí: um PR por spec, na ordem das fases do plano. Os diagramas em
`docs/especificacao/` são o seu mapa quando precisar entender alguma parte do sistema.

---

## 5. Observações importantes

- **Nada de código de produção foi alterado** nestes PRs — são só documentação e configuração.
- As specs de front (002 e 005) descrevem trabalho no repositório **despesas-front**; os PRs
  de implementação dessas vão lá.
- Pontos menores (CORS aberto, `JWT_SECRET`, `register` retornando entidade, categorias globais)
  estão no **backlog** em [`docs/specs/README.md`](specs/README.md) para virarem specs quando você quiser.

Qualquer dúvida, comenta no PR. 🚀
