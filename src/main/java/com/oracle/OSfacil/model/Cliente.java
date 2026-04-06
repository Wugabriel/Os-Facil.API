package com.oracle.OSfacil.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oracle.OSfacil.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Entity
@Getter
@Setter
@Table(name = "tb_cliente")
@EqualsAndHashCode(of = "id")
public class Cliente implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "cliente_seq_generator")
    @SequenceGenerator(name = "cliente_seq_generator",sequenceName = "cliente_generator",allocationSize=1)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, length = 50, unique = true)
    private String cpf;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String senha;
    @OneToMany(mappedBy = "cliente",cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Veiculo> veiculos = new HashSet<>();
    @Column(nullable = false,length = 50)
    private String telefone;
    @Column(nullable = false)
    private String endereco;
    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
