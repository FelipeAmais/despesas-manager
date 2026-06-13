# Frameworks, Bibliotecas e Decisões Arquiteturais

## Back-end — `despesas-manager`

| Tecnologia | Versão | Papel |
|-----------|--------|-------|
| Java | 21 | Linguagem |
| Spring Boot | 3.5.3 | Framework base (parent POM) |
| Spring Web (MVC) | (gerenciada) | Endpoints REST, Tomcat embarcado |
| Spring Security | (gerenciada) | Filter chain, autenticação stateless |
| Spring Data JPA | (gerenciada) | Repositórios + Hibernate ORM |
| Hibernate | (gerenciada) | Mapeamento objeto-relacional |
| JJWT (jjwt-api/impl/jackson) | 0.12.3 | Geração/validação de JWT (HS256) |
| BCryptPasswordEncoder | (Spring Security) | Hash de senha |
| MySQL Connector/J | (gerenciada) | Driver JDBC MySQL 8 |
| springdoc-openapi-starter-webmvc-ui | 2.5.0 | Swagger UI / OpenAPI |
| Lombok | (gerenciada) | `@Data`, `@AllArgsConstructor` etc. |
| Spring Boot DevTools | (gerenciada) | Hot reload em dev |
| spring-boot-starter-test (JUnit 5) | (gerenciada) | Testes (só `contextLoads()`) |
| Maven (wrapper) | — | Build |

## Front-end — `despesas-front`

| Tecnologia | Papel |
|-----------|-------|
| HTML5 | Estrutura das páginas |
| CSS3 | Estilo (`css/style.css`) |
| JavaScript ES6+ | Lógica (`script.js`, `paginaDespesa.js`, `novaCategoria.js`) |
| Fetch API | Comunicação com a API |
| Bootstrap + Bootstrap Icons | UI/estilização (classes `btn`, `bi bi-*`) |

## Decisões arquiteturais (e o porquê)

| Decisão | Avaliação |
|---------|-----------|
| **Arquitetura em camadas** (controller/service/repository/model/dto) | ✅ Separação clara, injeção por construtor. |
| **Autenticação JWT stateless** (sem sessão) | ✅ Adequado para API REST; escala horizontalmente. |
| **BCrypt** para senhas | ✅ Padrão de mercado. |
| **DTOs** na borda (`DespesaRequest/Response`, `Login*`) | ✅ Bom — embora `DespesaResponse` ainda exponha a entidade `Categoria`. |
| **`BigDecimal`** para valores | ✅ Correto para dinheiro. |
| **`ddl-auto=validate`** | ✅ Não deixa o ORM mexer no schema em prod — porém exige migração externa (ver lacuna). |
| **Configuração por env vars com defaults** | ✅ 12-factor friendly. |
| **`Usuario implements UserDetails`** | 🟡 Acopla domínio e segurança; comum em projetos didáticos, mas idealmente separado. |
| **Exceções de negócio via `IllegalArgumentException` → 422** | 🟡 Funciona, mas exceções específicas dariam respostas mais semânticas. |

## 🔴 Lacunas conhecidas

Consolidação das divergências entre a arquitetura documentada e a implementação atual
(detalhes nos diagramas correspondentes):

| Severidade | Lacuna | Onde | Impacto |
|-----------|--------|------|---------|
| 🔴 Crítico | **IDOR** — `GET/PUT/DELETE /despesas/{id}` usam `findById`/`existsById` sem checar o dono | `DespesaService` | Qualquer usuário autenticado lê/edita/apaga despesas de outros. |
| 🔴 Crítico | **Front sem JWT** — a SPA não envia `Authorization: Bearer` | `despesas-front/js/*` | App ponta-a-ponta retorna 401; não funciona contra a API protegida. |
| 🟠 Alto | **Sem validação no registro** — `validarUsuario()` vazio, sem Bean Validation | `UsuarioService`, DTOs | Aceita e-mail inválido/senha em branco. |
| 🟠 Alto | **Sem migrations** — `ddl-auto=validate` sem Flyway/Liquibase | infra | Deploy falha em banco novo. |
| 🟠 Alto | **XSS armazenado** — `innerHTML` com dados da API | `script.js` | Descrição com HTML/script executa no navegador. |
| 🟡 Médio | **CORS `*`** sem configuração no Security | `@CrossOrigin`, `SecurityConfig` | Preflight pode ser bloqueado; política permissiva. |
| 🟡 Médio | **JWT weak key** — `JWT_SECRET` curto quebra HS256 (jjwt 0.12) | `JwtService` | Login falha se a chave de prod < 32 bytes. |
| 🟡 Médio | **`register` retorna a entidade `Usuario`** | `AuthController` | Acoplamento; só seguro por causa do `@JsonIgnore`. |
| 🟡 Médio | **Categorias globais** (sem `usuario_id`) | `Categoria`, `CategoriaService` | Todos compartilham e editam as mesmas categorias. |
| 🟢 Baixo | Campo `nome` do usuário nunca preenchido; só `contextLoads()` de teste | vários | Dívida técnica menor. |

### Backlog do próprio autor (READMEs)

- Back-end: validação de usuários, Swagger (já no pom), testes unitários, Docker, refresh token, filtros por período/categoria, recuperação de senha.
- Front: validação avançada, feedback visual, edição (já feita), filtro/busca, paginação, gráficos (Chart.js/D3), autenticação, responsividade, i18n.
