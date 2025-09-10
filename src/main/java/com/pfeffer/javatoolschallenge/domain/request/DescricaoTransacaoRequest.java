package com.pfeffer.javatoolschallenge.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescricaoTransacaoRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal valor;

    private LocalDateTime dataHora;

    private String estabelecimento;

}
