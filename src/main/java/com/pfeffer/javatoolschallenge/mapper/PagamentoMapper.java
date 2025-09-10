package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;

public final class PagamentoMapper {

    private PagamentoMapper() {
    }

    public static Transacao requestToEntity(PagamentoRequest request) {
        if (request == null) {
            return null;
        }

        return TransacaoMapper.requestToEntity(request);
    }

    public static PagamentoDTO entityToDto(Transacao transacao) {
        if (transacao == null) {
            return null;
        }

        return PagamentoDTO.builder()
                .transacao(TransacaoMapper.entityToDto(transacao))
                .build();
    }

}
