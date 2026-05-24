IF NOT EXISTS (SELECT 1 FROM tb_funcionario WHERE login = 'admin' OR cpf = '00000000000' OR email = 'admin@osfacil.com')
BEGIN
    INSERT INTO tb_funcionario (nome, cpf, email, login, senha, salario, role)
    VALUES ('Admin', '00000000000', 'admin@osfacil.com', 'admin',
            '\\\',
            5000, 'ROLE_ADMIN');
END
