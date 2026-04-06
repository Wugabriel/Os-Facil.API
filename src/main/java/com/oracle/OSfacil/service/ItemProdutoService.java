package com.oracle.OSfacil.service;


import com.oracle.OSfacil.dto.request.ItemProdutoDTO;
import com.oracle.OSfacil.dto.response.ItemProdutoResponseDTO;
import com.oracle.OSfacil.mapper.ItemProdutoMapper;
import com.oracle.OSfacil.model.ItemProduto;
import com.oracle.OSfacil.model.OrdemServico;
import com.oracle.OSfacil.model.Produto;
import com.oracle.OSfacil.repository.ItemProdutoRepository;
import com.oracle.OSfacil.repository.OrdemServicoRepository;
import com.oracle.OSfacil.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemProdutoService {

    private final ItemProdutoRepository itemProdutoRepository;
    private final ProdutoRepository produtoRepository;
    private final OrdemServicoRepository ordemServicoRepository;
    private final ItemProdutoMapper itemProdutoMapper;

    @Transactional
    public ItemProdutoResponseDTO criar(ItemProdutoDTO dto) {
        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + dto.getProdutoId()));

        OrdemServico ordemServico = ordemServicoRepository.findById(dto.getOrdemServicoId())
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada com id: " + dto.getOrdemServicoId()));

        ItemProduto itemProduto = itemProdutoMapper.toEntity(dto);
        itemProduto.setProduto(produto);
        itemProduto.setOrdemServico(ordemServico);
        itemProduto.setSubtotal(calcularSubtotal(dto.getValorUnitario(), dto.getQuantidade()));

        return itemProdutoMapper.toResponseDTO(itemProdutoRepository.save(itemProduto));
    }

    @Transactional(readOnly = true)
    public ItemProdutoResponseDTO buscar(Long id) {
        return itemProdutoMapper.toResponseDTO(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<ItemProdutoResponseDTO> listarTodos() {
        return itemProdutoRepository.findAll()
                .stream()
                .map(itemProdutoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public ItemProdutoResponseDTO atualizar(Long id, ItemProdutoDTO dto) {
        ItemProduto itemProduto = buscarPorId(id);

        Produto produto = produtoRepository.findById(dto.getProdutoId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com id: " + dto.getProdutoId()));

        OrdemServico ordemServico = ordemServicoRepository.findById(dto.getOrdemServicoId())
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada com id: " + dto.getOrdemServicoId()));

        itemProduto.setProduto(produto);
        itemProduto.setOrdemServico(ordemServico);
        itemProduto.setQuantidade(dto.getQuantidade());
        itemProduto.setValorUnitario(dto.getValorUnitario());
        itemProduto.setSubtotal(calcularSubtotal(dto.getValorUnitario(), dto.getQuantidade()));

        return itemProdutoMapper.toResponseDTO(itemProdutoRepository.save(itemProduto));
    }

    @Transactional
    public void deletar(Long id) {
        itemProdutoRepository.delete(buscarPorId(id));
    }

    private ItemProduto buscarPorId(Long id) {
        return itemProdutoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item produto não encontrado com id: " + id));
    }

    private BigDecimal calcularSubtotal(BigDecimal valorUnitario, Integer quantidade) {
        if (valorUnitario == null || quantidade == null) {
            throw new RuntimeException("Valor unitário e quantidade são obrigatórios para calcular o subtotal");
        }
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }
}

