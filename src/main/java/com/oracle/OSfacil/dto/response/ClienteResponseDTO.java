package com.oracle.OSfacil.dto.response;


import com.oracle.OSfacil.dto.request.ClienteDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Data
public class ClienteResponseDTO  extends RepresentationModel<ClienteDTO> {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String endereco;
    private Set<Long> veiculosIds =  new HashSet<>();

}
