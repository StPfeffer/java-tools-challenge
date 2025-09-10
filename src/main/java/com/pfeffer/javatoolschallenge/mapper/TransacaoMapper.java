package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.TransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;

public final class TransacaoMapper {

    private TransacaoMapper() {
    }

    public static Transacao requestToEntity(PagamentoRequest request) {
        if (request == null) {
            return null;
        }

        return Transacao.builder()
                .cartao(request.getCartao())
                .id(request.getId())
                .descricao(DescricaoTransacaoMapper.requestToEntity(request.getDescricao()))
                .formaPagamento(FormaPagamentoMapper.requestToEntity(request.getFormaPagamento()))
                .build();
    }

    public static TransacaoDTO entityToDto(Transacao transacao) {
        if (transacao == null) {
            return null;
        }

        return TransacaoDTO.builder()
                .id(transacao.getId())
                .cartao(transacao.getCartao())
                .descricao(DescricaoTransacaoMapper.entityToDto(transacao.getDescricao()))
                .formaPagamento(FormaPagamentoMapper.entityToDto(transacao.getFormaPagamento()))
                .build();
    }

}
