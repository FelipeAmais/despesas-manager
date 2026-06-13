# C4 — Nível 3: Diagrama de Componentes (API)

Detalha o interior do container **despesas-manager** (pacote `com.felipe.despesas`),
organizado em camadas.

```mermaid
C4Component
    title Componentes — API despesas-manager

    Container_Boundary(api, "despesas-manager") {

        Component(filter, "JwtAuthFilter", "OncePerRequestFilter", "Lê 'Authorization: Bearer', valida o token e popula o SecurityContext")
        Component(sec, "SecurityConfig", "@Configuration", "Filter chain stateless; /auth/** público, resto autenticado")
        Component(geh, "GlobalExceptionHandler", "@ControllerAdvice", "Mapeia exceções para 401/409/422")

        Component(authc, "AuthController", "@RestController /auth", "register, login")
        Component(despc, "DespesaController", "@RestController /despesas", "CRUD de despesas")
        Component(catc, "CategoriaController", "@RestController /categorias", "CRUD de categorias")

        Component(usrs, "UsuarioService", "@Service / UserDetailsService", "Cria usuário, login, carrega UserDetails")
        Component(jwts, "JwtService", "@Service", "Gera e lê tokens JWT (HS256)")
        Component(desps, "DespesaService", "@Service", "Regras de despesa; vincula ao usuário autenticado")
        Component(cats, "CategoriaService", "@Service", "Regras de categoria; nome único")

        ComponentDb(usrr, "UsuarioRepository", "JpaRepository", "findByEmail")
        ComponentDb(despr, "DespesaRepository", "JpaRepository", "findByUsuario")
        ComponentDb(catr, "CategoriaRepository", "JpaRepository", "findByNome")
    }

    ContainerDb(db, "MySQL 8", "Banco", "")

    Rel(filter, jwts, "valida token via")
    Rel(filter, usrs, "carrega usuário via")
    Rel(sec, filter, "registra")

    Rel(authc, usrs, "usa")
    Rel(despc, desps, "usa")
    Rel(catc, cats, "usa")

    Rel(usrs, jwts, "gera token via")
    Rel(usrs, usrr, "usa")
    Rel(desps, despr, "usa")
    Rel(desps, catr, "valida categoria via")
    Rel(cats, catr, "usa")

    Rel(usrr, db, "JDBC")
    Rel(despr, db, "JDBC")
    Rel(catr, db, "JDBC")
```

## Camadas e componentes

### `config` — Segurança / infraestrutura
| Componente | Papel |
|-----------|-------|
| `SecurityConfig` | Filter chain: CSRF off, sessão STATELESS, `/auth/**` liberado, `anyRequest().authenticated()`, entry point devolve 401. |
| `JwtAuthFilter` | Para cada request, extrai o Bearer token, recupera o e-mail, carrega `UserDetails` e autentica no `SecurityContextHolder`. Exceções são silenciadas → segue sem autenticar (cai em 401). |
| `PasswordConfig` | Expõe o bean `PasswordEncoder` = `BCryptPasswordEncoder`. |

### `controller` — Borda HTTP
| Componente | Base path | Endpoints |
|-----------|-----------|-----------|
| `AuthController` | `/auth` | `POST /register`, `POST /login` (públicos) |
| `DespesaController` | `/despesas` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| `CategoriaController` | `/categorias` | `GET`, `POST`, `PUT`, `DELETE /{id}` |

### `services` — Regras de negócio
| Componente | Responsabilidade |
|-----------|------------------|
| `UsuarioService` (`UserDetailsService`) | `criarUsuario`, `login` (verifica BCrypt + emite token), `loadUserByUsername`. ⚠️ `validarUsuario()` está vazio. |
| `JwtService` | `gerarToken` (subject=email, exp 24h, HS256) e `extrairEmail`. |
| `DespesaService` | CRUD + `validarDespesa` (valor>0, data não futura, descrição não vazia). Pega o usuário via `SecurityContextHolder`. 🔴 ownership só checado no `listar`. |
| `CategoriaService` | CRUD + unicidade de `nome`. Categorias são globais (sem vínculo a usuário). |

### `repository` — Acesso a dados (Spring Data JPA)
`UsuarioRepository.findByEmail`, `DespesaRepository.findByUsuario`, `CategoriaRepository.findByNome` — derivados de query, sem JPQL manual.

### `exception`
`GlobalExceptionHandler` (`@ControllerAdvice`) + `InvalidCredentialsException`.

## 🔴 Anotação de segurança no fluxo de despesas

```mermaid
flowchart TD
    A[DespesaController] --> B{Método}
    B -->|GET / listar| C["listarDespesas()<br/>findByUsuario(autenticado) ✅"]
    B -->|"GET /{id}"| D["buscarPorId(id)<br/>findById(id) 🔴 sem dono"]
    B -->|"PUT /{id}"| E["atualizarDespesa(id)<br/>findById(id) 🔴 sem dono"]
    B -->|"DELETE /{id}"| F["excluirDespesa(id)<br/>existsById(id) 🔴 sem dono"]
    style C fill:#e6ffe6,stroke:#2a2
    style D fill:#ffe5e5,stroke:#d33
    style E fill:#ffe5e5,stroke:#d33
    style F fill:#ffe5e5,stroke:#d33
```
