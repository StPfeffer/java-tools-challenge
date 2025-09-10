package com.pfeffer.javatoolschallenge.service;

import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.TransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.entity.DescricaoTransacao;
import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import com.pfeffer.javatoolschallenge.domain.enums.StatusTransacao;
import com.pfeffer.javatoolschallenge.domain.enums.TipoFormaPagamento;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;
import com.pfeffer.javatoolschallenge.exception.TransacaoJaExisteException;
import com.pfeffer.javatoolschallenge.exception.TransacaoNotFoundException;
import com.pfeffer.javatoolschallenge.mapper.PagamentoMapper;
import com.pfeffer.javatoolschallenge.mapper.TransacaoMapper;
import com.pfeffer.javatoolschallenge.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final TransacaoRepository transacaoRepository;

    private final RandomGenerator random = new SecureRandom();

    private static void validarFormaPagamento(Transacao transacao) {
        TipoFormaPagamento tipo = transacao.getFormaPagamento().getTipo();
        Integer parcelas = transacao.getFormaPagamento().getParcelas();

        if (TipoFormaPagamento.AVISTA == tipo && !parcelas.equals(1)) {
            throw new IllegalArgumentException("Pagamento à vista deve ter apenas 1 parcela");
        }

        if (!(TipoFormaPagamento.AVISTA == tipo) && parcelas <= 1) {
            throw new IllegalArgumentException("Pagamento parcelado deve ter mais de 1 parcela");
        }
    }

    public PagamentoDTO processarPagamento(PagamentoRequest request) {
        Transacao transacao = PagamentoMapper.requestToEntity(request);

        if (transacaoRepository.existsById(transacao.getId())) {
            throw new TransacaoJaExisteException("Transação com ID " + transacao.getId() + " já existe");
        }

        validarFormaPagamento(transacao);

        boolean autorizado = simularAutorizacao();

        enriquecerTransacao(transacao, autorizado);

        transacaoRepository.save(transacao);

        return PagamentoMapper.entityToDto(transacao);
    }

    public PagamentoDTO consultarTransacao(String id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new TransacaoNotFoundException("Transação não encontrada com ID: " + id));

        return PagamentoMapper.entityToDto(transacao);
    }

    public List<TransacaoDTO> consultarTodasTransacoes() {
        return transacaoRepository.findAll().stream()
                .map(TransacaoMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public PagamentoDTO estornarTransacao(String id) {
        Transacao transacao = transacaoRepository.findById(id)
                .orElseThrow(() -> new TransacaoNotFoundException("Transação não encontrada com ID: " + id));

        if (!(StatusTransacao.AUTORIZADO == transacao.getDescricao().getStatus())) {
            throw new IllegalStateException("Apenas transações autorizadas podem ser estornadas");
        }

        transacao.getDescricao().setStatus(StatusTransacao.CANCELADO);

        transacaoRepository.save(transacao);

        return PagamentoMapper.entityToDto(transacao);
    }

    private boolean simularAutorizacao() {
        return random.nextInt(100) < 90;
    }

    private void enriquecerTransacao(Transacao transacao, boolean autorizado) {
        DescricaoTransacao descricao = transacao.getDescricao();

        descricao.setStatus(autorizado ? StatusTransacao.AUTORIZADO : StatusTransacao.NEGADO);

        if (autorizado) {
            descricao.setNsu(gerarNsu());
            descricao.setCodigoAutorizacao(gerarCodigoAutorizacao());
        }

        if (descricao.getDataHora() == null) {
            descricao.setDataHora(LocalDateTime.now());
        }
    }

    private String gerarNsu() {
        return String.valueOf(1000000000L + random.nextInt(900000000));
    }

    private String gerarCodigoAutorizacao() {
        return String.valueOf(100000000L + random.nextInt(900000000));
    }

}
