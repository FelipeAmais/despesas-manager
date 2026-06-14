# spec-002 — Login e envio de JWT no front

- **Prioridade:** 🔴 Crítica
- **Alvo:** front-end (despesas-front) — *repositório separado*
- **Status:** Proposta
- **Relacionada:** [02-containers-c4](../especificacao/02-containers-c4.md), [spec-001](spec-001-ownership-despesas.md)

## 1. Problema / contexto

O back-end passou a exigir JWT (`anyRequest().authenticated()`), mas o front (`js/script.js`,
`js/paginaDespesa.js`, `js/novaCategoria.js`) faz `fetch` **sem o header `Authorization`** e
**não tem tela de login**. Hoje, toda chamada a `/despesas` e `/categorias` retorna **401** →
o app não funciona ponta-a-ponta.

## 2. Objetivo

O usuário se registra/loga, o front guarda o token e o envia em todas as chamadas protegidas;
sem token válido, é redirecionado para o login.

## 3. Critérios de aceite

- [ ] Existe uma tela de **login** (e registro) que chama `POST /auth/login` e guarda o token.
- [ ] Todas as chamadas a `/despesas` e `/categorias` enviam `Authorization: Bearer <token>`.
- [ ] Resposta **401** em qualquer chamada → o front limpa o token e redireciona ao login.
- [ ] Logout limpa o token armazenado.
- [ ] A URL base da API fica em **um único** ponto de configuração (não repetida em cada arquivo).

## 4. Design proposto

- `auth.js` (novo): `login(email, senha)`, `register(...)`, `getToken()`, `logout()`,
  guardando o token em `localStorage` (chave `despesas_token`).
- `api.js` (novo): wrapper `apiFetch(path, options)` que injeta o header `Authorization`,
  resolve a URL base de uma constante única e trata 401 (limpa token + `redirect` ao login).
- `login.html` (novo): formulário de login/registro.
- Refatorar `script.js`, `paginaDespesa.js`, `novaCategoria.js` para usar `apiFetch`.

## 5. Plano de teste

Front sem suíte automatizada hoje; validar via **smoke test manual** documentado:

| # | Passo | Esperado |
|---|-------|----------|
| 1 | Abrir o app sem token | redireciona para `login.html` |
| 2 | Registrar + logar | token salvo; vai para a lista |
| 3 | Criar/listar despesa | requisições com `Bearer`; 200 |
| 4 | Apagar o token e recarregar | volta ao login (tratamento do 401) |

> Recomendado nesta spec: introduzir um teste mínimo com Jest/Vitest para `apiFetch`
> (mockando `fetch`) — opcional, mas destrava testes no front.

## 6. Fora de escopo

- Refresh token (vira spec própria).
- Redesenho visual das telas.

## 7. Riscos / observações

- `localStorage` é vulnerável a XSS — depende de [spec-005](spec-005-saneamento-xss-front.md) estar feita.
- Alinhar mensagens/status de erro com a padronização da [spec-001](spec-001-ownership-despesas.md).
- Esta spec vive no repositório **despesas-front**; abrir o PR lá.
