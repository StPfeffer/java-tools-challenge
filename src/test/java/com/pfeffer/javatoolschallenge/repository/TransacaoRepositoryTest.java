package com.pfeffer.javatoolschallenge.repository;

import com.pfeffer.javatoolschallenge.domain.entity.DescricaoTransacao;
import com.pfeffer.javatoolschallenge.domain.entity.FormaPagamento;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.enums.StatusTransacao;
import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoRepositoryTest {

    private TransacaoRepository repository;

    private Transacao transacao1;

    private Transacao transacao2;

    @BeforeEach
    void setUp() {
        repository = new TransacaoRepository();

        DescricaoTransacao descricao1 = DescricaoTransacao.builder()
                .valor(new BigDecimal("500.50"))
                .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                .estabelecimento("PetShop Mundo cão")
                .nsu("1234567890")
                .codigoAutorizacao("147258369")
                .status(StatusTransacao.AUTORIZADO)
                .build();

        FormaPagamento formaPagamento1 = FormaPagamento.builder()
                .tipo(TipoFormaPagamento.AVISTA)
                .parcelas(1)
                .build();

        transacao1 = Transacao.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(descricao1)
                .formaPagamento(formaPagamento1)
                .build();

        DescricaoTransacao descricao2 = DescricaoTransacao.builder()
                .valor(new BigDecimal("1200.00"))
                .dataHora(LocalDateTime.of(2021, 5, 2, 14, 20, 0))
                .estabelecimento("Loja de Roupas")
                .nsu("9876543210")
                .codigoAutorizacao("987654321")
                .status(StatusTransacao.AUTORIZADO)
                .build();

        FormaPagamento formaPagamento2 = FormaPagamento.builder()
                .tipo(TipoFormaPagamento.PARCELADO_LOJA)
                .parcelas(3)
                .build();

        transacao2 = Transacao.builder()
                .cartao("5555********5678")
                .id("2000235689000002")
                .descricao(descricao2)
                .formaPagamento(formaPagamento2)
                .build();
    }

    @Test
    void save_DeveArmazenarTransacao() {
        Transacao resultado = repository.save(transacao1);

        assertNotNull(resultado);
        assertEquals(transacao1.getId(), resultado.getId());
        assertEquals(transacao1.getCartao(), resultado.getCartao());
        assertEquals(transacao1.getDescricao(), resultado.getDescricao());
        assertEquals(transacao1.getFormaPagamento(), resultado.getFormaPagamento());
    }

    @Test
    void save_DeveSubstituirTransacaoExistente() {
        repository.save(transacao1);

        Transacao transacaoAtualizada = Transacao.builder()
                .cartao("4444********1234")
                .id("1000235689000001")
                .descricao(DescricaoTransacao.builder()
                        .valor(new BigDecimal("600.00"))
                        .dataHora(LocalDateTime.of(2021, 5, 1, 18, 30, 0))
                        .estabelecimento("PetShop Mundo cão")
                        .nsu("1234567890")
                        .codigoAutorizacao("147258369")
                        .status(StatusTransacao.CANCELADO)
                        .build())
                .formaPagamento(transacao1.getFormaPagamento())
                .build();

        Transacao resultado = repository.save(transacaoAtualizada);

        assertNotNull(resultado);
        assertEquals(transacaoAtualizada.getId(), resultado.getId());
        assertEquals(new BigDecimal("600.00"), resultado.getDescricao().getValor());
        assertEquals(StatusTransacao.CANCELADO, resultado.getDescricao().getStatus());

        Optional<Transacao> encontrada = repository.findById("1000235689000001");
        assertTrue(encontrada.isPresent());
        assertEquals(new BigDecimal("600.00"), encontrada.get().getDescricao().getValor());
    }

    @Test
    void findById_ComIdExistente_DeveRetornarTransacao() {
        repository.save(transacao1);

        Optional<Transacao> resultado = repository.findById("1000235689000001");

        assertTrue(resultado.isPresent());
        assertEquals(transacao1.getId(), resultado.get().getId());
        assertEquals(transacao1.getCartao(), resultado.get().getCartao());
    }

    @Test
    void findById_ComIdInexistente_DeveRetornarEmpty() {
        Optional<Transacao> resultado = repository.findById("id-inexistente");

        assertFalse(resultado.isPresent());
    }

    @Test
    void findById_ComIdNull_DeveRetornarEmpty() {
        Optional<Transacao> resultado = repository.findById(null);

        assertFalse(resultado.isPresent());
    }

    @Test
    void findAll_ComRepositorioVazio_DeveRetornarListaVazia() {
        List<Transacao> resultado = repository.findAll();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void findAll_ComTransacoesArmazenadas_DeveRetornarTodasTransacoes() {
        repository.save(transacao1);
        repository.save(transacao2);

        List<Transacao> resultado = repository.findAll();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        assertTrue(resultado.stream().anyMatch(t -> t.getId().equals("1000235689000001")));
        assertTrue(resultado.stream().anyMatch(t -> t.getId().equals("2000235689000002")));
    }

    @Test
    void findAll_DeveRetornarNovaInstanciaLista() {
        repository.save(transacao1);

        List<Transacao> lista1 = repository.findAll();
        List<Transacao> lista2 = repository.findAll();

        assertNotSame(lista1, lista2);
        assertEquals(lista1.size(), lista2.size());
    }

    @Test
    void existsById_ComIdExistente_DeveRetornarTrue() {
        repository.save(transacao1);

        boolean existe = repository.existsById("1000235689000001");

        assertTrue(existe);
    }

    @Test
    void existsById_ComIdInexistente_DeveRetornarFalse() {
        boolean existe = repository.existsById("id-inexistente");

        assertFalse(existe);
    }

    @Test
    void existsById_ComIdNull_DeveRetornarFalse() {
        boolean existe = repository.existsById(null);

        assertFalse(existe);
    }

    @Test
    void deleteById_ComIdExistente_DeveRemoverTransacao() {
        repository.save(transacao1);
        repository.save(transacao2);
        assertTrue(repository.existsById("1000235689000001"));

        repository.deleteById("1000235689000001");

        assertFalse(repository.existsById("1000235689000001"));
        assertTrue(repository.existsById("2000235689000002"));

        Optional<Transacao> resultado = repository.findById("1000235689000001");
        assertFalse(resultado.isPresent());
    }

    @Test
    void deleteById_ComIdInexistente_NaoDeveLancarExcecao() {
        repository.save(transacao1);

        assertDoesNotThrow(() -> repository.deleteById("id-inexistente"));

        assertTrue(repository.existsById("1000235689000001"));
    }

    @Test
    void deleteById_ComIdNull_NaoDeveLancarExcecao() {
        repository.save(transacao1);

        assertDoesNotThrow(() -> repository.deleteById(null));

        assertTrue(repository.existsById("1000235689000001"));
    }

    @Test
    void operacoesSequenciais_DeveFuncionarCorretamente() {
        repository.save(transacao1);
        assertTrue(repository.existsById("1000235689000001"));
        assertEquals(1, repository.findAll().size());

        repository.save(transacao2);
        assertTrue(repository.existsById("2000235689000002"));
        assertEquals(2, repository.findAll().size());

        transacao1.getDescricao().setStatus(StatusTransacao.CANCELADO);
        repository.save(transacao1);
        assertEquals(2, repository.findAll().size());

        Optional<Transacao> atualizada = repository.findById("1000235689000001");
        assertTrue(atualizada.isPresent());
        assertEquals(StatusTransacao.CANCELADO, atualizada.get().getDescricao().getStatus());

        repository.deleteById("1000235689000001");
        assertFalse(repository.existsById("1000235689000001"));
        assertTrue(repository.existsById("2000235689000002"));
        assertEquals(1, repository.findAll().size());

        repository.deleteById("2000235689000002");
        assertEquals(0, repository.findAll().size());
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void save_ComTransacaoNull_DeveLancarException() {
        assertThrows(NullPointerException.class, () -> repository.save(null));
    }

}
