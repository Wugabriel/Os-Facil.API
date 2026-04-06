package com.oracle.OSfacil.service;

import com.oracle.OSfacil.dto.request.FuncionarioDTO;
import com.oracle.OSfacil.dto.response.FuncionarioResponseDTO;
import com.oracle.OSfacil.enums.Role;
import com.oracle.OSfacil.mapper.FuncionarioMapper;
import com.oracle.OSfacil.model.Funcionario;
import com.oracle.OSfacil.repository.FuncionarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final FuncionarioMapper funcionarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public FuncionarioResponseDTO criar(FuncionarioDTO dto) {
        if (funcionarioRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado para outro funcionário!");
        }
        if (funcionarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado para outro funcionário!");
        }
        if (funcionarioRepository.existsByLogin(dto.getLogin())) {
            throw new RuntimeException("Login já cadastrado para outro funcionário!");
        }

        Funcionario funcionario = funcionarioMapper.toEntity(dto);
        funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));
        funcionario.setRole(Role.ROLE_FUNCIONARIO);

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional
    public FuncionarioResponseDTO atualizar(FuncionarioDTO dto, Long id) {
        Funcionario funcionario = buscarPorId(id);

        if (!funcionario.getCpf().equals(dto.getCpf()) && funcionarioRepository.existsByCpf(dto.getCpf())) {
            throw new RuntimeException("CPF já cadastrado para outro funcionário!");
        }
        if (!funcionario.getEmail().equals(dto.getEmail()) && funcionarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado para outro funcionário!");
        }
        if (!funcionario.getLogin().equals(dto.getLogin()) && funcionarioRepository.existsByLogin(dto.getLogin())) {
            throw new RuntimeException("Login já cadastrado para outro funcionário!");
        }

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setCpf(dto.getCpf());
        funcionario.setSalario(dto.getSalario());
        funcionario.setLogin(dto.getLogin());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return funcionarioMapper.toResponseDTO(funcionarioRepository.save(funcionario));
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll()
                .stream()
                .map(funcionarioMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO listarPorId(Long id) {
        return funcionarioMapper.toResponseDTO(buscarPorId(id));
    }

    @Transactional
    public void deletarPorId(Long id) {
        funcionarioRepository.delete(buscarPorId(id));
    }

    private Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado com id: " + id));
    }
}