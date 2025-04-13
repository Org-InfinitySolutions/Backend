package com.infinitysolutions.service;

import com.infinitysolutions.authservice.infra.exception.RecursoExistenteException;
import com.infinitysolutions.authservice.mapper.CredencialMapper;
import com.infinitysolutions.authservice.model.Credencial;
import com.infinitysolutions.authservice.service.CredencialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import com.infinitysolutions.authservice.repository.CredencialRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@TestPropertySource(properties ={
        "eureka.client.enabled=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.service-registry.auto-registration.enabled=false"
})
class CredencialServiceTest {
    @Mock
    private CredencialRepository repository;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private CredencialService service;

    private UUID idUsuario;
    private String email;
    private String senha;
    private String senhaEncoded;
    private Credencial credencialMock;
    private String tipoUsuario;


    @BeforeEach
    void setUp() {
        idUsuario = UUID.randomUUID();
        email = "email@email.com";
        senha = "senha123";
        senhaEncoded = "senhaEncoded";
        tipoUsuario = "PJ";
        credencialMock = new Credencial(idUsuario, email, senhaEncoded);
    }

    @Test
    @DisplayName("Deve criar credencial com sucesso quando dados são válidos")
    void criarCredencialUsuario_DadosValidos_DeveSalvarCredencial() {
        // Arrange
        when(repository.existsById(idUsuario)).thenReturn(false);
        when(repository.existsByEmail(email)).thenReturn(false);
        when(encoder.encode(senha)).thenReturn(senhaEncoded);

        try (MockedStatic<CredencialMapper> mockedMapper = mockStatic(CredencialMapper.class)) {
            mockedMapper.when(() -> CredencialMapper.toCredencial(idUsuario, email, senhaEncoded))
                    .thenReturn(credencialMock);

            // Act
            service.criarCredencialUsuario(idUsuario, email, senha, tipoUsuario);

            // Assert
            verify(repository).save(credencialMock);
            verify(encoder).encode(senha);
            mockedMapper.verify(() -> CredencialMapper.toCredencial(idUsuario, email, senhaEncoded));
        }
    }

    @Test
    @DisplayName("Deve lançar exceção quando ID do usuário já existe")
    void criarCredencialUsuario_IDExistente_DeveLancarException() {
        // Arrange
        when(repository.existsById(idUsuario)).thenReturn(true);

        // Act & Assert
        RecursoExistenteException exception = assertThrows(
                RecursoExistenteException.class,
                () -> service.criarCredencialUsuario(idUsuario, email, senha, tipoUsuario)
        );

        assertTrue(exception.getMessage().contains(idUsuario.toString()));
        verify(repository, never()).save(any());
        verify(encoder, never()).encode(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já está em uso")
    void criarCredencialUsuario_EmailExistente_DeveLancarException() {
        // Arrange
        when(repository.existsById(idUsuario)).thenReturn(false);
        when(repository.existsByEmail(email)).thenReturn(true);

        // Act & Assert
        RecursoExistenteException exception = assertThrows(
          RecursoExistenteException.class,
                () -> service.criarCredencialUsuario(idUsuario, email, senha, tipoUsuario)
        );

        assertTrue(exception.getMessage().contains(email));
        verify(repository, never()).save(any());
        verify(encoder, never()).encode(any());
    }


}
