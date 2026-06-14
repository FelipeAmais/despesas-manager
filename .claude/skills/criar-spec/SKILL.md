---
name: criar-spec
description: Cria uma nova spec de feature/correção seguindo o template SDD do projeto, em docs/specs/. Use ao iniciar qualquer mudança não-trivial, antes de escrever código.
---

# Criar uma spec (Spec-Driven Development)

Neste projeto, **toda mudança não-trivial começa por uma spec**. A spec é o contrato:
descreve o problema, o que é sucesso (critérios de aceite) e o plano de teste, *antes*
de tocar no código. Isso deixa a implementação (sua ou do Claude) focada e verificável.

## Quando usar

- Novo endpoint, regra de negócio, correção de bug relevante, mudança de segurança ou schema.
- **Não** precisa para: ajuste de texto, rename trivial, correção de typo.

## Como criar

1. Escolha o próximo número: olhe `docs/specs/` e pegue o maior `spec-NNN` + 1.
2. Copie o template:

   ```bash
   cp docs/specs/TEMPLATE.md docs/specs/spec-NNN-titulo-curto.md
   ```

3. Preencha **todas** as seções do template. Princípios:
   - **Problema/contexto**: por que isso importa, com link para o código atual (`arquivo:linha`).
   - **Critérios de aceite**: lista verificável (`- [ ]`), em termos observáveis (status HTTP, comportamento), não de implementação.
   - **Plano de teste**: quais testes vão provar cada critério (TDD — o teste vem antes).
   - **Fora de escopo**: o que esta spec deliberadamente NÃO faz.
4. Adicione a nova spec ao índice em `docs/specs/README.md`.

## Depois da spec

- Implemente seguindo **TDD**: escreva o teste que falha → código que passa → refatore.
- Rode `./mvnw test` e confirme verde antes de declarar pronto.
- Abra **um PR por spec** (mantém a revisão pequena).

## Dica de uso com o Claude

Peça assim: *"Implemente a spec-NNN usando TDD. Leia a spec inteira primeiro, liste os
testes que vai escrever, e só então comece pelo primeiro teste que falha."*
