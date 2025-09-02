package com.infinitysolutions.applicationservice.core.exception;

/**
 * Exceção lançada quando um documento (CPF, CNPJ, RG) ou credencial (Email, Senha) é inválido.
 * Esta exceção é tratada pelo GlobalExceptionHandler com status HTTP 400 (Bad Request).
 */
public class DocumentoInvalidoException extends CoreLayerException {
    
    public DocumentoInvalidoException(String message) {
        super("documento_invalido", message);
    }
    
    public static DocumentoInvalidoException cpfInvalido(String motivo) {
        return new DocumentoInvalidoException("CPF inválido: " + motivo);
    }
    
    public static DocumentoInvalidoException cnpjInvalido(String motivo) {
        return new DocumentoInvalidoException("CNPJ inválido: " + motivo);
    }
    
    public static DocumentoInvalidoException rgInvalido(String motivo) {
        return new DocumentoInvalidoException("RG inválido: " + motivo);
    }
    
    public static DocumentoInvalidoException emailInvalido(String motivo) {
        return new DocumentoInvalidoException("Email inválido: " + motivo);
    }
    
    public static DocumentoInvalidoException senhaInvalida(String motivo) {
        return new DocumentoInvalidoException("Senha inválida: " + motivo);
    }
}
