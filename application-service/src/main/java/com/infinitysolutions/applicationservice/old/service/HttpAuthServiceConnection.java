package com.infinitysolutions.applicationservice.old.service;

import com.infinitysolutions.applicationservice.old.infra.client.AuthServiceClient;
import com.infinitysolutions.applicationservice.old.infra.exception.AuthServiceCommunicationException;
import com.infinitysolutions.applicationservice.old.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaEmail;
import com.infinitysolutions.applicationservice.old.service.strategy.AuthServiceConnection;
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
    public RespostaEmail buscarEmailUsuario(UUID idUsuario) {
        log.info("Buscando email do usuário ID: {} no AuthService", idUsuario);
        
        try {
            RespostaEmail resposta = authServiceClient.buscarEmail(idUsuario, authApiKey);
            log.info("Email do usuário ID: {} obtido com sucesso", idUsuario);
            return resposta;
        } catch (FeignException e) {
            log.error("Erro ao buscar email do usuário no AuthService para o usuário ID: {}. Status: {}, Response: {}", 
                    idUsuario, e.status(), e.getMessage());
                    
            if (e.status() == 404) {
                throw new com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException(
                    "Credencial para o usuário de ID " + idUsuario + " não encontrada");
            }
            
            throw AuthServiceCommunicationException.servicoIndisponivel();
        } catch (Exception e) {
            log.error("Erro inesperado ao buscar email do usuário no AuthService para o usuário ID: {}", idUsuario, e);
            throw ErroInesperadoException.erroInesperado("Erro inesperado na comunicação com AuthService.", e.getMessage());
        }
    }
}
