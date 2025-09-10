package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PagamentoMapper {

    private final TransacaoMapper transacaoMapper;

    public Transacao requestToEntity(PagamentoRequest request) {
        if (request == null) {
            return null;
        }

        return transacaoMapper.requestToEntity(request);
    }

    public PagamentoDTO entityToDto(Transacao transacao) {
        if (transacao == null) {
            return null;
        }

        return PagamentoDTO.builder()
                .transacao(transacaoMapper.entityToDto(transacao))
                .build();
    }

}
