package com.oracle.OSfacil.dto.response;


import com.oracle.OSfacil.enums.StatusOrdemServico;
import com.oracle.OSfacil.enums.StatusPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrdemServicoResponseDTO {
    private Long id;
    private Long clienteId;
    private String nomeCliente;
    private StatusOrdemServico statusOrdemServico;
    private String descricao;
    private StatusPagamento statusPagamento;
    private BigDecimal valor;
    private List<ItemProdutoResponseDTO> itens;

}
