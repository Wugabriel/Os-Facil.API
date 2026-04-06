package com.oracle.OSfacil.mapper;

import com.oracle.OSfacil.dto.request.ItemProdutoDTO;
import com.oracle.OSfacil.dto.response.ItemProdutoResponseDTO;
import com.oracle.OSfacil.model.ItemProduto;
import com.oracle.OSfacil.model.OrdemServico;
import com.oracle.OSfacil.model.Produto;
import com.oracle.OSfacil.repository.OrdemServicoRepository;
import com.oracle.OSfacil.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemProdutoMapper {

    private final ProdutoRepository produtoRepository;
    private final OrdemServicoRepository ordemServicoRepository;

    public ItemProdutoResponseDTO toResponseDTO(ItemProduto itemProduto) {

        ItemProdutoResponseDTO dto = new ItemProdutoResponseDTO();

        dto.setId(itemProduto.getId());
        dto.setQuantidade(itemProduto.getQuantidade());
        dto.setValorUnitario(itemProduto.getValorUnitario());
        dto.setSubtotal(itemProduto.getSubtotal());

        if (itemProduto.getProduto() != null) {
            dto.setProdutoId(itemProduto.getProduto().getId());
            dto.setNomeProduto(itemProduto.getProduto().getNome());
        }

        if (itemProduto.getOrdemServico() != null) {
            dto.setOrdemServicoId(itemProduto.getOrdemServico().getId());
        }

        return dto;
    }

    public ItemProduto toEntity(ItemProdutoDTO dto) {

        ItemProduto itemProduto = new ItemProduto();

        itemProduto.setQuantidade(dto.getQuantidade());
        itemProduto.setValorUnitario(dto.getValorUnitario());

        if (dto.getProdutoId() != null) {
            Produto produto = produtoRepository.findById(dto.getProdutoId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
            itemProduto.setProduto(produto);
        }

        if (dto.getOrdemServicoId() != null) {
            OrdemServico ordemServico = ordemServicoRepository.findById(dto.getOrdemServicoId())
                    .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada"));
            itemProduto.setOrdemServico(ordemServico);
        }

        return itemProduto;
    }
}


