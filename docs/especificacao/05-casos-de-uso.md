# Casos de Uso

## Mapa de casos de uso

```mermaid
flowchart LR
    U([👤 Usuário])

    subgraph Publico["Público (sem token)"]
        UC1[UC01 Registrar usuário]
        UC2[UC02 Autenticar / obter JWT]
    end

    subgraph Autenticado["Autenticado (JWT)"]
        UC3[UC03 Criar despesa]
        UC4[UC04 Listar minhas despesas]
        UC5[UC05 Ver despesa por id]
        UC6[UC06 Atualizar despesa]
        UC7[UC07 Excluir despesa]
        UC8[UC08 Criar categoria]
        UC9[UC09 Listar categorias]
        UC10[UC10 Atualizar categoria]
        UC11[UC11 Excluir categoria]
    end

    U --> UC1 & UC2
    U --> UC3 & UC4 & UC5 & UC6 & UC7
    U --> UC8 & UC9 & UC10 & UC11

    style UC5 fill:#ffe5e5,stroke:#d33
    style UC6 fill:#ffe5e5,stroke:#d33
    style UC7 fill:#ffe5e5,stroke:#d33
```

> 🔴 UC05/UC06/UC07 estão em vermelho: hoje operam sobre **qualquer** despesa por id, sem verificar o dono.

## Mapa endpoint × método × autenticação

| UC | Método | Endpoint | Auth | Sucesso | Erros tratados |
|----|--------|----------|------|---------|----------------|
| UC01 | POST | `/auth/register` | ❌ público | 201 | 409 e-mail duplicado |
| UC02 | POST | `/auth/login` | ❌ público | 200 + token | 401 credenciais inválidas |
| UC03 | POST | `/despesas` | ✅ JWT | 201 | 422 validação |
| UC04 | GET | `/despesas` | ✅ JWT | 200 (só do usuário) | — |
| UC05 | GET | `/despesas/{id}` | ✅ JWT | 200 / 404 | 🔴 sem checagem de dono |
| UC06 | PUT | `/despesas/{id}` | ✅ JWT | 200 | 422; 🔴 sem checagem de dono |
| UC07 | DELETE | `/despesas/{id}` | ✅ JWT | 204 | 422 inexistente; 🔴 sem checagem de dono |
| UC08 | POST | `/categorias` | ✅ JWT | 201 | 422 nome duplicado |
| UC09 | GET | `/categorias` | ✅ JWT | 200 | — |
| UC10 | PUT | `/categorias` | ✅ JWT | 200 | 422 inexistente/duplicado |
| UC11 | DELETE | `/categorias/{id}` | ✅ JWT | 204 | 422 inexistente |

## Regras de negócio (UC03/UC06 — Despesa)

`DespesaService.validarDespesa()`:
- `valor` deve ser **> 0**;
- `data` **não** pode ser futura;
- `descricao` não pode ser vazia/em branco.

## Regras de negócio (UC08/UC10 — Categoria)

- `nome` deve ser **único** (validado na criação e na atualização).
