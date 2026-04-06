package com.oracle.OSfacil.dto.request;


import com.oracle.OSfacil.enums.FormaPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoDTO {
    @NotNull(message = "A forma de pagamento não pode ser vazio")
    private FormaPagamento formaPagamento;
    @NotNull(message = "O valor não pode ser nulo")
    @Positive
    private BigDecimal valor;

}
