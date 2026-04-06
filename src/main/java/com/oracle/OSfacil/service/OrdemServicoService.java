package com.oracle.OSfacil.service;

import com.oracle.OSfacil.dto.request.OrdemServicoDTO;
import com.oracle.OSfacil.dto.response.OrdemServicoResponseDTO;
import com.oracle.OSfacil.enums.StatusOrdemServico;
import com.oracle.OSfacil.enums.StatusPagamento;
import com.oracle.OSfacil.mapper.OrdemServicoMapper;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.OrdemServico;
import com.oracle.OSfacil.repository.ClienteRepository;
import com.oracle.OSfacil.repository.OrdemServicoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;
    private final ClienteRepository clienteRepository;
    private final OrdemServicoMapper ordemServicoMapper;

    @Transactional
    public OrdemServicoResponseDTO criar(OrdemServicoDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + dto.getClienteId()));

        OrdemServico ordemServico = ordemServicoMapper.toEntity(dto);
        ordemServico.setCliente(cliente);
        ordemServico.setStatusOrdemServico(StatusOrdemServico.ABERTA);
        ordemServico.setStatusPagamento(StatusPagamento.PENDENTE);

        return ordemServicoMapper.toResponseDTO(ordemServicoRepository.save(ordemServico));
    }

    @Transactional
    public OrdemServicoResponseDTO atualizar(OrdemServicoDTO dto, Long id) {
        OrdemServico ordemServico = buscarPorId(id);

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + dto.getClienteId()));

        ordemServico.setCliente(cliente);
        ordemServico.setValor(dto.getValor());
        ordemServico.setDescricao(dto.getDescricao());
        ordemServico.setStatusPagamento(dto.getStatusPagamento());
        ordemServico.setStatusOrdemServico(dto.getStatusOrdemServico());

        return ordemServicoMapper.toResponseDTO(ordemServicoRepository.save(ordemServico));
    }

    @Transactional(readOnly = true)
    public OrdemServicoResponseDTO buscar(Long id) {
        return ordemServicoMapper.toResponseDTO(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoResponseDTO> listarTodos() {
        return ordemServicoRepository.findAll()
                .stream()
                .map(ordemServicoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        ordemServicoRepository.delete(buscarPorId(id));
    }

    private OrdemServico buscarPorId(Long id) {
        return ordemServicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada com id: " + id));
    }
}