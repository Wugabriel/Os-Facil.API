package com.oracle.OSfacil.dto.response;


import lombok.Data;

@Data
public class VeiculoResponseDTO {
    private Long id;
    private Long clienteId;
    private String nomeCliente;
    private String modelo;
    private String marca;
    private String cor;
    private Integer ano;
    private String placa;

}
