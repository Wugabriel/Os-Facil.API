package com.oracle.OSfacil.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VeiculoDTO {
    @NotBlank(message = "O modelo do carro não pode ser vazio")
    private String modelo;
    @NotBlank(message = "A cor do carro não pode ser vazio")
    private String cor;
    @NotBlank(message = "A marca do carro não pode ser vazio")
    private String marca;
    @NotNull(message = "O ano do veículo não pode ser vazio")
    private Integer ano;
    @NotNull(message = "O Id do cliente não pode ser vazio")
    private Long clienteId;
    @NotBlank(message = "A placa não pode ser vazio")
    private String placa;
}
