ALTER TABLE tb_ordemservico ADD funcionario_id NUMBER(19,0);

ALTER TABLE tb_ordemservico
    ADD CONSTRAINT fk_ordemservico_funcionario
    FOREIGN KEY (funcionario_id)
    REFERENCES tb_funcionario(id);
