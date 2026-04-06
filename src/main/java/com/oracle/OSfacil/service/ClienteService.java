package com.oracle.OSfacil.service;


import com.oracle.OSfacil.dto.request.ClienteDTO;
import com.oracle.OSfacil.dto.response.ClienteResponseDTO;
import com.oracle.OSfacil.enums.Role;
import com.oracle.OSfacil.mapper.ClienteMapper;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.Veiculo;
import com.oracle.OSfacil.repository.ClienteRepository;
import com.oracle.OSfacil.repository.VeiculoRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ClienteService implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ClienteResponseDTO criar(ClienteDTO dto) {
        if (clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado para outro cliente!");
        }
        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado para outro cliente!");
        }

        Cliente cliente = clienteMapper.toEntity(dto);
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setRole(Role.ROLE_CLIENTE);

        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAllWithVeiculos()
                .stream()
                .map(clienteMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO listarPorId(Long id) {
        return clienteMapper.toResponseDTO(
                clienteRepository.findByIdWithVeiculos(id)
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id))
        );
    }

    @Transactional
    public ClienteResponseDTO atualizar(Long id, ClienteDTO dto) {
        Cliente cliente = buscarPorId(id);

        if (!cliente.getCpf().equals(dto.getCpf()) && clienteRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado para outro cliente!");
        }

        if (!cliente.getEmail().equals(dto.getEmail()) && clienteRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado para outro cliente!");
        }

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        cliente.setCpf(dto.getCpf());
        cliente.setEndereco(dto.getEndereco());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return clienteMapper.toResponseDTO(clienteRepository.save(cliente));
    }

    @Transactional
    public void deletar(Long id) {
        clienteRepository.delete(buscarPorId(id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clienteRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("Cliente não encontrado com email: " + username));
    }

    private Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com id: " + id));
    }
}