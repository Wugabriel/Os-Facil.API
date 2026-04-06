package com.oracle.OSfacil.mapper;

import com.oracle.OSfacil.dto.request.ProdutoDTO;
import com.oracle.OSfacil.dto.response.ProdutoResponseDTO;
import com.oracle.OSfacil.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public ProdutoResponseDTO toResponseDTO(Produto produto) {

        ProdutoResponseDTO dto = new ProdutoResponseDTO();

        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setPreco(produto.getPreco());
        dto.setQuantidade(produto.getQuantidade());

        return dto;
    }

    public Produto toEntity(ProdutoDTO dto) {

        Produto produto = new Produto();

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());

        return produto;
    }
}
