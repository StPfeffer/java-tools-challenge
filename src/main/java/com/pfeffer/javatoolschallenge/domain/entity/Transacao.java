package com.pfeffer.javatoolschallenge.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    private String cartao;

    private String id;

    private DescricaoTransacao descricao;

    private FormaPagamento formaPagamento;

}
