# spec-003 — Validação de entrada (Bean Validation)

- **Prioridade:** 🟠 Alta
- **Alvo:** back-end (despesas-manager)
- **Status:** Proposta
- **Relacionada:** [05-casos-de-uso](../especificacao/05-casos-de-uso.md)

## 1. Problema / contexto

O registro não valida nada: `UsuarioService.validarUsuario()` está **vazio** e nenhum DTO
usa Bean Validation. É possível registrar com e-mail inválido ou senha em branco. As regras
de despesa existem, mas estão espalhadas em código imperativo no service.

## 2. Objetivo

Validar a entrada na borda HTTP de forma declarativa, com respostas **400** consistentes e
mensagens claras por campo.

## 3. Critérios de aceite

- [ ] `POST /auth/register` com e-mail inválido ou senha curta (< 6) retorna **400** com mensagem por campo.
- [ ] `POST /auth/register` válido continua retornando **201**.
- [ ] `POST /despesas` com `descricao` vazia, `valor <= 0` ou `data` futura retorna **400**.
- [ ] Erros de validação têm corpo previsível (campo → mensagem), não stack trace.

## 4. Design proposto

- Adicionar a dependência `spring-boot-starter-validation` ao `pom.xml`.
- Anotar os DTOs:
  - `LoginRequest`: `@Email @NotBlank email`, `@NotBlank @Size(min=6) senha`.
  - `DespesaRequest`: `@NotBlank descricao`, `@NotNull @Positive valor`, `@NotNull @PastOrPresent data`, `@NotNull categoriaId`.
- Anotar os parâmetros dos controllers com `@Valid`.
- Em `GlobalExceptionHandler`, tratar `MethodArgumentNotValidException` → **400** com mapa `campo: mensagem`.
- Remover a validação imperativa duplicada de `validarDespesa` que passou a ser coberta por anotações
  (manter no service apenas o que não é expressável por anotação, se houver).

## 5. Plano de teste (TDD)

| # | Teste | Prova qual critério |
|---|-------|---------------------|
| 1 | register com email `"abc"` → 400 | validação de e-mail |
| 2 | register com senha `"123"` → 400 | tamanho mínimo de senha |
| 3 | register válido → 201 | caminho feliz |
| 4 | criar despesa com `valor = 0` → 400 | `@Positive` |
| 5 | criar despesa com `data` futura → 400 | `@PastOrPresent` |

## 6. Fora de escopo

- Unicidade de e-mail (já coberta por constraint + handler de `DataIntegrityViolationException`).
- Política de senha forte (complexidade) — vira spec própria se desejado.

## 7. Riscos / observações

- Mudança de status (422 → 400) para validações: alinhar com o front (spec-002) e documentar no README da API.
