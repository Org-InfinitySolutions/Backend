package com.infinitysolutions.authservice.infra.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthServiceException.class)
    public ResponseEntity<ErrorResponse> handleAuthServiceException(AuthServiceException ex, HttpServletRequest request) {
        log.error("Exceção de serviço: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(getStatusForException(ex).value())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(getStatusForException(ex)).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message("Campos inválidos na requisição")
                .path(request.getRequestURI())
                .build();

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach( fieldError -> errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage()));

        log.error("Erro de validação: {}", errorResponse.getValidationErrors());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        String userFriendlyMessage = "Formato de JSON inválido";

        if (message != null && message.contains("UUID")) {
            userFriendlyMessage = "O formato do ID é inválido. Utilize um UUID válido no formato: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx";
        }

        log.error("Erro de desserialização: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("formato_invalido" + userFriendlyMessage)
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private HttpStatus getStatusForException(AuthServiceException ex) {
        if (ex instanceof RecursoExistenteException) {
            return HttpStatus.CONFLICT;
        } else if (ex instanceof RecursoNaoEncontradoException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof AutenticacaoException) {
            return HttpStatus.UNAUTHORIZED;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}