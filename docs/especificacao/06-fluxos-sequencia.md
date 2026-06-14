# Fluxos — Diagramas de Sequência

## 1. Registro de usuário (`POST /auth/register`)

```mermaid
sequenceDiagram
    actor U as Usuário
    participant AC as AuthController
    participant US as UsuarioService
    participant PE as PasswordEncoder (BCrypt)
    participant UR as UsuarioRepository
    participant DB as MySQL

    U->>AC: POST /auth/register {email, senha}
    AC->>US: criarUsuario(LoginRequest)
    US->>PE: encode(senha)
    PE-->>US: hash BCrypt
    US->>UR: save(usuario)
    UR->>DB: INSERT usuarios
    alt e-mail duplicado
        DB-->>UR: violação UNIQUE
        UR-->>US: DataIntegrityViolationException
        US-->>AC: (propaga)
        AC-->>U: 409 "Email já cadastrado."
    else ok
        DB-->>UR: id
        US-->>AC: Usuario (senha @JsonIgnore)
        AC-->>U: 201 Created {id, email}
    end
```

## 2. Login e emissão de JWT (`POST /auth/login`)

```mermaid
sequenceDiagram
    actor U as Usuário
    participant AC as AuthController
    participant US as UsuarioService
    participant UR as UsuarioRepository
    participant PE as PasswordEncoder
    participant JS as JwtService

    U->>AC: POST /auth/login {email, senha}
    AC->>US: login(LoginRequest)
    US->>UR: findByEmail(email)
    alt não encontrado ou senha errada
        US-->>AC: InvalidCredentialsException
        AC-->>U: 401 "Email ou senha Inválidos"
    else credenciais ok
        US->>PE: matches(senha, hash)
        PE-->>US: true
        US->>JS: gerarToken(email)
        JS-->>US: JWT (HS256, exp 24h)
        US-->>AC: LoginResponse{token}
        AC-->>U: 200 {token}
    end
```

## 3. Request autenticado — filtro JWT (qualquer rota protegida)

```mermaid
sequenceDiagram
    actor U as Usuário
    participant F as JwtAuthFilter
    participant JS as JwtService
    participant US as UsuarioService
    participant SC as SecurityContext
    participant C as Controller protegido

    U->>F: GET /despesas (Authorization: Bearer <jwt>)
    alt sem header / não "Bearer "
        F->>C: segue sem autenticar
        Note over C: anyRequest().authenticated() → 401
    else com token
        F->>JS: extrairEmail(token)
        alt token inválido/expirado
            JS-->>F: lança exceção (capturada)
            F->>C: segue sem autenticar → 401
        else token válido
            JS-->>F: email
            F->>US: loadUserByUsername(email)
            US-->>F: UserDetails (Usuario)
            F->>SC: setAuthentication(...)
            F->>C: prossegue autenticado
            C-->>U: 200 + dados
        end
    end
```

## 4. Criar despesa (`POST /despesas`)

```mermaid
sequenceDiagram
    actor U as Usuário
    participant DC as DespesaController
    participant DS as DespesaService
    participant SC as SecurityContext
    participant CR as CategoriaRepository
    participant DR as DespesaRepository

    U->>DC: POST /despesas (Bearer + DespesaRequest)
    DC->>DS: criarDespesa(req)
    DS->>CR: findById(categoriaId)
    alt categoria inexistente
        DS-->>DC: IllegalArgumentException
        DC-->>U: 422 "Categoria não encontrada"
    else ok
        DS->>SC: getAuthentication().getPrincipal()
        SC-->>DS: Usuario autenticado
        DS->>DS: validarDespesa() (valor>0, data<=hoje, descricao)
        alt inválida
            DS-->>U: 422 mensagem da regra
        else válida
            DS->>DR: save(despesa + usuario)
            DR-->>DS: Despesa
            DS-->>DC: DespesaResponse
            DC-->>U: 201 Created
        end
    end
```
