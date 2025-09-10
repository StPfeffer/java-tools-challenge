package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.TransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransacaoMapper {

    private final DescricaoTransacaoMapper descricaoMapper;

    private final FormaPagamentoMapper formaPagamentoMapper;

    public Transacao requestToEntity(PagamentoRequest request) {
        if (request == null) {
            return null;
        }

        return Transacao.builder()
                .cartao(request.getCartao())
                .id(request.getId())
                .descricao(descricaoMapper.requestToEntity(request.getDescricao()))
                .formaPagamento(formaPagamentoMapper.requestToEntity(request.getFormaPagamento()))
                .build();
    }

    public TransacaoDTO entityToDto(Transacao transacao) {
        if (transacao == null) {
            return null;
        }

        return TransacaoDTO.builder()
                .id(transacao.getId())
                .cartao(transacao.getCartao())
                .descricao(descricaoMapper.entityToDto(transacao.getDescricao()))
                .formaPagamento(formaPagamentoMapper.entityToDto(transacao.getFormaPagamento()))
                .build();
    }

}
