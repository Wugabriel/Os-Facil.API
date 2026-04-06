package com.oracle.OSfacil.service;

import com.oracle.OSfacil.dto.request.ProdutoDTO;
import com.oracle.OSfacil.dto.response.ProdutoResponseDTO;
import com.oracle.OSfacil.mapper.ProdutoMapper;
import com.oracle.OSfacil.model.Produto;
import com.oracle.OSfacil.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoResponseDTO criar(ProdutoDTO dto) {
        return produtoMapper.toResponseDTO(
                produtoRepository.save(produtoMapper.toEntity(dto))
        );
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Long id, ProdutoDTO dto) {
        Produto produto = buscarPorId(id);

        produto.setNome(dto.getNome());
        produto.setPreco(dto.getPreco());
        produto.setQuantidade(dto.getQuantidade());

        return produtoMapper.toResponseDTO(produtoRepository.save(produto));
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll()
                .stream()
                .map(produtoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscar(Long id) {
        return produtoMapper.toResponseDTO(buscarPorId(id));
    }

    @Transactional
    public void deletar(Long id) {
        produtoRepository.delete(buscarPorId(id));
    }

    private Produto buscarPorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + id));
    }
}