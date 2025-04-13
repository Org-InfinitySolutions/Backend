package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.repository.EnderecoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoServiceTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    private EnderecoDTO enderecoDTO;
    private Endereco enderecoExistente;

    @BeforeEach
    void setUp() {
        // Configuração do objeto DTO usado nos testes
        enderecoDTO = new EnderecoDTO();
        enderecoDTO.setCep("12345-678");
        enderecoDTO.setLogradouro("Rua Exemplo");
        enderecoDTO.setNumero("123");
        enderecoDTO.setBairro("Bairro Teste");
        enderecoDTO.setCidade("Cidade Teste");
        enderecoDTO.setEstado("SP");

        // Configuração de um objeto Endereco existente
        enderecoExistente = new Endereco(
                "12345678",
                "Rua Exemplo",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "123",
                null
        );
        enderecoExistente.setId(1);
    }

    @Test
    @DisplayName("Deve buscar um endereço existente sem complemento")
    void deveBuscarEnderecoExistenteSemComplemento() {
        // Arrange
        when(enderecoRepository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(enderecoExistente));

        // Act
        Endereco resultado = enderecoService.buscarEndereco(enderecoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
        assertEquals("12345678", resultado.getCep());
        assertEquals("Rua Exemplo", resultado.getLogradouro());
        verify(enderecoRepository, times(1))
                .findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                        anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
                );
        verify(enderecoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar um endereço existente com complemento")
    void deveBuscarEnderecoExistenteComComplemento() {
        // Arrange
        enderecoDTO.setComplemento("Apto 101");
        Endereco enderecoComComplemento = new Endereco(
                "12345678",
                "Rua Exemplo",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "123",
                "Apto 101"
        );
        enderecoComComplemento.setId(2);

        when(enderecoRepository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.of(enderecoComComplemento));

        // Act
        Endereco resultado = enderecoService.buscarEndereco(enderecoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getId());
        assertEquals("Apto 101", resultado.getComplemento());
        verify(enderecoRepository, times(1))
                .findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                        anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
                );
        verify(enderecoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve criar um novo endereço sem complemento quando não encontrado")
    void deveCriarNovoEnderecoSemComplemento() {
        // Arrange
        Endereco novoEndereco = new Endereco(
                "12345678",
                "Rua Exemplo",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "123",
                null
        );
        novoEndereco.setId(3);

        when(enderecoRepository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.empty());

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(novoEndereco);

        // Act
        Endereco resultado = enderecoService.buscarEndereco(enderecoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, resultado.getId());
        verify(enderecoRepository, times(1))
                .findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                        anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
                );
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    @DisplayName("Deve criar um novo endereço com complemento quando não encontrado")
    void deveCriarNovoEnderecoComComplemento() {
        // Arrange
        enderecoDTO.setComplemento("Apto 202");

        Endereco novoEnderecoComComplemento = new Endereco(
                "12345678",
                "Rua Exemplo",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "123",
                "Apto 202"
        );
        novoEnderecoComComplemento.setId(4);

        when(enderecoRepository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.empty());

        when(enderecoRepository.save(any(Endereco.class))).thenReturn(novoEnderecoComComplemento);

        // Act
        Endereco resultado = enderecoService.buscarEndereco(enderecoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals(4, resultado.getId());
        assertEquals("Apto 202", resultado.getComplemento());
        verify(enderecoRepository, times(1))
                .findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIgnoreCase(
                        anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), anyString()
                );
        verify(enderecoRepository, times(1)).save(any(Endereco.class));
    }

    @Test
    @DisplayName("Deve normalizar o CEP corretamente")
    void deveNormalizarCep() {
        // Arrange
        enderecoDTO.setCep("12.345-678");

        when(enderecoRepository.findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                anyString(), anyString(), anyString(), anyString(), anyString(), eq("12345678")
        )).thenReturn(Optional.of(enderecoExistente));

        // Act
        Endereco resultado = enderecoService.buscarEndereco(enderecoDTO);

        // Assert
        assertNotNull(resultado);
        assertEquals("12345678", resultado.getCep());
        verify(enderecoRepository, times(1))
                .findByLogradouroIgnoreCaseAndNumeroAndBairroIgnoreCaseAndCidadeIgnoreCaseAndEstadoIgnoreCaseAndCepAndComplementoIsNull(
                        anyString(), anyString(), anyString(), anyString(), anyString(), eq("12345678")
                );
    }
}