package com.oracle.OSfacil.mapper;

import com.oracle.OSfacil.dto.request.VeiculoDTO;
import com.oracle.OSfacil.dto.response.VeiculoResponseDTO;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.Veiculo;
import com.oracle.OSfacil.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VeiculoMapper {

    private final ClienteRepository clienteRepository;

    public VeiculoResponseDTO toResponseDTO(Veiculo veiculo) {

        VeiculoResponseDTO dto = new VeiculoResponseDTO();

        dto.setId(veiculo.getId());
        dto.setModelo(veiculo.getModelo());
        dto.setMarca(veiculo.getMarca());
        dto.setCor(veiculo.getCor());
        dto.setAno(veiculo.getAno());
        dto.setPlaca(veiculo.getPlaca());

        if (veiculo.getCliente() != null) {
            dto.setClienteId(veiculo.getCliente().getId());
            dto.setNomeCliente(veiculo.getCliente().getNome());
        }

        return dto;
    }

    public Veiculo toEntity(VeiculoDTO dto) {

        Veiculo veiculo = new Veiculo();

        veiculo.setModelo(dto.getModelo());
        veiculo.setMarca(dto.getMarca());
        veiculo.setCor(dto.getCor());
        veiculo.setAno(dto.getAno());
        veiculo.setPlaca(dto.getPlaca());

        if (dto.getClienteId() != null) {

            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            veiculo.setCliente(cliente);

        } else {
            veiculo.setCliente(null);
        }

        return veiculo;
    }
}
