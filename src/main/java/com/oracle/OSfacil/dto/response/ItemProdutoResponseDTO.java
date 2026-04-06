package com.oracle.OSfacil.dto.response;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemProdutoResponseDTO {
    private Long id;
    private Long produtoId;
    private Long ordemServicoId;
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal subtotal;

}
