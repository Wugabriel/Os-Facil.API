package com.oracle.OSfacil.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemProdutoDTO {
    @NotNull(message = "O Id da ordem do serviço não pode ser vazio")
    private Long ordemServicoId;
    @NotNull(message = "O Id do produto não pode ser vazio")
    private Long produtoId;
    @NotNull(message = "A quantidade não pode ser nula")
    @Positive
    private Integer quantidade;
    @NotNull(message = "O valor unitário não pode ser nulo")
    @Positive
    private BigDecimal valorUnitario;

}
