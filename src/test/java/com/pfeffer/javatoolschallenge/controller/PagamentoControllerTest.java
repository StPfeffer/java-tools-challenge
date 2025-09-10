package com.pfeffer.javatoolschallenge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pfeffer.javatoolschallenge.domain.dto.DescricaoTransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.FormaPagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.TransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
import com.pfeffer.javatoolschallenge.domain.request.DescricaoTransacaoRequest;
import com.pfeffer.javatoolschallenge.domain.request.FormaPagamentoRequest;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;
import com.pfeffer.javatoolschallenge.service.PagamentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagamentoController.class)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PagamentoService pagamentoService;

    @Autowired
    private ObjectMapper objectMapper;

    private PagamentoRequest pagamentoRequest;

    private PagamentoDTO pagamentoResponse;

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

        DescricaoTransacaoDTO descricaoDTO = DescricaoTransacaoDTO.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .build();

        FormaPagamentoDTO formaPagamentoDTO = FormaPagamentoDTO.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        TransacaoDTO transacaoDTO = TransacaoDTO.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(descricaoDTO)
                .formaPagamento(formaPagamentoDTO)
                .build();

        pagamentoResponse = PagamentoDTO.builder()
                .transacao(transacaoDTO)
                .build();
    }

    @Test
    void processarPagamento_QuandoRequestValido_DeveRetornar201() throws Exception {
        when(pagamentoService.processarPagamento(any(PagamentoRequest.class))).thenReturn(pagamentoResponse);

        mockMvc.perform(post("/api/v1/pagamentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagamentoRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.transacao.id").value("1000235689000001"))
                .andExpect(jsonPath("$.transacao.cartao").value("4444********1234"));
    }

    @Test
    void consultarTransacao_QuandoIdExiste_DeveRetornar200() throws Exception {
        when(pagamentoService.consultarTransacao(anyString())).thenReturn(pagamentoResponse);

        mockMvc.perform(get("/api/v1/pagamentos/1000235689000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id").value("1000235689000001"));
    }

    @Test
    void consultarTodasTransacoes_DeveRetornar200() throws Exception {
        when(pagamentoService.consultarTodasTransacoes()).thenReturn(Collections.singletonList(pagamentoResponse.getTransacao()));

        mockMvc.perform(get("/api/v1/pagamentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1000235689000001"));
    }

    @Test
    void estornarTransacao_QuandoIdExiste_DeveRetornar200() throws Exception {
        when(pagamentoService.estornarTransacao(anyString())).thenReturn(pagamentoResponse);

        mockMvc.perform(put("/api/v1/pagamentos/1000235689000001/estorno"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.id").value("1000235689000001"));
    }

}
