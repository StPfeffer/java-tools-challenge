package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.FormaPagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.FormaPagamento;
import com.pfeffer.javatoolschallenge.domain.request.FormaPagamentoRequest;

public final class FormaPagamentoMapper {

    private FormaPagamentoMapper() {
    }

    public static FormaPagamento requestToEntity(FormaPagamentoRequest request) {
        if (request == null) {
            return null;
        }

        return FormaPagamento.builder()
                .tipo(request.getTipo())
                .parcelas(request.getParcelas())
                .build();
    }

    public static FormaPagamentoDTO entityToDto(FormaPagamento entity) {
        if (entity == null) {
            return null;
        }

        return FormaPagamentoDTO.builder()
                .tipo(entity.getTipo())
                .parcelas(entity.getParcelas())
                .build();
    }

}
