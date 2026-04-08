package com.oracle.OSfacil.repository;

import com.oracle.OSfacil.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    Optional<Funcionario> findByEmailIgnoreCase(String email);
}
