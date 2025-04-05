package com.infinitysolutions.applicationservice.infra.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Trata erros de validação de campos (anotações @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message("Campos inválidos na requisição")
                .path(request.getRequestURI())
                .build();
        
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        fieldErrors.forEach(fieldError -> 
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage())
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    // Trata erros de desserialização JSON, incluindo o campo discriminador de tipo
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Parse JSON")
                .path(request.getRequestURI())
                .build();
        
        Throwable cause = ex.getCause();
        
        if (cause instanceof InvalidTypeIdException) {
            // Erro específico quando o campo discriminador "tipo" tem valor inválido
            errorResponse.setMessage("Valor inválido para o campo 'tipo'. Valores permitidos: 'PF' (Pessoa Física) ou 'PJ' (Pessoa Jurídica)");
        } else if (cause instanceof MismatchedInputException) {
            // Erro quando um campo tem tipo de dado inválido
            errorResponse.setMessage("Formato de dados inválido: " + cause.getMessage());
        } else if (cause instanceof JsonParseException) {
            // Erro de sintaxe JSON
            errorResponse.setMessage("JSON inválido: " + cause.getMessage());
        } else if (cause instanceof JsonMappingException) {
            // Erro genérico de mapeamento JSON
            errorResponse.setMessage("Erro no mapeamento do JSON: " + cause.getMessage());
        } else {
            // Outros erros de leitura
            errorResponse.setMessage("Erro ao processar requisição: " + ex.getMessage());
        }
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    // Trata erros de validação de constraints (CPF, CNPJ, etc.)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(
            ConstraintViolationException ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Erro de Validação")
                .message("Violação de restrições de campos")
                .path(request.getRequestURI())
                .build();
        
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            // Remove "cadastrar.usuarioCadastroDTO." do início do caminho do campo para clareza
            fieldName = fieldName.replaceAll(".*\\.", "");
            errorResponse.addValidationError(fieldName, violation.getMessage());
        });
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    // Trata quaisquer outras exceções não capturadas
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(
            Exception ex, HttpServletRequest request) {
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Erro Interno")
                .message("Ocorreu um erro interno no servidor")
                .path(request.getRequestURI())
                .build();
        
        // Em ambiente de produção, não devemos expor detalhes de erros internos
        // Para debug/desenvolvimento, podemos ativar esta linha:
        // errorResponse.setMessage(ex.getMessage());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
