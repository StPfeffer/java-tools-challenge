package com.pfeffer.javatoolschallenge.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String id;

    private String cartao;

    private DescricaoTransacaoDTO descricao;

    private FormaPagamentoDTO formaPagamento;

}
