package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.DescricaoTransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.FormaPagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
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
class PagamentoMapperTest {

    @Mock
    private TransacaoMapper transacaoMapper;

    @InjectMocks
    private PagamentoMapper pagamentoMapper;

    private PagamentoRequest pagamentoRequest;

    private Transacao transacao;

    private TransacaoDTO transacaoDTO;

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

        DescricaoTransacaoDTO descricaoDTO = DescricaoTransacaoDTO.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .nsu("1234567890")
                .codigoAutorizacao("147258369")
                .status(StatusTransacao.AUTORIZADO)
                .build();

        FormaPagamentoDTO formaPagamentoDTO = FormaPagamentoDTO.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        transacaoDTO = TransacaoDTO.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(descricaoDTO)
                .formaPagamento(formaPagamentoDTO)
                .build();
    }

    @Test
    void requestToEntity_DeveConverterCorretamente() {
        when(transacaoMapper.requestToEntity(pagamentoRequest)).thenReturn(transacao);

        Transacao result = pagamentoMapper.requestToEntity(pagamentoRequest);

        assertNotNull(result);
        assertEquals(transacao.getId(), result.getId());
        assertEquals(transacao.getCartao(), result.getCartao());
        verify(transacaoMapper).requestToEntity(pagamentoRequest);
    }

    @Test
    void entityToDto_DeveConverterCorretamente() {
        when(transacaoMapper.entityToDto(transacao)).thenReturn(transacaoDTO);

        PagamentoDTO result = pagamentoMapper.entityToDto(transacao);

        assertNotNull(result);
        assertNotNull(result.getTransacao());
        assertEquals(transacaoDTO.getId(), result.getTransacao().getId());
        assertEquals(transacaoDTO.getCartao(), result.getTransacao().getCartao());
        verify(transacaoMapper).entityToDto(transacao);
    }

    @Test
    void requestToEntity_ComRequestNull_DeveRetornarNull() {
        Transacao result = pagamentoMapper.requestToEntity(null);
        assertNull(result);
    }

}
