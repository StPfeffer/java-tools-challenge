package com.pfeffer.javatoolschallenge.mapper;

import com.pfeffer.javatoolschallenge.domain.dto.DescricaoTransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.DescricaoTransacao;
import com.pfeffer.javatoolschallenge.domain.enums.StatusTransacao;
import com.pfeffer.javatoolschallenge.domain.request.DescricaoTransacaoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DescricaoTransacaoMapperTest {

    private DescricaoTransacaoMapper mapper;

    private DescricaoTransacaoRequest request;

    private DescricaoTransacao entity;

    @BeforeEach
    void setUp() {
        mapper = new DescricaoTransacaoMapper();

        request = DescricaoTransacaoRequest.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .build();

        entity = DescricaoTransacao.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .nsu("1234567890")
                .codigoAutorizacao("147258369")
                .status(StatusTransacao.AUTORIZADO)
                .build();
    }

    @Test
    void requestToEntity_DeveConverterCorretamente() {
        DescricaoTransacao result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(request.getValor(), result.getValor());
        assertEquals(request.getDataHora(), result.getDataHora());
        assertEquals(request.getEstabelecimento(), result.getEstabelecimento());
        assertNull(result.getNsu());
        assertNull(result.getCodigoAutorizacao());
        assertNull(result.getStatus());
    }

    @Test
    void entityToDto_DeveConverterCorretamente() {
        DescricaoTransacaoDTO result = mapper.entityToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getValor(), result.getValor());
        assertEquals(entity.getDataHora(), result.getDataHora());
        assertEquals(entity.getEstabelecimento(), result.getEstabelecimento());
        assertEquals(entity.getNsu(), result.getNsu());
        assertEquals(entity.getCodigoAutorizacao(), result.getCodigoAutorizacao());
        assertEquals(entity.getStatus(), result.getStatus());
    }

    @Test
    void requestToEntity_ComRequestNull_DeveRetornarNull() {
        DescricaoTransacao result = mapper.requestToEntity(null);
        assertNull(result);
    }

    @Test
    void requestToEntity_ComValorNull_DeveManterNull() {
        request.setValor(null);

        DescricaoTransacao result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertNull(result.getValor());
        assertEquals(request.getDataHora(), result.getDataHora());
        assertEquals(request.getEstabelecimento(), result.getEstabelecimento());
    }

    @Test
    void requestToEntity_ComDataHoraNull_DeveManterNull() {
        request.setDataHora(null);

        DescricaoTransacao result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(request.getValor(), result.getValor());
        assertNull(result.getDataHora());
        assertEquals(request.getEstabelecimento(), result.getEstabelecimento());
    }

    @Test
    void requestToEntity_ComEstabelecimentoNull_DeveManterNull() {
        request.setEstabelecimento(null);

        DescricaoTransacao result = mapper.requestToEntity(request);

        assertNotNull(result);
        assertEquals(request.getValor(), result.getValor());
        assertEquals(request.getDataHora(), result.getDataHora());
        assertNull(result.getEstabelecimento());
    }

    @Test
    void entityToDto_ComCamposNulos_DeveManterNulos() {
        entity.setNsu(null);
        entity.setCodigoAutorizacao(null);
        entity.setStatus(null);

        DescricaoTransacaoDTO result = mapper.entityToDto(entity);

        assertNotNull(result);
        assertEquals(entity.getValor(), result.getValor());
        assertEquals(entity.getDataHora(), result.getDataHora());
        assertEquals(entity.getEstabelecimento(), result.getEstabelecimento());
        assertNull(result.getNsu());
        assertNull(result.getCodigoAutorizacao());
        assertNull(result.getStatus());
    }

}
