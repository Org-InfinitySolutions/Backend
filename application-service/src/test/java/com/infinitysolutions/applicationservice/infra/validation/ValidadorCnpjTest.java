package com.infinitysolutions.applicationservice.infra.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes unitários para a classe {@link ValidadorCnpj}
 * <p>
 * Estes testes verificam se o validador de CNPJ está funcionando corretamente
 * para todos os cenários relevantes, incluindo CNPJs com formatação correta (com e sem pontuação),
 * valores nulos ou vazios, CNPJs com tamanho incorreto e
 * CNPJs com caracteres inválidos. O validador verifica apenas o formato do CNPJ,
 * não sua validade matemática (que será feita posteriormente por uma API externa).
 * </p>
 */
@ExtendWith(MockitoExtension.class)
public class ValidadorCnpjTest {
    
    private ValidadorCnpj validador;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @BeforeEach
    public void setup() {
        validador = new ValidadorCnpj();
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CNPJs válidos com formatação")
    @ValueSource(strings = {
            "12.345.678/0001-95", 
            "11.222.333/0001-81", 
            "27.865.757/0001-02",
            "61.797.924/0001-50"
    })
    public void deveRetornarTrueParaCnpjValidoComFormatacao(String cnpj) {
        assertTrue(validador.isValid(cnpj, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CNPJs válidos sem formatação")
    @ValueSource(strings = {
            "12345678000195", 
            "11222333000181", 
            "27865757000102",
            "61797924000150"
    })
    public void deveRetornarTrueParaCnpjValidoSemFormatacao(String cnpj) {
        assertTrue(validador.isValid(cnpj, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CNPJs com formato válido mesmo com dígitos verificadores incorretos")
    @ValueSource(strings = {
            "12.345.678/0001-96", // Dígito verificador incorreto mas formato válido
            "11.222.333/0001-82", // Dígito verificador incorreto mas formato válido
            "27.865.757/0001-03", // Dígito verificador incorreto mas formato válido
            "61.797.924/0001-51"  // Dígito verificador incorreto mas formato válido
    })
    public void deveRetornarTrueParaCnpjComFormatoValidoMasDVIncorreto(String cnpj) {
        assertTrue(validador.isValid(cnpj, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar false para valores nulos ou vazios")
    @NullAndEmptySource
    public void deveRetornarFalseParaValoresNulosOuVazios(String cnpj) {
        assertFalse(validador.isValid(cnpj, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar false para CNPJs com tamanho incorreto")
    @ValueSource(strings = {
            "123456", 
            "123456789012", 
            "1234567890123",
            "123456789012345"
    })
    public void deveRetornarFalseParaCnpjComTamanhoIncorreto(String cnpj) {
        assertFalse(validador.isValid(cnpj, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CNPJs com todos os dígitos iguais")
    @ValueSource(strings = {
            "00000000000000", 
            "11111111111111", 
            "22222222222222",
            "33333333333333",
            "44444444444444",
            "55555555555555",
            "66666666666666",
            "77777777777777",
            "88888888888888",
            "99999999999999"
    })
    public void deveRetornarTrueParaCnpjComTodosDigitosIguais(String cnpj) {
        assertTrue(validador.isValid(cnpj, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar false para CNPJs com caracteres não numéricos além dos separadores esperados")
    @ValueSource(strings = {
            "12.345.678/0001-9A", // Letra no lugar de número
            "12.34B.678/0001-95", // Letra no meio
            "12.345.678/0001-9$", // Símbolo especial
            "12,345.678/0001-95"  // Separador incorreto
    })
    public void deveRetornarFalseParaCnpjComCaracteresInvalidos(String cnpj) {
        assertFalse(validador.isValid(cnpj, context));
    }
    
    @Test
    @DisplayName("Deve inicializar corretamente")
    public void deveInicializarCorretamente() {
        // Verifica se o método initialize não causa exceções
        validador.initialize(null);
        // Se chegar aqui, o teste passa
        assertTrue(true);
    }
}