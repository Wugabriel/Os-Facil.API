package com.oracle.OSfacil.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class ProdutoDTO {
    @NotBlank(message = "O nome não pode ser vazio")
    private String nome;
    @NotNull(message = "O preço não pode ser nulo")
    @Positive
    private BigDecimal preco;
    @NotNull(message = "A quantidade não pode ser vazio")
    @Min(value = 0, message = "O estoque não pode ser negativo")
    private Integer quantidade;
}

