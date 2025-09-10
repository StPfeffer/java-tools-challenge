package com.pfeffer.javatoolschallenge.exception;

import java.io.Serial;

public class TransacaoNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public TransacaoNotFoundException(String message) {
        super(message);
    }

}
