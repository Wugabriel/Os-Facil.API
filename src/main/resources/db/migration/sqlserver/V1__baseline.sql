CREATE TABLE tb_cliente (
    id BIGINT IDENTITY(1,1) NOT NULL,
    cpf NVARCHAR(50) NOT NULL UNIQUE,
    telefone NVARCHAR(50) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE,
    endereco NVARCHAR(255) NOT NULL,
    nome NVARCHAR(255) NOT NULL,
    role NVARCHAR(255) CHECK (role IN ('ROLE_CLIENTE','ROLE_FUNCIONARIO','ROLE_ADMIN')),
    senha NVARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tb_funcionario (
    salario DECIMAL(38,2) NOT NULL,
    id BIGINT IDENTITY(1,1) NOT NULL,
    cpf NVARCHAR(50) NOT NULL,
    email NVARCHAR(255) NOT NULL UNIQUE,
    login NVARCHAR(255) NOT NULL UNIQUE,
    nome NVARCHAR(255) NOT NULL,
    role NVARCHAR(255) CHECK (role IN ('ROLE_CLIENTE','ROLE_FUNCIONARIO','ROLE_ADMIN')),
    senha NVARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tb_produto (
    preco DECIMAL(38,2) NOT NULL,
    quantidade INT NOT NULL,
    id BIGINT IDENTITY(1,1) NOT NULL,
    nome NVARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tb_ordemservico (
    valor DECIMAL(38,2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    id BIGINT IDENTITY(1,1) NOT NULL,
    descricao NVARCHAR(255) NOT NULL,
    status_ordem_servico NVARCHAR(255) NOT NULL CHECK (status_ordem_servico IN ('ABERTA','CONCLUIDA','CANCELADA','EM_ANDAMENTO')),
    status_pagamento NVARCHAR(255) NOT NULL CHECK (status_pagamento IN ('PENDENTE','PAGO','CANCELADO')),
    PRIMARY KEY (id)
);

CREATE TABLE tb_pagamento (
    valor DECIMAL(38,2) NOT NULL,
    cliente_id BIGINT,
    id BIGINT IDENTITY(1,1) NOT NULL,
    forma_pagamento NVARCHAR(50) NOT NULL CHECK (forma_pagamento IN ('PIX','CARTAO_CREDITO','CARTAO_DEBITO','DINHEIRO')),
    PRIMARY KEY (id)
);

CREATE TABLE tb_veiculo (
    ano INT NOT NULL CHECK ((ano>=1886) AND (ano<=2050)),
    cliente_id BIGINT NOT NULL,
    id BIGINT IDENTITY(1,1) NOT NULL,
    placa NVARCHAR(10) NOT NULL UNIQUE,
    cor NVARCHAR(30) NOT NULL,
    marca NVARCHAR(100) NOT NULL,
    modelo NVARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tb_itemproduto (
    quantidade INT NOT NULL,
    subtotal DECIMAL(38,2) NOT NULL,
    valor_unitario DECIMAL(38,2) NOT NULL,
    id BIGINT IDENTITY(1,1) NOT NULL,
    ordem_servico_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE tb_itemproduto ADD CONSTRAINT FK6rstmcwqom2tqxsr4i0ebbnfp FOREIGN KEY (ordem_servico_id) REFERENCES tb_ordemservico(id);
ALTER TABLE tb_itemproduto ADD CONSTRAINT FKqudgjmvt3sn6s6fkoromrwf79 FOREIGN KEY (produto_id) REFERENCES tb_produto(id);
ALTER TABLE tb_ordemservico ADD CONSTRAINT FKfnuuca5gipekth26oggyruov6 FOREIGN KEY (cliente_id) REFERENCES tb_cliente(id);
ALTER TABLE tb_pagamento ADD CONSTRAINT FK2jijy17ebaheag5rvwmdb0td5 FOREIGN KEY (cliente_id) REFERENCES tb_cliente(id);
ALTER TABLE tb_veiculo ADD CONSTRAINT FKrv2dkbho7roxmjk3w9vya2r2c FOREIGN KEY (cliente_id) REFERENCES tb_cliente(id);
