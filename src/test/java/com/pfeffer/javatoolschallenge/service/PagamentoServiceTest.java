package com.pfeffer.javatoolschallenge.service;

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
import com.pfeffer.javatoolschallenge.exception.TransacaoJaExisteException;
import com.pfeffer.javatoolschallenge.exception.TransacaoNotFoundException;
import com.pfeffer.javatoolschallenge.mapper.PagamentoMapper;
import com.pfeffer.javatoolschallenge.mapper.TransacaoMapper;
import com.pfeffer.javatoolschallenge.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @Mock
    private TransacaoMapper transacaoMapper;

    @InjectMocks
    private PagamentoService pagamentoService;

    private PagamentoRequest pagamentoRequest;

    private Transacao transacao;

    @BeforeEach
    void setUp() {
        DescricaoTransacao descricao = DescricaoTransacao.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
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
    }

    @Test
    void processarPagamento_QuandoTransacaoValida_DeveRetornarPagamentoResponse() {
        PagamentoDTO expectedResponse = PagamentoDTO.builder()
                .transacao(TransacaoDTO.builder()
                        .id("1000235689000001")
                        .cartao("4444********1234")
                        .build())
                .build();

        when(pagamentoMapper.requestToEntity(pagamentoRequest)).thenReturn(transacao);
        when(transacaoRepository.existsById(anyString())).thenReturn(false);
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(pagamentoMapper.entityToDto(any(Transacao.class))).thenReturn(expectedResponse);

        PagamentoDTO response = pagamentoService.processarPagamento(pagamentoRequest);

        assertNotNull(response);
        assertNotNull(response.getTransacao());
        verify(transacaoRepository).save(any(Transacao.class));
        verify(pagamentoMapper).requestToEntity(pagamentoRequest);
        verify(pagamentoMapper).entityToDto(any(Transacao.class));
    }

    @Test
    void processarPagamento_QuandoTransacaoJaExiste_DeveLancarTransacaoJaExisteException() {
        when(pagamentoMapper.requestToEntity(pagamentoRequest)).thenReturn(transacao);
        when(transacaoRepository.existsById(anyString())).thenReturn(true);

        assertThrows(TransacaoJaExisteException.class, () -> {
            pagamentoService.processarPagamento(pagamentoRequest);
        });
    }

    @Test
    void consultarTransacao_QuandoTransacaoExiste_DeveRetornarPagamentoResponse() {
        PagamentoDTO expectedResponse = PagamentoDTO.builder()
                .transacao(TransacaoDTO.builder()
                        .id("1000235689000001")
                        .cartao("4444********1234")
                        .build())
                .build();

        when(transacaoRepository.findById(anyString())).thenReturn(Optional.of(transacao));
        when(pagamentoMapper.entityToDto(transacao)).thenReturn(expectedResponse);

        PagamentoDTO response = pagamentoService.consultarTransacao("1000235689000001");

        assertNotNull(response);
        assertEquals("1000235689000001", response.getTransacao().getId());
        verify(pagamentoMapper).entityToDto(transacao);
    }

    @Test
    void consultarTransacao_QuandoTransacaoNaoExiste_DeveLancarTransacaoNotFoundException() {
        when(transacaoRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(TransacaoNotFoundException.class, () -> {
            pagamentoService.consultarTransacao("999999999");
        });
    }

    @Test
    void consultarTodasTransacoes_DeveRetornarListaDeTransacoes() {
        List<Transacao> transacoes = Collections.singletonList(transacao);
        TransacaoDTO transacaoDTO = TransacaoDTO.builder()
                .id("1000235689000001")
                .cartao("4444********1234")
                .build();

        when(transacaoRepository.findAll()).thenReturn(transacoes);
        when(transacaoMapper.entityToDto(transacao)).thenReturn(transacaoDTO);

        List<TransacaoDTO> resultado = pagamentoService.consultarTodasTransacoes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("1000235689000001", resultado.get(0).getId());
        verify(transacaoMapper).entityToDto(transacao);
    }

    @Test
    void estornarTransacao_QuandoTransacaoAutorizada_DeveAlterarStatusParaCancelado() {
        transacao.getDescricao().setStatus(StatusTransacao.AUTORIZADO);
        PagamentoDTO expectedResponse = PagamentoDTO.builder()
                .transacao(TransacaoDTO.builder()
                        .id("1000235689000001")
                        .cartao("4444********1234")
                        .build())
                .build();

        when(transacaoRepository.findById(anyString())).thenReturn(Optional.of(transacao));
        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacao);
        when(pagamentoMapper.entityToDto(any(Transacao.class))).thenReturn(expectedResponse);

        PagamentoDTO response = pagamentoService.estornarTransacao("1000235689000001");

        assertNotNull(response);
        verify(transacaoRepository).save(any(Transacao.class));
        verify(pagamentoMapper).entityToDto(any(Transacao.class));
    }

    @Test
    void estornarTransacao_QuandoTransacaoNaoAutorizada_DeveLancarIllegalStateException() {
        transacao.getDescricao().setStatus(StatusTransacao.NEGADO);
        when(transacaoRepository.findById(anyString())).thenReturn(Optional.of(transacao));

        assertThrows(IllegalStateException.class, () -> {
            pagamentoService.estornarTransacao("1000235689000001");
        });
    }

}
