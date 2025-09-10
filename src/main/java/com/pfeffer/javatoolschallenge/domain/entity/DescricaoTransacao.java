package com.pfeffer.javatoolschallenge.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pfeffer.javatoolschallenge.domain.enums.StatusTransacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescricaoTransacao {

    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;

    private String estabelecimento;

    private String nsu;

    private String codigoAutorizacao;

    private StatusTransacao status;

}
