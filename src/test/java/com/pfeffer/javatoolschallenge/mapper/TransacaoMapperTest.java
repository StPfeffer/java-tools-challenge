package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.TransacaoDTO;
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

class TransacaoMapperTest {

    private PagamentoRequest pagamentoRequest;

    private Transacao transacao;

    private DescricaoTransacao descricaoEntity;

    private FormaPagamento formaPagamentoEntity;

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

        descricaoEntity = DescricaoTransacao.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .nsu("1234567890")
                .codigoAutorizacao("147258369")
                .status(StatusTransacao.AUTORIZADO)
                .build();

        formaPagamentoEntity = FormaPagamento.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        transacao = Transacao.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(descricaoEntity)
                .formaPagamento(formaPagamentoEntity)
                .build();
    }

    @Test
    void requestToEntity_DeveConverterCorretamente() {
        Transacao result = TransacaoMapper.requestToEntity(pagamentoRequest);

        assertNotNull(result);
        assertEquals("4444********1234", result.getCartao());
        assertEquals("1000235689000001", result.getId());
        assertNotNull(result.getDescricao());
        assertNotNull(result.getFormaPagamento());

        assertEquals(new BigDecimal("500.50"), result.getDescricao().getValor());
        assertEquals(LocalDateTime.of(2021, 5, 1, 18, 30, 0), result.getDescricao().getDataHora());
        assertEquals("PetShop Mundo cão", result.getDescricao().getEstabelecimento());

        assertEquals(TipoFormaPagamento.AVISTA, result.getFormaPagamento().getTipo());
        assertEquals(1, result.getFormaPagamento().getParcelas());
    }

    @Test
    void entityToDto_DeveConverterCorretamente() {
        TransacaoDTO result = TransacaoMapper.entityToDto(transacao);

        assertNotNull(result);
        assertEquals("4444********1234", result.getCartao());
        assertEquals("1000235689000001", result.getId());
        assertNotNull(result.getDescricao());
        assertNotNull(result.getFormaPagamento());

        assertEquals(new BigDecimal("500.50"), result.getDescricao().getValor());
        assertEquals(LocalDateTime.of(2021, 5, 1, 18, 30, 0), result.getDescricao().getDataHora());
        assertEquals("PetShop Mundo cão", result.getDescricao().getEstabelecimento());
        assertEquals("1234567890", result.getDescricao().getNsu());
        assertEquals("147258369", result.getDescricao().getCodigoAutorizacao());
        assertEquals(StatusTransacao.AUTORIZADO, result.getDescricao().getStatus());

        assertEquals(TipoFormaPagamento.AVISTA, result.getFormaPagamento().getTipo());
        assertEquals(1, result.getFormaPagamento().getParcelas());
    }

    @Test
    void requestToEntity_ComRequestNull_DeveRetornarNull() {
        Transacao result = TransacaoMapper.requestToEntity(null);

        assertNull(result);
    }

    @Test
    void entityToDto_ComEntityNull_DeveRetornarNull() {
        TransacaoDTO result = TransacaoMapper.entityToDto(null);

        assertNull(result);
    }

    @Test
    void requestToEntity_ComDescricaoEFormaPagamentoNulos_DeveManterNulos() {
        PagamentoRequest requestComNulos = PagamentoRequest.builder()
                .cartao("1111********5678")
                .id("2000000000000001")
                .descricao(null)
                .formaPagamento(null)
                .build();

        Transacao result = TransacaoMapper.requestToEntity(requestComNulos);

        assertNotNull(result);
        assertEquals("1111********5678", result.getCartao());
        assertEquals("2000000000000001", result.getId());
        assertNull(result.getDescricao());
        assertNull(result.getFormaPagamento());
    }

    @Test
    void entityToDto_ComDescricaoEFormaPagamentoNulos_DeveManterNulos() {
        Transacao transacaoComNulos = Transacao.builder()
                .cartao("1111********5678")
                .id("2000000000000001")
                .descricao(null)
                .formaPagamento(null)
                .build();

        TransacaoDTO result = TransacaoMapper.entityToDto(transacaoComNulos);

        assertNotNull(result);
        assertEquals("1111********5678", result.getCartao());
        assertEquals("2000000000000001", result.getId());
        assertNull(result.getDescricao());
        assertNull(result.getFormaPagamento());
    }

}
