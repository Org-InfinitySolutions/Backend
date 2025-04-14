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

@ExtendWith(MockitoExtension.class)
public class ValidadorCpfTest {
    
    private ValidadorCpf validador;
    
    @Mock
    private ConstraintValidatorContext context;
    
    @BeforeEach
    public void setup() {
        validador = new ValidadorCpf();
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CPFs válidos com formatação")
    @ValueSource(strings = {
            "123.456.789-09", 
            "529.982.247-25", 
            "111.222.333-96",
            "074.307.760-45"
    })
    public void deveRetornarTrueParaCpfValidoComFormatacao(String cpf) {
        assertTrue(validador.isValid(cpf, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CPFs válidos sem formatação")
    @ValueSource(strings = {
            "12345678909", 
            "52998224725", 
            "11122233396",
            "07430776045"
    })
    public void deveRetornarTrueParaCpfValidoSemFormatacao(String cpf) {
        assertTrue(validador.isValid(cpf, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CPFs com formato válido mesmo com dígitos verificadores incorretos")
    @ValueSource(strings = {
            "123.456.789-00", // Dígito verificador incorreto mas formato válido
            "529.982.247-26", // Dígito verificador incorreto mas formato válido
            "111.222.333-97", // Dígito verificador incorreto mas formato válido
            "074.307.760-46"  // Dígito verificador incorreto mas formato válido
    })
    public void deveRetornarTrueParaCpfComFormatoValidoMasDVIncorreto(String cpf) {
        assertTrue(validador.isValid(cpf, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar false para CPFs com tamanho incorreto")
    @ValueSource(strings = {
            "123456", 
            "1234567890", 
            "123456789012",
            "1234567890123"
    })
    public void deveRetornarFalseParaCpfComTamanhoIncorreto(String cpf) {
        assertFalse(validador.isValid(cpf, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar true para CPFs com todos os dígitos iguais")
    @ValueSource(strings = {
            "00000000000", 
            "11111111111", 
            "22222222222",
            "33333333333",
            "44444444444",
            "55555555555",
            "66666666666",
            "77777777777",
            "88888888888",
            "99999999999"
    })
    public void deveRetornarFalseParaCpfComTodosDigitosIguais(String cpf) {
        assertFalse(validador.isValid(cpf, context));
    }
    
    @ParameterizedTest
    @DisplayName("Deve retornar false para CPFs com caracteres não numéricos além dos separadores esperados")
    @ValueSource(strings = {
            "123.456.789-0A", // Letra no lugar de número
            "52A.982.247-25", // Letra no meio
            "111.222.333-9$", // Símbolo especial
            "123,456.789-09"  // Separador incorreto
    })
    public void deveRetornarFalseParaCpfComCaracteresInvalidos(String cpf) {
        assertFalse(validador.isValid(cpf, context));
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