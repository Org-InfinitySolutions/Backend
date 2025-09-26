package com.infinitysolutions.applicationservice.core.domain.valueobject;

import com.infinitysolutions.applicationservice.core.exception.DocumentoInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar um CNPJ válido.
 * 
 * Realiza validações estruturais e matemáticas completas:
 * - Deve conter exatamente 14 dígitos
 * - Não pode ter todos os dígitos iguais
 * - Deve ter dígitos verificadores corretos
 * 
 * Aceita entrada com ou sem formatação (pontos, barras e traços).
 */
public final class Cnpj {
    
    private static final Pattern PATTERN_APENAS_DIGITOS_14 = Pattern.compile("^\\d{14}$");
    private static final Pattern PATTERN_DIGITOS_REPETIDOS = Pattern.compile("(\\d)\\1{13}");
    
    private final String valor;
    
    private Cnpj(String valor) {
        this.valor = valor;
    }
    
    /**
     * Cria um novo CNPJ a partir de uma string.
     * 
     * @param cnpj String contendo o CNPJ (com ou sem formatação)
     * @return Instância de CNPJ válida
     * @throws DocumentoInvalidoException se o CNPJ for inválido
     */
    public static Cnpj of(String cnpj) {
        if (cnpj == null || cnpj.isBlank()) {
            throw DocumentoInvalidoException.cnpjInvalido("CNPJ não pode ser nulo ou vazio");
        }
        
        String cnpjLimpo = limparFormatacao(cnpj);
        validarEstrutura(cnpjLimpo);
        
        return new Cnpj(cnpjLimpo);
    }
    
    /**
     * Verifica se uma string representa um CNPJ válido.
     * 
     * @param cnpj String a ser validada
     * @return true se o CNPJ for válido, false caso contrário
     */
    public static boolean isValido(String cnpj) {
        try {
            of(cnpj);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private static String limparFormatacao(String cnpj) {
        return cnpj.replaceAll("[.\\-/\\s]", "");
    }
    
    private static void validarEstrutura(String cnpjLimpo) {
        if (!PATTERN_APENAS_DIGITOS_14.matcher(cnpjLimpo).matches()) {
            throw DocumentoInvalidoException.cnpjInvalido("CNPJ deve conter exatamente 14 dígitos numéricos");
        }
        
        if (PATTERN_DIGITOS_REPETIDOS.matcher(cnpjLimpo).matches()) {
            throw DocumentoInvalidoException.cnpjInvalido("CNPJ não pode ter todos os dígitos iguais");
        }
    }
    
    
    /**
     * Retorna o CNPJ sem formatação (apenas dígitos).
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Retorna o CNPJ formatado (XX.XXX.XXX/XXXX-XX).
     */
    public String getValorFormatado() {
        return String.format("%s.%s.%s/%s-%s",
                valor.substring(0, 2),
                valor.substring(2, 5),
                valor.substring(5, 8),
                valor.substring(8, 12),
                valor.substring(12, 14));
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cnpj cnpj = (Cnpj) obj;
        return Objects.equals(valor, cnpj.valor);
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
