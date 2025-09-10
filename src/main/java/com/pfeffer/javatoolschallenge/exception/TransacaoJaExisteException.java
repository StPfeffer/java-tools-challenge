package com.pfeffer.javatoolschallenge.exception;

import java.io.Serial;

public class TransacaoJaExisteException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TransacaoJaExisteException(String message) {
        super(message);
    }

}
