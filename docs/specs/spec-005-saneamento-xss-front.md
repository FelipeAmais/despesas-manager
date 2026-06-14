# spec-005 — Sanear XSS na renderização (front)

- **Prioridade:** 🟠 Alta
- **Alvo:** front-end (despesas-front) — *repositório separado*
- **Status:** Proposta
- **Relacionada:** [spec-002](spec-002-autenticacao-no-front.md)

## 1. Problema / contexto

Em `js/script.js`, `renderizarDespesas()` monta as linhas da tabela via **template string em
`innerHTML`**, interpolando `descricao` e `categoria.nome` vindos da API:

```js
item.innerHTML = `<td>${element.descricao}</td><td>${element.categoria.nome}</td>...`;
```

Como esses dados são controlados pelo usuário (criados em despesas/categorias), uma descrição
com HTML/script é **executada no navegador** → **XSS armazenado**.

## 2. Objetivo

Renderizar dados do usuário sem permitir injeção de HTML/script.

## 3. Critérios de aceite

- [ ] Uma despesa com `descricao` contendo `<img src=x onerror=alert(1)>` é exibida como **texto literal**, sem executar.
- [ ] O mesmo vale para `categoria.nome` e qualquer outro dado da API renderizado.
- [ ] A tabela continua exibindo data, descrição, categoria, valor e os botões editar/excluir.

## 4. Design proposto

- Trocar a construção via `innerHTML` por criação de nós com `document.createElement` +
  `textContent` para os campos de dado.
- Para os botões (que precisam de marcação), construir os elementos e ligar handlers via
  `addEventListener` (em vez de `onclick="...(${id})"` inline), passando o `id` por closure/dataset.
- Centralizar uma função `criarCelulaTexto(valor)` para reuso.
- (Opcional) `criarCelulaTexto` cobre também `paginaDespesa.js` se houver render de dados lá.

## 5. Plano de teste

Validação manual documentada (e, se houver Jest/Vitest da spec-002, um teste de unidade):

| # | Passo | Esperado |
|---|-------|----------|
| 1 | Criar despesa com `descricao = <img src=x onerror=alert(1)>` | aparece como texto, **sem** alert |
| 2 | Criar categoria com nome contendo `<b>x</b>` | aparece literal, sem negrito |

> Se a spec-002 introduzir Jest/Vitest, adicionar teste: `renderizarDespesas` com payload
> malicioso não produz nós `<img>`/`<script>` no DOM.

## 6. Fora de escopo

- Sanitização no back-end (defesa em profundidade) — pode virar spec própria.
- Content-Security-Policy headers (configuração de hospedagem).

## 7. Riscos / observações

- Corrige um pré-requisito de segurança para guardar o token em `localStorage` (spec-002).
- Esta spec vive no repositório **despesas-front**; abrir o PR lá.
