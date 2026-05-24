package com.oracle.OSfacil.dto.response;

import lombok.Data;

@Data
public class TokenResponseDTO {
    private String tokenAcesso;
    private Long id;
    private String nome;
    private String email;
    private String role;
}