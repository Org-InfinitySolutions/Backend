package com.infinitysolutions.applicationservice.old.service.strategy;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaEmail;

import java.util.UUID;

public interface AuthServiceConnection {
    void enviarCredenciais(AuthServiceCadastroRequestDTO credenciasDTO);
    RespostaEmail buscarEmailUsuario(UUID idUsuario);
}
