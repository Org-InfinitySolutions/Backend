package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.client.AuthServiceClient;
import com.infinitysolutions.applicationservice.infra.exception.AuthServiceCommunicationException;
import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.model.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.model.dto.auth.DesativarCredenciaisRequestDTO;
import com.infinitysolutions.applicationservice.service.strategy.AuthServiceConnection;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpAuthServiceConnection implements AuthServiceConnection {

    private final AuthServiceClient authServiceClient;

    @Value("${services.auth.apikey}")
    private String authApiKey;

    @Override
    public void enviarCredenciais(AuthServiceCadastroRequestDTO credenciaisDTO) {
        log.info("Enviando credenciais para o AuthService para o usuário ID: {}", credenciaisDTO.idUsuario());

        try {

            authServiceClient.registrarCredenciais(credenciaisDTO, authApiKey);
            log.info("Credenciais para o usuário ID: {} enviadas com sucesso.", credenciaisDTO.idUsuario());
        } catch (FeignException e) {
            log.error("Erro ao enviar credenciais para AuthService para o usuário ID: {}. Status: {}, Response: {}", 
                    credenciaisDTO.idUsuario(), e.status(), e.getMessage());

            if (e.status() == 409 || (e.contentUTF8() != null && e.contentUTF8().contains("Email já está em uso"))) {
                String email = credenciaisDTO.email();
                log.warn("Tentativa de cadastro com email já existente: {}", email);
                throw com.infinitysolutions.applicationservice.infra.exception.RecursoExistenteException.emailJaEmUso(email);
            }
            
            throw AuthServiceCommunicationException.falhaAoEnviarCredenciais();


        } catch (Exception e) {

            if (e.getCause() instanceof TimeoutException) {
                log.error("Timeout ao comunicar com AuthService", e);
                throw AuthServiceCommunicationException.timeout();
            }

            log.error("Erro inesperado ao enviar credenciais para AuthService para o usuário ID: {}", credenciaisDTO.idUsuario(), e);
            throw ErroInesperadoException.erroInesperado("Erro inesperado na comunicação com AuthService.", e.getMessage());
        }
    }

    @Override
    public void desativarCredenciais(UUID idUsuario) {
        log.info("Desativando credenciais para o usuário ID: {}", idUsuario);

        try {
            authServiceClient.desativarCredenciais(idUsuario, authApiKey, new DesativarCredenciaisRequestDTO(false));
            log.info("Credenciais para o usuário ID: {} desativadas com sucesso.", idUsuario);
        } catch (FeignException e) {
            log.error("Erro ao desativar credenciais para AuthService para o usuário ID: {}. Status: {}, Response: {}",
                    idUsuario, e.status(), e.getMessage());
            throw AuthServiceCommunicationException.servicoIndisponivel();
        } catch (Exception e) {
            log.error("Erro inesperado ao desativar credenciais para AuthService para o usuário ID: {}", idUsuario, e);
            throw ErroInesperadoException.erroInesperado("Erro inesperado na comunicação com AuthService.", e.getMessage());
        }
    }
}
