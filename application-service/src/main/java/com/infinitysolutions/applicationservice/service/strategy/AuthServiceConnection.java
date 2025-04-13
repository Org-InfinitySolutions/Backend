package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.model.dto.auth.AuthServiceCadastroRequestDTO;

import java.util.UUID;

public interface AuthServiceConnection {
    void enviarCredenciais(AuthServiceCadastroRequestDTO credenciasDTO);
    void desativarCredenciais(UUID idUsuario);
}
