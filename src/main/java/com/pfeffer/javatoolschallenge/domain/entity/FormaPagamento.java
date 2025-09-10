package com.pfeffer.javatoolschallenge.domain.entity;

import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormaPagamento {

    private TipoFormaPagamento tipo;

    private Integer parcelas;

}
