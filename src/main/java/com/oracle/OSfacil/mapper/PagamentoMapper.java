package com.oracle.OSfacil.mapper;

import com.oracle.OSfacil.dto.request.PagamentoDTO;
import com.oracle.OSfacil.dto.response.PagamentoResponseDTO;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.model.Pagamento;
import com.oracle.OSfacil.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class PagamentoMapper {

    public PagamentoResponseDTO toResponseDTO(Pagamento pagamento) {
        if (pagamento == null) return null;

        PagamentoResponseDTO dto = new PagamentoResponseDTO();
        dto.setId(pagamento.getId());
        dto.setValor(pagamento.getValor());
        dto.setFormaPagamento(pagamento.getFormaPagamento());

        if (pagamento.getCliente() != null) {
            dto.setClienteId(pagamento.getCliente().getId());
            dto.setNomeCliente(pagamento.getCliente().getNome());
        }

        return dto;
    }

    public Pagamento toEntity(PagamentoDTO dto) {
        if (dto == null) return null;

        Pagamento pagamento = new Pagamento();
        pagamento.setValor(dto.getValor());
        pagamento.setFormaPagamento(dto.getFormaPagamento());
        return pagamento;
    }
}
