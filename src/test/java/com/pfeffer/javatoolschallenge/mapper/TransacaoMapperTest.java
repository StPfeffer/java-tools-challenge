package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.DescricaoTransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.FormaPagamentoDTO;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransacaoMapperTest {

    @Mock
    private DescricaoTransacaoMapper descricaoMapper;

    @Mock
    private FormaPagamentoMapper formaPagamentoMapper;

    @InjectMocks
    private TransacaoMapper transacaoMapper;

    private PagamentoRequest pagamentoRequest;

    private Transacao transacao;

    private DescricaoTransacao descricaoEntity;

    private FormaPagamento formaPagamentoEntity;

    private DescricaoTransacaoDTO descricaoDTO;

    private FormaPagamentoDTO formaPagamentoDTO;

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

        descricaoDTO = DescricaoTransacaoDTO.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .nsu("1234567890")
                .codigoAutorizacao("147258369")
                .status(StatusTransacao.AUTORIZADO)
                .build();

        formaPagamentoDTO = FormaPagamentoDTO.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();
    }

    @Test
    void requestToEntity_DeveConverterCorretamente() {
        when(descricaoMapper.requestToEntity(pagamentoRequest.getDescricao())).thenReturn(descricaoEntity);
        when(formaPagamentoMapper.requestToEntity(pagamentoRequest.getFormaPagamento())).thenReturn(formaPagamentoEntity);

        Transacao result = transacaoMapper.requestToEntity(pagamentoRequest);

        assertNotNull(result);
        assertEquals("4444********1234", result.getCartao());
        assertEquals("1000235689000001", result.getId());
        assertNotNull(result.getDescricao());
        assertNotNull(result.getFormaPagamento());
        verify(descricaoMapper).requestToEntity(pagamentoRequest.getDescricao());
        verify(formaPagamentoMapper).requestToEntity(pagamentoRequest.getFormaPagamento());
    }

    @Test
    void entityToDto_DeveConverterCorretamente() {
        when(descricaoMapper.entityToDto(descricaoEntity)).thenReturn(descricaoDTO);
        when(formaPagamentoMapper.entityToDto(formaPagamentoEntity)).thenReturn(formaPagamentoDTO);

        TransacaoDTO result = transacaoMapper.entityToDto(transacao);

        assertNotNull(result);
        assertEquals("4444********1234", result.getCartao());
        assertEquals("1000235689000001", result.getId());
        assertNotNull(result.getDescricao());
        assertNotNull(result.getFormaPagamento());
        verify(descricaoMapper).entityToDto(descricaoEntity);
        verify(formaPagamentoMapper).entityToDto(formaPagamentoEntity);
    }

    @Test
    void requestToEntity_ComRequestNull_DeveRetornarNull() {
        Object result = transacaoMapper.requestToEntity(null);
        assertNull(result);
    }

    @Test
    void entityToDto_ComEntityNull_DeveRetornarNull() {
        Object result = transacaoMapper.entityToDto(null);
        assertNull(result);
    }

}
