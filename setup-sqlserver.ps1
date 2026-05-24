$base = "C:\Users\gabiw\Os-Facil.API"

# ── 1. pom.xml: adiciona mssql-jdbc e flyway-sqlserver ──────────────────────
$pom = Get-Content "$base\pom.xml" -Raw

$mssql = @"

        <dependency>
            <groupId>com.microsoft.sqlserver</groupId>
            <artifactId>mssql-jdbc</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.flywaydb</groupId>
            <artifactId>flyway-sqlserver</artifactId>
        </dependency>
"@

if ($pom -notmatch "mssql-jdbc") {
    $pom = $pom -replace "(<dependency>\s*<groupId>org\.flywaydb</groupId>\s*<artifactId>flyway-core</artifactId>)", "$mssql`n        `$1"
    Set-Content "$base\pom.xml" $pom -Encoding UTF8
    Write-Host "✅ pom.xml atualizado"
} else {
    Write-Host "⏭️  mssql-jdbc já existe no pom.xml"
}

# ── 2. application-sqlserver.yml ─────────────────────────────────────────────
$sqlserverYml = @"
spring:
  datasource:
    url: `${DATABASE_URL}
    username: `${DATABASE_USERNAME}
    password: `${DATABASE_PASSWORD}
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

  jpa:
    hibernate:
      ddl-auto: validate
    defer-datasource-initialization: false
    database-platform: org.hibernate.dialect.SQLServerDialect
    show-sql: true
    open-in-view: false

  flyway:
    enabled: true
    baseline-on-migrate: true
    baseline-version: 0
    locations: classpath:db/migration/sqlserver
"@
Set-Content "$base\src\main\resources\application-sqlserver.yml" $sqlserverYml -Encoding UTF8
Write-Host "✅ application-sqlserver.yml criado"

# ── 3. Migrations SQL Server ──────────────────────────────────────────────────
$migDir = "$base\src\main\resources\db\migration\sqlserver"
New-Item -ItemType Directory -Force -Path $migDir | Out-Null

# V1
Set-Content "$migDir\V1__baseline.sql" @"
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
"@ -Encoding UTF8

# V2
Set-Content "$migDir\V2__addRole.sql" @"
-- role ja incluido no V1 para SQL Server
-- no-op
SELECT 1;
"@ -Encoding UTF8

# V3
Set-Content "$migDir\V3__insertAdmin.sql" @"
IF NOT EXISTS (SELECT 1 FROM tb_funcionario WHERE login = 'admin' OR cpf = '00000000000' OR email = 'admin@osfacil.com')
BEGIN
    INSERT INTO tb_funcionario (nome, cpf, email, login, senha, salario, role)
    VALUES ('Admin', '00000000000', 'admin@osfacil.com', 'admin',
            '\$2a\$10\$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
            5000, 'ROLE_ADMIN');
END
"@ -Encoding UTF8

# V4
Set-Content "$migDir\V4__addDataCriacao.sql" @"
ALTER TABLE tb_ordemservico ADD data_criacao DATE;
"@ -Encoding UTF8

# V5
Set-Content "$migDir\V5__addCamposFuncionario.sql" @"
ALTER TABLE tb_funcionario ADD cargo NVARCHAR(100);
ALTER TABLE tb_funcionario ADD especialidade NVARCHAR(100);
ALTER TABLE tb_funcionario ADD telefone NVARCHAR(50);
"@ -Encoding UTF8

# V6
Set-Content "$migDir\V6__addFuncionarioOrdemServico.sql" @"
ALTER TABLE tb_ordemservico ADD funcionario_id BIGINT;
ALTER TABLE tb_ordemservico ADD CONSTRAINT fk_ordemservico_funcionario FOREIGN KEY (funcionario_id) REFERENCES tb_funcionario(id);
"@ -Encoding UTF8

Write-Host "✅ Migrations SQL Server criadas (V1-V6)"

# ── 4. azure-pipelines.yml ────────────────────────────────────────────────────
Set-Content "$base\azure-pipelines.yml" @"
trigger:
  branches:
    include:
      - main

stages:
  - stage: CI
    displayName: Build and Push
    jobs:
      - job: Build
        pool:
          vmImage: ubuntu-latest
        steps:
          - task: Maven@4
            displayName: Maven Test
            inputs:
              mavenPomFile: pom.xml
              goals: test
              publishJUnitResults: true
              testResultsFiles: '**/surefire-reports/TEST-*.xml'

          - task: Docker@2
            displayName: Build and Push to ACR
            inputs:
              containerRegistry: ACRConnection
              repository: osfacil
              command: buildAndPush
              Dockerfile: Dockerfile
              tags: latest

  - stage: CD
    displayName: Deploy to Azure
    dependsOn: CI
    condition: succeeded()
    jobs:
      - job: Deploy
        pool:
          vmImage: ubuntu-latest
        steps:
          - task: AzureWebAppContainer@1
            displayName: Deploy Container
            inputs:
              azureSubscription: AzureConnection
              appName: osfacil-webapp-mr560210
              containers: osfacilmr560210.azurecr.io/osfacil:latest
"@ -Encoding UTF8

Write-Host "✅ azure-pipelines.yml criado"
Write-Host ""
Write-Host "🎉 Setup completo! Próximo passo: git push"
