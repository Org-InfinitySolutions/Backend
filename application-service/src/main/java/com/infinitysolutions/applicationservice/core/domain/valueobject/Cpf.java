package com.infinitysolutions.applicationservice.core.domain.valueobject;

import com.infinitysolutions.applicationservice.core.exception.DocumentoInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar um CPF válido.
 * 
 * Realiza validações estruturais e matemáticas completas:
 * - Deve conter exatamente 11 dígitos
 * - Não pode ter todos os dígitos iguais
 * - Deve ter dígitos verificadores corretos
 * 
 * Aceita entrada com ou sem formatação (pontos e traços).
 */
public final class Cpf {
    
    private static final Pattern PATTERN_APENAS_DIGITOS_11 = Pattern.compile("^\\d{11}$");
    private static final Pattern PATTERN_DIGITOS_REPETIDOS = Pattern.compile("(\\d)\\1{10}");
    
    private final String valor;
    
    private Cpf(String valor) {
        this.valor = valor;
    }
    
    /**
     * Cria um novo CPF a partir de uma string.
     * 
     * @param cpf String contendo o CPF (com ou sem formatação)
     * @return Instância de CPF válida
     * @throws DocumentoInvalidoException se o CPF for inválido
     */
    public static Cpf of(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            throw DocumentoInvalidoException.cpfInvalido("não pode ser nulo ou vazio");
        }
        
        String cpfLimpo = limparFormatacao(cpf);
        validarEstrutura(cpfLimpo);
        return new Cpf(cpfLimpo);
    }
    
    /**
     * Verifica se uma string representa um CPF válido.
     * 
     * @param cpf String a ser validada
     * @return true se o CPF for válido, false caso contrário
     */
    public static boolean isValido(String cpf) {
        try {
            of(cpf);
            return true;
        } catch (DocumentoInvalidoException e) {
            return false;
        }
    }
    
    private static String limparFormatacao(String cpf) {
        return cpf.replaceAll("[.\\-\\s]", "");
    }
    
    private static void validarEstrutura(String cpfLimpo) {
        if (!PATTERN_APENAS_DIGITOS_11.matcher(cpfLimpo).matches()) {
            throw DocumentoInvalidoException.cpfInvalido("deve conter exatamente 11 dígitos numéricos");
        }
        
        if (PATTERN_DIGITOS_REPETIDOS.matcher(cpfLimpo).matches()) {
            throw DocumentoInvalidoException.cpfInvalido("não pode ter todos os dígitos iguais");
        }
    }
    
    
    /**
     * Retorna o CPF sem formatação (apenas dígitos).
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Retorna o CPF formatado (XXX.XXX.XXX-XX).
     */
    public String getValorFormatado() {
        return String.format("%s.%s.%s-%s",
                valor.substring(0, 3),
                valor.substring(3, 6),
                valor.substring(6, 9),
                valor.substring(9, 11));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cpf cpf = (Cpf) obj;
        return Objects.equals(valor, cpf.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return getValorFormatado();
    }
}
