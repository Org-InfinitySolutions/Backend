package com.infinitysolutions.applicationservice.old.service;

import com.infinitysolutions.applicationservice.core.usecases.credencial.BuscarCredencialPorId;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaEmail;
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

    private final BuscarCredencialPorId buscarCredencialPorId;

    @Override
    public RespostaEmail buscarEmailUsuario(UUID idUsuario) {
        log.info("Buscando email do usuário ID: {} no AuthService", idUsuario);
        RespostaEmail resposta = new RespostaEmail(buscarCredencialPorId.execute(idUsuario).getEmail().getValor());
        log.info("Email do usuário ID: {} obtido com sucesso", idUsuario);
        return resposta;
    }
}
