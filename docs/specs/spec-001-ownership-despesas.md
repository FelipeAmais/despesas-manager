# spec-001 — Isolar despesas por usuário (corrige IDOR)

- **Prioridade:** 🔴 Crítica
- **Alvo:** back-end (despesas-manager)
- **Status:** Proposta
- **Relacionada:** [03-componentes-c4](../especificacao/03-componentes-c4.md), [05-casos-de-uso](../especificacao/05-casos-de-uso.md)

## 1. Problema / contexto

A feature "vincular despesas ao usuário" só foi aplicada em `listarDespesas()`. Os demais
métodos de `DespesaService` operam por `id` **sem verificar o dono**:

- `buscarPorId(id)` → `despesaRepository.findById(id)` (sem filtro de usuário)
- `atualizarDespesa(id, ...)` → `findById(id)` (sem filtro)
- `excluirDespesa(id)` → `existsById(id)` + `deleteById(id)` (sem filtro)

Resultado (**IDOR / Broken Access Control**): qualquer usuário autenticado consegue **ler,
editar e excluir despesas de outros usuários** informando o `id`. Isso anula o propósito do login.

## 2. Objetivo

Garantir que um usuário só acessa (ler/editar/excluir) **as próprias despesas**. Acesso a
despesa de terceiro responde como inexistente.

## 3. Critérios de aceite

- [ ] `GET /despesas/{id}` de uma despesa de **outro** usuário retorna **404** (não 200, não 403 — não vaza existência).
- [ ] `GET /despesas/{id}` da **própria** despesa retorna **200** com os dados.
- [ ] `PUT /despesas/{id}` de despesa de outro usuário retorna **404** e **não** altera o dado.
- [ ] `DELETE /despesas/{id}` de despesa de outro usuário retorna **404** e **não** apaga o dado.
- [ ] `GET /despesas` continua retornando **somente** as despesas do usuário autenticado.
- [ ] Operações na própria despesa seguem funcionando (200/204).

## 4. Design proposto

- No `DespesaRepository`, adicionar consultas com escopo de usuário:
  ```java
  Optional<Despesa> findByIdAndUsuario(Long id, Usuario usuario);
  boolean existsByIdAndUsuario(Long id, Usuario usuario);
  ```
- Em `DespesaService`, trocar `findById`/`existsById` por essas versões, usando
  `getUsuarioAutenticado()`. Quando não encontrar, lançar uma exceção mapeada para **404**.
- Introduzir `RecursoNaoEncontradoException` e tratá-la em `GlobalExceptionHandler` → `404 NOT_FOUND`.
  (Hoje "não encontrada" cai em `IllegalArgumentException` → 422; padronizar para 404 nestes casos.)
- `atualizarDespesa` deve carregar a despesa **já filtrada por usuário** antes de alterar.

## 5. Plano de teste (TDD)

Testes de integração (`@SpringBootTest` + MockMvc), criando 2 usuários A e B:

| # | Teste | Prova qual critério |
|---|-------|---------------------|
| 1 | B faz `GET /despesas/{idDoA}` → 404 | isolamento na leitura |
| 2 | A faz `GET /despesas/{idDoA}` → 200 | acesso próprio |
| 3 | B faz `PUT /despesas/{idDoA}` → 404 e o dado de A permanece | isolamento na edição |
| 4 | B faz `DELETE /despesas/{idDoA}` → 404 e o dado de A permanece | isolamento na exclusão |
| 5 | `GET /despesas` como B não inclui despesas de A | escopo da listagem |

> Escreva o teste 1 primeiro (deve falhar contra o código atual), depois implemente.

## 6. Fora de escopo

- Papéis/roles (admin que vê tudo).
- Paginação/filtros de listagem.

## 7. Riscos / observações

- Padronizar 404 vs 422 pode afetar mensagens que o front espera — alinhar com [spec-002](spec-002-autenticacao-no-front.md).
- Garantir que `buscarPorId` use o mesmo escopo (hoje ele nem checava dono).
