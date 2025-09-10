package com.pfeffer.javatoolschallenge.domain.request;

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
public class PagamentoRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String cartao;

    private String id;

    private DescricaoTransacaoRequest descricao;

    private FormaPagamentoRequest formaPagamento;

}
