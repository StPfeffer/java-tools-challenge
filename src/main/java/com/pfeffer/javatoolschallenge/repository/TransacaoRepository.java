package com.pfeffer.javatoolschallenge.repository;

import com.pfeffer.javatoolschallenge.domain.entity.Transacao;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TransacaoRepository {

    private final Map<String, Transacao> transacoes = new HashMap<>();

    public Transacao save(Transacao transacao) {
        transacoes.put(transacao.getId(), transacao);
        return transacao;
    }

    public Optional<Transacao> findById(String id) {
        return Optional.ofNullable(transacoes.get(id));
    }

    public List<Transacao> findAll() {
        return new ArrayList<>(transacoes.values());
    }

    public boolean existsById(String id) {
        return transacoes.containsKey(id);
    }

    public void deleteById(String id) {
        transacoes.remove(id);
    }

}
