# Topologia de Implantação

```mermaid
flowchart TB
    subgraph Cliente["💻 Dispositivo do usuário"]
        BR[Navegador]
    end

    subgraph StaticHost["🌐 Hospedagem estática (front)"]
        FE["despesas-front<br/>index.html / Despesa.html / criarCategoria.html<br/>+ CSS / JS / Bootstrap (CDN)"]
    end

    subgraph Railway["☁️ Railway (produção)"]
        API["despesas-manager<br/>JAR Spring Boot (Tomcat embarcado)<br/>PORT (env)"]
        DB[("MySQL 8")]
    end

    BR -->|HTTPS| FE
    BR -->|"HTTPS / JSON<br/>dispesas-manager-production.up.railway.app"| API
    API -->|JDBC| DB
```

## Ambientes e configuração

A API é configurada por variáveis de ambiente (`application.properties`), com defaults para desenvolvimento local:

| Variável | Default (dev) | Uso |
|----------|---------------|-----|
| `PORT` | `8080` | Porta HTTP |
| `DATABASE_URL` | `jdbc:mysql://localhost:3306/mydb` | Conexão JDBC |
| `DATABASE_USERNAME` | `root` | Usuário do banco |
| `DATABASE_PASSWORD` | *(vazio)* | Senha do banco |
| `JWT_SECRET` | `minha-chave-local-de-desenvolvimento` | Chave HS256 (⚠️ precisa ≥ 32 bytes) |

## Notas de implantação

- **Build:** Maven wrapper (`./mvnw`) → JAR executável (`spring-boot-maven-plugin`).
- **Banco:** `spring.jpa.hibernate.ddl-auto=validate` — a aplicação **não cria nem altera** o schema. 🟠 Um banco novo no Railway falha ao subir sem o schema pré-existente; não há Flyway/Liquibase.
- **Front:** a hospedagem exata não está versionada nos repositórios (provável GitHub Pages ou similar). A URL da API está fixa no código JS.
- **DevTools:** `spring-boot-devtools` está no classpath (escopo runtime/optional) — irrelevante em produção.
- **Swagger UI:** `springdoc-openapi` está no `pom.xml` → disponível em `/swagger-ui.html` (apesar de o README listar Swagger como "futuro").
