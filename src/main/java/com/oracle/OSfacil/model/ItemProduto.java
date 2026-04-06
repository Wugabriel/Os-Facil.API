package com.oracle.OSfacil.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "tb_itemproduto")
public class ItemProduto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE ,generator = "itemProduto_seq_generator")
    @SequenceGenerator(name = "itemProduto_seq_generator",sequenceName = "itemProduto_generator",allocationSize = 1 )
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "ordem_servico_id")
    private OrdemServico ordemServico;
    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;
    @Column(nullable = false)
    private Integer quantidade;
    @Column(nullable = false,name = "valor_unitario")
    private BigDecimal valorUnitario;
    @Column(nullable = false)
    private BigDecimal subtotal;
}
