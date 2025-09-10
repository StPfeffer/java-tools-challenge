package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.FormaPagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.FormaPagamento;
import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
import com.pfeffer.javatoolschallenge.domain.request.FormaPagamentoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FormaPagamentoMapperTest {

    private FormaPagamentoMapper mapper;

    private FormaPagamentoRequest request;

    private FormaPagamento entity;

    @BeforeEach
    void setUp() {
        mapper = new FormaPagamentoMapper();

        request = FormaPagamentoRequest.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        entity = FormaPagamento.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();
    }

    @Test
    void requestToEntity_DeveConverterCorretamente() {
        FormaPagamento result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(request.getTipo(), result.getTipo());
        assertEquals(request.getParcelas(), result.getParcelas());
    }

    @Test
    void entityToDto_DeveConverterCorretamente() {
        FormaPagamentoDTO result = mapper.entityToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getTipo(), result.getTipo());
        assertEquals(entity.getParcelas(), result.getParcelas());
    }

    @Test
    void requestToEntity_ComRequestNull_DeveRetornarNull() {
        FormaPagamento result = mapper.requestToEntity(null);
        assertNull(result);
    }

    @Test
    void requestToEntity_ComTipoParceladoLoja_DeveConverterCorretamente() {
        request.setTipo(TipoFormaPagamento.PARCELADO_LOJA);
        request.setParcelas(3);

        FormaPagamento result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(TipoFormaPagamento.PARCELADO_LOJA, result.getTipo());
        assertEquals(3, result.getParcelas());
    }

    @Test
    void requestToEntity_ComTipoParceladoEmissor_DeveConverterCorretamente() {
        request.setTipo(TipoFormaPagamento.PARCELADO_EMISSOR);
        request.setParcelas(12);

        FormaPagamento result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(TipoFormaPagamento.PARCELADO_EMISSOR, result.getTipo());
        assertEquals(12, result.getParcelas());
    }

    @Test
    void entityToDto_ComDiferentesTipos_DeveConverterCorretamente() {
        entity.setTipo(TipoFormaPagamento.PARCELADO_LOJA);
        entity.setParcelas(6);

        FormaPagamentoDTO result = mapper.entityToDto(entity);

        assertNotNull(result);
        assertEquals(TipoFormaPagamento.PARCELADO_LOJA, result.getTipo());
        assertEquals(6, result.getParcelas());
    }

    @Test
    void requestToEntity_ComTipoNull_DeveManterNull() {
        request.setTipo(null);

        FormaPagamento result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertNull(result.getTipo());
        assertEquals(request.getParcelas(), result.getParcelas());
    }

    @Test
    void requestToEntity_ComParcelasNull_DeveManterNull() {
        request.setParcelas(null);

        FormaPagamento result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(request.getTipo(), result.getTipo());
        assertNull(result.getParcelas());
    }

    @Test
    void entityToDto_ComCamposNulos_DeveManterNulos() {
        entity.setTipo(null);
        entity.setParcelas(null);

        FormaPagamentoDTO result = mapper.entityToDto(entity);

        assertNotNull(result);
        assertNull(result.getTipo());
        assertNull(result.getParcelas());
    }

}
