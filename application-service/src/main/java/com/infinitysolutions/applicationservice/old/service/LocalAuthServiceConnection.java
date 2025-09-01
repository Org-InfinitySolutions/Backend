package com.infinitysolutions.applicationservice.old.service;

import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.old.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaEmail;
import com.infinitysolutions.applicationservice.old.service.auth.CredencialService;
import com.infinitysolutions.applicationservice.old.service.strategy.AuthServiceConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Validated
@Component
@RequiredArgsConstructor
@Slf4j
@Primary
public class LocalAuthServiceConnection implements AuthServiceConnection {

    private final CredencialService credencialService;

    @Override
    public void enviarCredenciais(AuthServiceCadastroRequestDTO credenciasDTO) {
        try {
            log.info("Enviando credenciais para criação de usuário ID: {}", credenciasDTO.idUsuario());
            if (credencialService.verificarEmailExiste(credenciasDTO.email())) {
                log.warn("Tentativa de cadastro com email já existente: {}", credenciasDTO.email());
                throw RecursoExistenteException.emailJaEmUso(credenciasDTO.email());
            }
            
            credencialService.criarCredencialUsuario(credenciasDTO.idUsuario(), credenciasDTO.email(), credenciasDTO.senha(), credenciasDTO.tipoUsuario());
            log.info("Credenciais para o usuário ID: {} enviadas com sucesso.", credenciasDTO.idUsuario());
        } catch (Exception e) {
            log.error("Erro inesperado ao criar credenciais: {}", e.getMessage());
            throw ErroInesperadoException.erroInesperado(
                "Erro inesperado ao criar credenciais", e.getMessage());
        }
    }


    @Override
    public RespostaEmail buscarEmailUsuario(UUID idUsuario) {
        log.info("Buscando email do usuário ID: {} no AuthService", idUsuario);
        RespostaEmail resposta = credencialService.buscarEmail(idUsuario);
        log.info("Email do usuário ID: {} obtido com sucesso", idUsuario);
        return resposta;
    }
}
