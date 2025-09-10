package com.pfeffer.javatoolschallenge.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pfeffer.javatoolschallenge.domain.enums.StatusTransacao;
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
public class DescricaoTransacaoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;

    private String estabelecimento;

    private String nsu;

    private String codigoAutorizacao;

    private StatusTransacao status;

}
