package com.infinitysolutions.applicationservice.core.domain.valueobject;

import com.infinitysolutions.applicationservice.core.exception.DocumentoInvalidoException;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para representar uma senha válida.
 * 
 * Realiza validações de segurança:
 * - Comprimento mínimo configurável (padrão: 8 caracteres)
 * - Deve conter pelo menos uma letra
 * - Deve conter pelo menos um dígito
 * - Opcionalmente requer letra maiúscula
 * - Opcionalmente requer caractere especial
 * - Não pode ser nula ou vazia
 *
 */
public final class Senha {
    
    private static final Pattern PATTERN_LETRA = Pattern.compile("[a-zA-Z]");
    private static final Pattern PATTERN_DIGITO = Pattern.compile("\\d");
    private static final Pattern PATTERN_MAIUSCULA = Pattern.compile("[A-Z]");
    private static final Pattern PATTERN_ESPECIAL = Pattern.compile("[!@#$%^&*(),.?\":{}|<>]");
    
    public static final int COMPRIMENTO_MINIMO_PADRAO = 8;
    
    private final String valor;
    
    private Senha(String valor) {
        this.valor = valor;
    }
    
    /**
     * Cria uma nova Senha com validações padrão (8+ caracteres, letras e números).
     * 
     * @param senha String contendo a senha
     * @return Instância de Senha válida
     * @throws DocumentoInvalidoException se a senha for inválida
     */
    public static Senha of(String senha) {
        return of(senha, COMPRIMENTO_MINIMO_PADRAO, false, false);
    }
    
    /**
     * Cria uma nova Senha com comprimento mínimo personalizado.
     * 
     * @param senha String contendo a senha
     * @param comprimentoMinimo Comprimento mínimo exigido
     * @return Instância de Senha válida
     * @throws DocumentoInvalidoException se a senha for inválida
     */
    public static Senha of(String senha, int comprimentoMinimo) {
        return of(senha, comprimentoMinimo, false, false);
    }
    
    /**
     * Cria uma nova Senha com validações personalizadas.
     * 
     * @param senha String contendo a senha
     * @param comprimentoMinimo Comprimento mínimo exigido
     * @param requerMaiuscula Se deve exigir pelo menos uma letra maiúscula
     * @param requerEspecial Se deve exigir pelo menos um caractere especial
     * @return Instância de Senha válida
     * @throws DocumentoInvalidoException se a senha for inválida
     */
    public static Senha of(String senha, int comprimentoMinimo, boolean requerMaiuscula, boolean requerEspecial) {
        if (senha == null || senha.isBlank()) {
            throw DocumentoInvalidoException.senhaInvalida("não pode ser nula ou vazia");
        }
        
        validarRequisitos(senha, comprimentoMinimo, requerMaiuscula, requerEspecial);
        return new Senha(senha);
    }
    
    /**
     * Cria uma nova Senha seguindo os padrões do sistema (8+ caracteres, letras, números e maiúscula).
     * 
     * @param senha String contendo a senha
     * @return Instância de Senha válida
     * @throws DocumentoInvalidoException se a senha for inválida
     */
    public static Senha ofSistema(String senha) {
        return of(senha, COMPRIMENTO_MINIMO_PADRAO, true, false);
    }
    
    /**
     * Verifica se uma string representa uma senha válida com requisitos padrão.
     * 
     * @param senha String a ser validada
     * @return true se a senha for válida, false caso contrário
     */
    public static boolean isValid(String senha) {
        try {
            of(senha);
            return true;
        } catch (DocumentoInvalidoException e) {
            return false;
        }
    }
    
    /**
     * Verifica se uma string representa uma senha válida com requisitos personalizados.
     * 
     * @param senha String a ser validada
     * @param comprimentoMinimo Comprimento mínimo exigido
     * @param requerMaiuscula Se deve exigir pelo menos uma letra maiúscula
     * @param requerEspecial Se deve exigir pelo menos um caractere especial
     * @return true se a senha for válida, false caso contrário
     */
    public static boolean isValid(String senha, int comprimentoMinimo, boolean requerMaiuscula, boolean requerEspecial) {
        try {
            of(senha, comprimentoMinimo, requerMaiuscula, requerEspecial);
            return true;
        } catch (DocumentoInvalidoException e) {
            return false;
        }
    }
    
    /**
     * Verifica se uma string representa uma senha válida seguindo os padrões do sistema.
     * 
     * @param senha String a ser validada
     * @return true se a senha for válida, false caso contrário
     */
    public static boolean isValidSistema(String senha) {
        try {
            ofSistema(senha);
            return true;
        } catch (DocumentoInvalidoException e) {
            return false;
        }
    }
    
    /**
     * Retorna o valor da senha.
     * ATENÇÃO: Use com cuidado. A senha deve ser criptografada antes de ser persistida.
     * 
     * @return String com a senha
     */
    public String getValor() {
        return valor;
    }
    
    /**
     * Retorna o comprimento da senha.
     * 
     * @return Número de caracteres da senha
     */
    public int getComprimento() {
        return valor.length();
    }
    
    /**
     * Verifica se a senha contém pelo menos uma letra maiúscula.
     * 
     * @return true se contém maiúscula, false caso contrário
     */
    public boolean temMaiuscula() {
        return PATTERN_MAIUSCULA.matcher(valor).find();
    }
    
    /**
     * Verifica se a senha contém pelo menos um caractere especial.
     * 
     * @return true se contém caractere especial, false caso contrário
     */
    public boolean temCaractereEspecial() {
        return PATTERN_ESPECIAL.matcher(valor).find();
    }
    
    /**
     * Verifica se a senha contém pelo menos um dígito.
     * 
     * @return true se contém dígito, false caso contrário
     */
    public boolean temDigito() {
        return PATTERN_DIGITO.matcher(valor).find();
    }
    
    /**
     * Verifica se a senha contém pelo menos uma letra.
     * 
     * @return true se contém letra, false caso contrário
     */
    public boolean temLetra() {
        return PATTERN_LETRA.matcher(valor).find();
    }
    
    private static void validarRequisitos(String senha, int comprimentoMinimo, boolean requerMaiuscula, boolean requerEspecial) {
        if (senha.length() < comprimentoMinimo) {
            throw DocumentoInvalidoException.senhaInvalida(
                String.format("deve ter pelo menos %d caracteres", comprimentoMinimo)
            );
        }
        
        if (!PATTERN_LETRA.matcher(senha).find()) {
            throw DocumentoInvalidoException.senhaInvalida("deve conter pelo menos uma letra");
        }
        
        if (!PATTERN_DIGITO.matcher(senha).find()) {
            throw DocumentoInvalidoException.senhaInvalida("deve conter pelo menos um dígito");
        }
        
        if (requerMaiuscula && !PATTERN_MAIUSCULA.matcher(senha).find()) {
            throw DocumentoInvalidoException.senhaInvalida("deve conter pelo menos uma letra maiúscula");
        }
        
        if (requerEspecial && !PATTERN_ESPECIAL.matcher(senha).find()) {
            throw DocumentoInvalidoException.senhaInvalida("deve conter pelo menos um caractere especial (!@#$%^&*(),.?\":{}|<>)");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Senha senha = (Senha) o;
        return Objects.equals(valor, senha.valor);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(valor);
    }
    
    @Override
    public String toString() {
        return "***";
    }
}
