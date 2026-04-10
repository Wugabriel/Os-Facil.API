MERGE INTO tb_funcionario t
USING (
    SELECT
        '00000000000' AS cpf,
        'admin@osfacil.com' AS email,
        'admin' AS login
    FROM dual
) s
ON (t.login = s.login OR t.cpf = s.cpf OR t.email = s.email)
WHEN NOT MATCHED THEN
    INSERT (id, nome, cpf, email, login, senha, salario, "ROLE")
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