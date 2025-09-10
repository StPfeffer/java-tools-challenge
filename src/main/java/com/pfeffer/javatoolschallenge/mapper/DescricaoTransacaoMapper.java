package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.DescricaoTransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.DescricaoTransacao;
import com.pfeffer.javatoolschallenge.domain.request.DescricaoTransacaoRequest;
import org.springframework.stereotype.Component;

@Component
public class DescricaoTransacaoMapper {

    public DescricaoTransacao requestToEntity(DescricaoTransacaoRequest request) {
        if (request == null) {
            return null;
        }

        return DescricaoTransacao.builder()
                .valor(request.getValor())
                .dataHora(request.getDataHora())
                .estabelecimento(request.getEstabelecimento())
                .build();
    }

    public DescricaoTransacaoDTO entityToDto(DescricaoTransacao entity) {
        if (entity == null) {
            return null;
        }

        return DescricaoTransacaoDTO.builder()
                .valor(entity.getValor())
                .dataHora(entity.getDataHora())
                .estabelecimento(entity.getEstabelecimento())
                .nsu(entity.getNsu())
                .codigoAutorizacao(entity.getCodigoAutorizacao())
                .status(entity.getStatus())
                .build();
    }

}
