package com.pfeffer.javatoolschallenge.domain.request;

import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
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
public class FormaPagamentoRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private TipoFormaPagamento tipo;

    private Integer parcelas;

}