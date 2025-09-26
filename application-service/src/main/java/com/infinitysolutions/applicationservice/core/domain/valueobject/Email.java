package com.infinitysolutions.applicationservice.core.domain.valueobject;

import com.infinitysolutions.applicationservice.core.exception.DocumentoInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar um endereço de email válido.
 * 
 * Realiza validações estruturais completas:
 * - Formato RFC 5322 simplificado
 * - Não pode ser nulo ou vazio
 * - Normalização para minúsculas
 * - Domínio com pelo menos 2 caracteres na extensão
 * 
 * O email é automaticamente normalizado para minúsculas.
 */
public final class Email {
    
    private static final Pattern PATTERN_EMAIL = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private final String valor;
    
    private Email(String valor) {
        this.valor = valor;
    }
    
    /**
     * Cria um novo Email a partir de uma string.
     * 
     * @param email String contendo o endereço de email
     * @return Instância de Email válida
     * @throws DocumentoInvalidoException se o email for inválido
     */
    public static Email of(String email) {
        if (email == null || email.isBlank()) {
            throw DocumentoInvalidoException.emailInvalido("não pode ser nulo ou vazio");
        }
        
        String emailNormalizado = email.trim().toLowerCase();
        validarFormato(emailNormalizado);
        return new Email(emailNormalizado);
    }
    
    /**
     * Verifica se uma string representa um email válido.
     * 
     * @param email String a ser validada
     * @return true se o email for válido, false caso contrário
     */
    public static boolean isValid(String email) {
        try {
            of(email);
            return true;
        } catch (DocumentoInvalidoException e) {
            return false;
        }
    }
    
    /**
     * Retorna o valor do email normalizado (minúsculas).
     * 
     * @return String com o email
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Extrai o domínio do email.
     * 
     * @return String com o domínio
     */
    public String getDominio() {
        return valor.substring(valor.indexOf('@') + 1);
    }
    
    /**
     * Extrai a parte local do email (antes do @).
     * 
     * @return String com a parte local
     */
    public String getParteLocal() {
        return valor.substring(0, valor.indexOf('@'));
    }
    
    private static void validarFormato(String email) {
        if (!PATTERN_EMAIL.matcher(email).matches()) {
            throw DocumentoInvalidoException.emailInvalido("formato inválido");
        }
        
        if (email.length() > 254) {
            throw DocumentoInvalidoException.emailInvalido("excede o tamanho máximo permitido (254 caracteres)");
        }
        
        String parteLocal = email.substring(0, email.indexOf('@'));
        if (parteLocal.length() > 64) {
            throw DocumentoInvalidoException.emailInvalido("parte antes do @ excede o tamanho máximo permitido (64 caracteres)");
        }
        
        // Verificar se não há pontos consecutivos
        if (email.contains("..")) {
            throw DocumentoInvalidoException.emailInvalido("não pode conter pontos consecutivos");
        }
        
        // Verificar se não começa ou termina com ponto na parte local
        if (parteLocal.startsWith(".") || parteLocal.endsWith(".")) {
            throw DocumentoInvalidoException.emailInvalido("parte antes do @ não pode começar ou terminar com ponto");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(valor, email.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return valor;
    }
}
