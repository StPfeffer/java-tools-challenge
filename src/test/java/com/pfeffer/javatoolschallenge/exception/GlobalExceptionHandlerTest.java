package com.pfeffer.javatoolschallenge.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleTransacaoNotFoundException_DeveRetornarNotFound() {
        String mensagem = "Transação não encontrada com ID: 123";
        TransacaoNotFoundException exception = new TransacaoNotFoundException(mensagem);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTransacaoNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(mensagem, errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
        assertTrue(errorResponse.getTimestamp() > 0);
        assertTrue(System.currentTimeMillis() - errorResponse.getTimestamp() < 1000);
    }

    @Test
    void handleTransacaoJaExisteException_DeveRetornarConflict() {
        String mensagem = "Transação com ID 123 já existe";
        TransacaoJaExisteException exception = new TransacaoJaExisteException(mensagem);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTransacaoJaExisteException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(mensagem, errorResponse.getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatus());
        assertTrue(errorResponse.getTimestamp() > 0);
        assertTrue(System.currentTimeMillis() - errorResponse.getTimestamp() < 1000);
    }

    @Test
    void handleIllegalArgumentException_DeveRetornarBadRequest() {
        String mensagem = "Parâmetro inválido fornecido";
        IllegalArgumentException exception = new IllegalArgumentException(mensagem);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(mensagem, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertTrue(errorResponse.getTimestamp() > 0);
        assertTrue(System.currentTimeMillis() - errorResponse.getTimestamp() < 1000);
    }

    @Test
    void handleIllegalStateException_DeveRetornarBadRequest() {
        String mensagem = "Apenas transações autorizadas podem ser estornadas";
        IllegalStateException exception = new IllegalStateException(mensagem);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalStateException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals(mensagem, errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
        assertTrue(errorResponse.getTimestamp() > 0);
        assertTrue(System.currentTimeMillis() - errorResponse.getTimestamp() < 1000);
    }

    @Test
    void handleGenericException_DeveRetornarInternalServerError() {
        String mensagemOriginal = "Erro inesperado do sistema";
        RuntimeException exception = new RuntimeException(mensagemOriginal);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Erro interno do servidor", errorResponse.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
        assertTrue(errorResponse.getTimestamp() > 0);
        assertTrue(System.currentTimeMillis() - errorResponse.getTimestamp() < 1000);
    }

    @Test
    void handleTransacaoNotFoundException_ComMensagemNull_DeveRetornarNotFound() {
        TransacaoNotFoundException exception = new TransacaoNotFoundException(null);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTransacaoNotFoundException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNull(errorResponse.getMessage());
        assertEquals(HttpStatus.NOT_FOUND.value(), errorResponse.getStatus());
    }

    @Test
    void handleTransacaoJaExisteException_ComMensagemNull_DeveRetornarConflict() {
        TransacaoJaExisteException exception = new TransacaoJaExisteException(null);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTransacaoJaExisteException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertNull(errorResponse.getMessage());
        assertEquals(HttpStatus.CONFLICT.value(), errorResponse.getStatus());
    }

    @Test
    void handleIllegalArgumentException_ComMensagemVazia_DeveRetornarBadRequest() {
        IllegalArgumentException exception = new IllegalArgumentException("");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("", errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
    }

    @Test
    void handleIllegalStateException_ComMensagemVazia_DeveRetornarBadRequest() {
        IllegalStateException exception = new IllegalStateException("");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleIllegalStateException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("", errorResponse.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), errorResponse.getStatus());
    }

    @Test
    void handleGenericException_ComNullPointerException_DeveRetornarInternalServerError() {
        NullPointerException exception = new NullPointerException("Tentativa de acessar objeto nulo");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Erro interno do servidor", errorResponse.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
    }

    @Test
    void timestampDeveTerValorProximoAoAtual() {
        long timestampAntes = System.currentTimeMillis();
        TransacaoNotFoundException exception = new TransacaoNotFoundException("Teste");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTransacaoNotFoundException(exception);
        long timestampDepois = System.currentTimeMillis();

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertTrue(errorResponse.getTimestamp() >= timestampAntes);
        assertTrue(errorResponse.getTimestamp() <= timestampDepois);
    }

    @Test
    void errorResponseDeveSerSerializavel() {
        TransacaoNotFoundException exception = new TransacaoNotFoundException("Teste de serialização");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTransacaoNotFoundException(exception);

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);

        String errorString = errorResponse.toString();
        assertNotNull(errorString);
        assertTrue(errorString.contains("Teste de serialização"));
        assertTrue(errorString.contains("404"));
    }

    @Test
    void handleGenericException_ComExceptionCustomizada_DeveRetornarInternalServerError() {
        Exception customException = new Exception("Erro customizado específico") {
            @Override
            public String getMessage() {
                return "Mensagem customizada que não deve aparecer na resposta";
            }
        };

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(customException);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        ErrorResponse errorResponse = response.getBody();
        assertNotNull(errorResponse);
        assertEquals("Erro interno do servidor", errorResponse.getMessage());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorResponse.getStatus());
    }

}
