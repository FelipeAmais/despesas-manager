# Modelo de Domínio

## Diagrama de classes (entidades JPA + DTOs)

```mermaid
classDiagram
    class Usuario {
        +Long id
        +String nome
        +String email
        -String senha
        +getAuthorities()
        +isEnabled()
    }
    class Despesa {
        +Long id
        +String descricao
        +BigDecimal valor
        +LocalDate data
    }
    class Categoria {
        +Long id
        +String nome
    }

    class DespesaRequest {
        +String descricao
        +BigDecimal valor
        +LocalDate data
        +Long categoriaId
    }
    class DespesaResponse {
        +Long id
        +String descricao
        +BigDecimal valor
        +LocalDate data
        +Categoria categoria
    }
    class LoginRequest {
        +String email
        +String senha
    }
    class LoginResponse {
        +String token
    }

    Usuario "1" --> "0..*" Despesa : possui
    Categoria "1" --> "0..*" Despesa : classifica
    Usuario ..|> UserDetails : implements

    DespesaRequest ..> Despesa : mapeia p/
    Despesa ..> DespesaResponse : mapeia p/
    note for Usuario "implements UserDetails\n(Spring Security)"
```

- `Usuario` **é** um `UserDetails` (acopla domínio e Spring Security — funciona, mas mistura responsabilidades).
- `Despesa` referencia `Categoria` (`@ManyToOne`, `EAGER`, `categoria_id NOT NULL`) e `Usuario` (`@ManyToOne`).
- DTOs isolam a borda HTTP do modelo — exceto `DespesaResponse`, que ainda **expõe a entidade `Categoria`** inteira.

## Modelo entidade-relacionamento (tabelas)

```mermaid
erDiagram
    USUARIOS ||--o{ DESPESAS : "possui"
    CATEGORIAS ||--o{ DESPESAS : "classifica"

    USUARIOS {
        bigint id PK
        varchar nome
        varchar email UK
        varchar senha "BCrypt"
    }
    CATEGORIAS {
        bigint id PK
        varchar nome
    }
    DESPESAS {
        bigint id PK
        varchar descricao
        decimal valor
        date data
        bigint categoria_id FK "NOT NULL"
        bigint usuario_id FK
    }
```

### Observações de modelagem
- `valor` usa `BigDecimal` (mapeado para `DECIMAL`) — correto para dinheiro (corrigido do `Double` no PR #11).
- `CATEGORIAS` não tem `usuario_id` → categorias são **compartilhadas entre todos os usuários**.
- `usuarios.email` é único; `usuarios.nome` existe mas nunca é preenchido no fluxo de registro.
- `despesas.usuario_id` não está marcado `NOT NULL` no código (`@ManyToOne` sem `@JoinColumn(nullable=false)`).
