package com.pfeffer.javatoolschallenge.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private String message;

    private int status;

    private long timestamp;

}
