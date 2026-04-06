package com.oracle.OSfacil.dto.response;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoResponseDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer quantidade;
}
