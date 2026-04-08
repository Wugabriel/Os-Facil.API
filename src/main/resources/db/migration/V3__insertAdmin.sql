INSERT INTO tb_funcionario (id, nome, cpf, email, login, senha, salario, "ROLE")
VALUES (
  funcionario_generator.NEXTVAL,
  'Admin',
  '00000000000',
  'admin@osfacil.com',
  'admin',
  '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
  5000,
  'ROLE_ADMIN'
);