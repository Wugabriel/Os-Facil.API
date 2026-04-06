package com.oracle.OSfacil.dto.response;


import com.oracle.OSfacil.enums.FormaPagamento;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoResponseDTO {
    private Long id;
    private BigDecimal valor;
    private FormaPagamento formaPagamento;
    private Long clienteId;
    private String nomeCliente;
}
