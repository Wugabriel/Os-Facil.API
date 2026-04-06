package com.oracle.OSfacil.dto.request;

import com.oracle.OSfacil.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Data
public class FuncionarioDTO {
    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;
    @NotBlank(message = "O CPF não pode estar vazio")
    @CPF(message = "cpf inválido")
    private String cpf;
    @NotBlank(message = "O email não pode ser vazio")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Email deve estar em um formato válido"
    )
    private String email;
    @NotNull(message = "O salário não pode ser nulo")
    @Positive
    private BigDecimal salario;
    @NotBlank(message = "O login não pode ser vazio")
    private String login;
    @NotBlank(message = "A senha não pode estar vazio")
    private String senha;
    @NotNull(message = "O cargo não pode ser vazio")
    private Role role;
}
