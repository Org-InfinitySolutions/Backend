package com.infinitysolutions.applicationservice.core.domain.valueobject;

import com.infinitysolutions.applicationservice.core.exception.DocumentoInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar um RG válido.
 * 
 * Aceita diferentes formatos de RG brasileiros:
 * - Formato padrão: XX.XXX.XXX-X
 * - Apenas números: XXXXXXXX ou XXXXXXXXX
 * - Permite dígito verificador X (maiúsculo ou minúsculo)
 * 
 * O RG no Brasil varia entre estados, portanto a validação é mais flexível
 * focando no formato estrutural básico.
 */
public final class Rg {
    
    // Aceita formatos: XX.XXX.XXX-X, XXXXXXXX, XXXXXXXXX
    private static final Pattern PATTERN_FORMATADO = Pattern.compile("^[0-9]{1,2}(\\.[0-9]{3}){2}-[0-9xX]$");
    private static final Pattern PATTERN_NUMERICO = Pattern.compile("^[0-9]{8,9}$");
    private static final Pattern PATTERN_NUMERICO_COM_X = Pattern.compile("^[0-9]{7,8}[0-9xX]$");
    
    private final String valor;
    
    private Rg(String valor) {
        this.valor = valor;
    }
    
    /**
     * Cria um novo RG a partir de uma string.
     * 
     * @param rg String contendo o RG (com ou sem formatação)
     * @return Instância de RG válida
     * @throws com.infinitysolutions.applicationservice.core.exception.DocumentoInvalidoException se o RG for inválido
     */
    public static Rg of(String rg) {
        if (rg == null || rg.isBlank()) {
            throw DocumentoInvalidoException.rgInvalido("RG não pode ser nulo ou vazio");
        }
        
        String rgTrimmed = rg.trim();
        validarFormato(rgTrimmed);
        
        // Armazena o valor normalizado (apenas dígitos e X se houver)
        String rgLimpo = limparFormatacao(rgTrimmed);
        
        return new Rg(rgLimpo.toUpperCase());
    }
    
    /**
     * Verifica se uma string representa um RG válido.
     * 
     * @param rg String a ser validada
     * @return true se o RG for válido, false caso contrário
     */
    public static boolean isValido(String rg) {
        try {
            of(rg);
            return true;
        } catch (DocumentoInvalidoException e) {
            return false;
        }
    }
    
    private static void validarFormato(String rg) {
        boolean formatoFormatado = PATTERN_FORMATADO.matcher(rg).matches();
        boolean formatoNumerico = PATTERN_NUMERICO.matcher(rg).matches();
        boolean formatoNumericoComX = PATTERN_NUMERICO_COM_X.matcher(rg).matches();
        
        if (!formatoFormatado && !formatoNumerico && !formatoNumericoComX) {
            throw DocumentoInvalidoException.rgInvalido(
                "Formato de RG inválido. Formatos aceitos: XX.XXX.XXX-X, XXXXXXXX ou XXXXXXXXX"
            );
        }
    }
    
    private static String limparFormatacao(String rg) {
        return rg.replaceAll("[.\\-\\s]", "");
    }
    
    /**
     * Retorna o RG sem formatação (apenas dígitos e X se houver).
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Retorna o RG formatado (XX.XXX.XXX-X).
     * Se o RG tiver menos de 9 caracteres, será preenchido com zeros à esquerda.
     */
    public String getValorFormatado() {
        String rgPadded = String.format("%9s", valor).replace(' ', '0');
        
        return String.format("%s.%s.%s-%s",
                rgPadded.substring(0, 2),
                rgPadded.substring(2, 5),
                rgPadded.substring(5, 8),
                rgPadded.substring(8, 9));
    }
    
    /**
     * Retorna true se o RG possui dígito verificador X.
     */
    public boolean possuiDigitoX() {
        return valor.endsWith("X");
    }
    
    /**
     * Retorna o número de dígitos do RG (excluindo o X se houver).
     */
    public int getTamanho() {
        return possuiDigitoX() ? valor.length() - 1 : valor.length();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Rg rg = (Rg) obj;
        return Objects.equals(valor, rg.valor);
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
