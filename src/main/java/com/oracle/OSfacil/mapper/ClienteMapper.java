package com.oracle.OSfacil.mapper;

import com.oracle.OSfacil.dto.request.ClienteDTO;
import com.oracle.OSfacil.dto.response.ClienteResponseDTO;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.Veiculo;
import com.oracle.OSfacil.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;


@Component
@AllArgsConstructor
public class ClienteMapper {

    private final VeiculoRepository veiculoRepository;

    public ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getId());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setTelefone(cliente.getTelefone());
        dto.setCpf(cliente.getCpf());
        dto.setEndereco(cliente.getEndereco());

        if (cliente.getVeiculos() != null && !cliente.getVeiculos().isEmpty()) {

            Set<Long> veiculoIds = cliente.getVeiculos()
                    .stream()
                    .map(Veiculo::getId)
                    .collect(Collectors.toSet());

            dto.setVeiculosIds(veiculoIds);
        }

        return dto;
    }

    public Cliente toEntity(ClienteDTO dto) {

        Cliente cliente = new Cliente();

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());
        cliente.setSenha(dto.getSenha());
        cliente.setEndereco(dto.getEndereco());

        if (dto.getVeiculoIds() != null && !dto.getVeiculoIds().isEmpty()) {

            Set<Veiculo> veiculos = dto.getVeiculoIds().stream()
                    .map(id -> veiculoRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Veículo não encontrado: " + id)))
                    .collect(Collectors.toSet());

            veiculos.forEach(v -> v.setCliente(cliente));

            cliente.setVeiculos(veiculos);
        }

        return cliente;
    }
}