package com.oracle.OSfacil.service;

import com.oracle.OSfacil.dto.request.VeiculoDTO;
import com.oracle.OSfacil.dto.response.VeiculoResponseDTO;
import com.oracle.OSfacil.mapper.VeiculoMapper;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.Veiculo;
import com.oracle.OSfacil.repository.ClienteRepository;
import com.oracle.OSfacil.repository.VeiculoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final ClienteRepository clienteRepository;
    private final VeiculoMapper veiculoMapper;

    @Transactional
    public VeiculoResponseDTO criar(VeiculoDTO dto) {
        if (veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new RuntimeException("Já existe um veículo cadastrado com a placa: " + dto.getPlaca());
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + dto.getClienteId()));

        Veiculo veiculo = veiculoMapper.toEntity(dto);
        veiculo.setCliente(cliente);
        cliente.getVeiculos().add(veiculo);

        return veiculoMapper.toResponseDTO(veiculoRepository.save(veiculo));
    }

    @Transactional
    public VeiculoResponseDTO atualizar(Long id, VeiculoDTO dto) {
        Veiculo veiculo = buscarPorId(id);

        if (!veiculo.getPlaca().equals(dto.getPlaca()) && veiculoRepository.existsByPlaca(dto.getPlaca())) {
            throw new RuntimeException("Já existe um veículo cadastrado com a placa: " + dto.getPlaca());
        }

        Cliente clienteNovo = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + dto.getClienteId()));

        Cliente clienteAtual = veiculo.getCliente();
        if (clienteAtual != null && !clienteAtual.getId().equals(clienteNovo.getId())) {
            clienteAtual.getVeiculos().remove(veiculo);
        }

        veiculo.setPlaca(dto.getPlaca());
        veiculo.setAno(dto.getAno());
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setCor(dto.getCor());
        veiculo.setCliente(clienteNovo);
        clienteNovo.getVeiculos().add(veiculo);

        return veiculoMapper.toResponseDTO(veiculoRepository.save(veiculo));
    }

    @Transactional(readOnly = true)
    public VeiculoResponseDTO buscar(Long id) {
        return veiculoMapper.toResponseDTO(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<VeiculoResponseDTO> listarTodos() {
        return veiculoRepository.findAll()
                .stream()
                .map(veiculoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public void deletar(Long id) {
        Veiculo veiculo = buscarPorId(id);

        Cliente cliente = veiculo.getCliente();
        if (cliente != null) {
            cliente.getVeiculos().remove(veiculo);
        }

        veiculoRepository.delete(veiculo);
    }

    private Veiculo buscarPorId(Long id) {
        return veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado com id: " + id));
    }
}