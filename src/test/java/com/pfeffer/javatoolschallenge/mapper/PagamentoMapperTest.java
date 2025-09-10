package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.DescricaoTransacao;
import com.pfeffer.javatoolschallenge.domain.entity.FormaPagamento;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.enums.StatusTransacao;
import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
import com.pfeffer.javatoolschallenge.domain.request.DescricaoTransacaoRequest;
import com.pfeffer.javatoolschallenge.domain.request.FormaPagamentoRequest;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PagamentoMapperTest {

    private PagamentoRequest pagamentoRequest;

    private Transacao transacao;

    @BeforeEach
    void setUp() {
        DescricaoTransacaoRequest descricaoRequest = DescricaoTransacaoRequest.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .build();

        FormaPagamentoRequest formaPagamentoRequest = FormaPagamentoRequest.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        pagamentoRequest = PagamentoRequest.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(descricaoRequest)
                .formaPagamento(formaPagamentoRequest)
                .build();

        DescricaoTransacao descricao = DescricaoTransacao.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .nsu("1234567890")
                .codigoAutorizacao("147258369")
                .status(StatusTransacao.AUTORIZADO)
                .build();

        FormaPagamento formaPagamento = FormaPagamento.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        transacao = Transacao.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(descricao)
                .formaPagamento(formaPagamento)
                .build();
    }

    @Test
    void requestToEntity_DeveConverterCorretamente() {
        Transacao result = PagamentoMapper.requestToEntity(pagamentoRequest);

        assertNotNull(result);
        assertEquals("1000235689000001", result.getId());
        assertEquals("4444********1234", result.getCartao());
        assertNotNull(result.getDescricao());
        assertNotNull(result.getFormaPagamento());
    }

    @Test
    void entityToDto_DeveConverterCorretamente() {
        PagamentoDTO result = PagamentoMapper.entityToDto(transacao);

        assertNotNull(result);
        assertNotNull(result.getTransacao());
        assertEquals("1000235689000001", result.getTransacao().getId());
        assertEquals("4444********1234", result.getTransacao().getCartao());
    }

    @Test
    void requestToEntity_ComRequestNull_DeveRetornarNull() {
        Transacao result = PagamentoMapper.requestToEntity(null);
        assertNull(result);
    }

    @Test
    void entityToDto_ComEntityNull_DeveRetornarNull() {
        PagamentoDTO result = PagamentoMapper.entityToDto(null);
        assertNull(result);
    }

}
