package com.oracle.OSfacil.mapper;

import com.oracle.OSfacil.dto.request.OrdemServicoDTO;
import com.oracle.OSfacil.dto.response.OrdemServicoResponseDTO;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.OrdemServico;
import com.oracle.OSfacil.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrdemServicoMapper {

    private final ClienteRepository clienteRepository;
    private final ItemProdutoMapper itemProdutoMapper;

    public OrdemServicoResponseDTO toResponseDTO(OrdemServico ordemServico) {

        OrdemServicoResponseDTO dto = new OrdemServicoResponseDTO();

        dto.setId(ordemServico.getId());
        dto.setDescricao(ordemServico.getDescricao());
        dto.setValor(ordemServico.getValor());
        dto.setStatusOrdemServico(ordemServico.getStatusOrdemServico());
        dto.setStatusPagamento(ordemServico.getStatusPagamento());

        if (ordemServico.getCliente() != null) {
            dto.setClienteId(ordemServico.getCliente().getId());
            dto.setNomeCliente(ordemServico.getCliente().getNome());
        }

        if (ordemServico.getItens() != null) {
            dto.setItens(
                    ordemServico.getItens()
                            .stream()
                            .map(itemProdutoMapper::toResponseDTO)
                            .toList()
            );
        }

        return dto;
    }

    public OrdemServico toEntity(OrdemServicoDTO dto) {

        OrdemServico ordemServico = new OrdemServico();

        ordemServico.setDescricao(dto.getDescricao());
        ordemServico.setValor(dto.getValor());
        ordemServico.setStatusOrdemServico(dto.getStatusOrdemServico());
        ordemServico.setStatusPagamento(dto.getStatusPagamento());

        if (dto.getClienteId() != null) {

            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            ordemServico.setCliente(cliente);
        }

        return ordemServico;
    }
}