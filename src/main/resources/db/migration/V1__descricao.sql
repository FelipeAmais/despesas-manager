CREATE table usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    senha VARCHAR(255)
);

CREATE table categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255)
);

CREATE table despesas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    descricao VARCHAR(255),
    valor decimal(10,2),
    data date,
    categoria_id BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);