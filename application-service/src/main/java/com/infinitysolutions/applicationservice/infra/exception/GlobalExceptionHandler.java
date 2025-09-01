package com.infinitysolutions.applicationservice.infra.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.infinitysolutions.applicationservice.core.exception.CoreLayerException;
import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationServiceException.class)
    public ResponseEntity<ErrorResponse> handleApplicationServiceException(ApplicationServiceException ex, HttpServletRequest request) {
        log.error("Exceção de serviço: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(getStatusForException(ex).value())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(getStatusForException(ex)).body(errorResponse);
    }

    @ExceptionHandler(CoreLayerException.class)
    public ResponseEntity<ErrorResponse> handleCoreLayerException(CoreLayerException ex, HttpServletRequest request) {
        log.error("Exceção no core: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(getStatusForException(ex).value())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(getStatusForException(ex)).body(errorResponse);
    }

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
        log.error("Erro de validação: {}", errorResponse.getValidationErrors());
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
        } else if (ex.getMessage() != null && ex.getMessage().contains("UUID")) {
            errorResponse.setMessage("O formato do ID é inválido. Utilize um UUID válido no formato: xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
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
    
    // Trata erros de integridade de dados (ex: chave estrangeira, entrada duplicada)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, HttpServletRequest request) {
        
        log.error("Violação de integridade de dados: {}", ex.getMessage());
        
        String errorMessage = "Operação não permitida devido a restrições de integridade de dados";
        String rootCause = ex.getMostSpecificCause().getMessage();
        
        if (rootCause != null) {
            if (rootCause.contains("produto_pedido") && rootCause.contains("FK8kkew2u78bwq2d6cwbll9hx0g")) {
                errorMessage = "Não é possível excluir este produto pois ele está vinculado a um ou mais pedidos. Para excluir o produto, primeiro é necessário remover ou alterar os pedidos que o utilizam.";
            } else if (rootCause.contains("Duplicate entry")) {
                errorMessage = "Já existe um registro com essas informações no sistema";
            } else if (rootCause.contains("foreign key constraint")) {
                errorMessage = "Não é possível realizar esta operação pois existem registros relacionados no sistema";
            } else if (rootCause.contains("Duplicate entry") && rootCause.contains("email")) {
                errorMessage = "Email já está em uso";
            }
        }
        
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .error("VIOLACAO_INTEGRIDADE_DADOS")
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    // Trata quaisquer outras exceções não capturadas
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleGeneralException(
//            Exception ex, HttpServletRequest request) {
//
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .error("Erro Interno")
//                .message("Ocorreu um erro interno no servidor")
//                .path(request.getRequestURI())
//                .build();
////
////         Em ambiente de produção, não devemos expor detalhes de erros internos
////         Para debug/desenvolvimento, podemos ativar esta linha:
////         errorResponse.setMessage(ex.getMessage());
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<ErrorResponse> handleOperacaoNaoPermitida(OperacaoNaoPermitidaException ex, HttpServletRequest request) {
        ErrorResponse errorResponse =  ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .error("OPERACAO_NAO_PERMITIDA")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(AuthServiceCommunicationException.class)
    public ResponseEntity<ErrorResponse> handleAuthServiceCommunicationException(
            AuthServiceCommunicationException ex,
            HttpServletRequest request) {

        log.error("Exceção de serviço: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.SERVICE_UNAVAILABLE.value())
                    .error("Erro de Comunicação com o Serviço de Autenticação")
                    .message(ex.getMessage() + " Se o problema persistir, entre em contato com o suporte.")
                    .path(request.getRequestURI())
                    .build();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    private HttpStatus getStatusForException(ApplicationServiceException ex) {
        if (ex instanceof AuthServiceCommunicationException) {
            return HttpStatus.SERVICE_UNAVAILABLE;
        } else if (ex instanceof TokenExpiradoException || ex instanceof AutenticacaoException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof DocumentoInvalidoException || ex instanceof RecursoIncompativelException) {
            return HttpStatus.UNPROCESSABLE_ENTITY;
        } else if (ex instanceof DocumentoNaoEncontradoException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof VinculoExistenteException) {
            return HttpStatus.CONFLICT;
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private HttpStatus getStatusForException(CoreLayerException ex) {
    if (ex instanceof RecursoNaoEncontradoException) {
        return HttpStatus.NOT_FOUND;
    } else if ( ex instanceof RecursoExistenteException) {
        return HttpStatus.CONFLICT;
    }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @ExceptionHandler(AutenticacaoException.class)
    public ResponseEntity<ErrorResponse> handleAuthServiceException(AutenticacaoException ex, HttpServletRequest request) {
        log.error("Exceção do serviço de autenticação: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(getStatusForException(ex).value())
                .error(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(getStatusForException(ex).value()).body(errorResponse);
    }

    @ExceptionHandler(TokenExpiradoException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpirado(TokenExpiradoException ex, HttpServletRequest request) {
        log.warn("Token expirado detectado para o path: {}", request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .error("TOKEN_EXPIRADO")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}
