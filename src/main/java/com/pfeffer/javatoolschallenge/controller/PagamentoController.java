package com.pfeffer.javatoolschallenge.controller;

import com.pfeffer.javatoolschallenge.domain.dto.PagamentoDTO;
import com.pfeffer.javatoolschallenge.domain.dto.TransacaoDTO;
import com.pfeffer.javatoolschallenge.domain.request.PagamentoRequest;
import com.pfeffer.javatoolschallenge.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoDTO> processarPagamento(@RequestBody PagamentoRequest request) {
        try {
            PagamentoDTO response = pagamentoService.processarPagamento(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<PagamentoDTO> consultarTransacao(@PathVariable String id) {
        try {
            PagamentoDTO response = pagamentoService.consultarTransacao(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<TransacaoDTO>> consultarTodasTransacoes() {
        List<TransacaoDTO> transacoes = pagamentoService.consultarTodasTransacoes();
        return ResponseEntity.ok(transacoes);
    }

    @PutMapping("{id}/estorno")
    public ResponseEntity<PagamentoDTO> estornarTransacao(@PathVariable String id) {
        try {
            PagamentoDTO response = pagamentoService.estornarTransacao(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
