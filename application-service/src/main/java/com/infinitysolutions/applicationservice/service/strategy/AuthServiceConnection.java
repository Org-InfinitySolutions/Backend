package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.model.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.model.dto.auth.RespostaEmailDTO;

import java.util.UUID;

public interface AuthServiceConnection {
    void enviarCredenciais(AuthServiceCadastroRequestDTO credenciasDTO);
    void desativarCredenciais(UUID idUsuario);
    RespostaEmailDTO buscarEmailUsuario(UUID idUsuario);
}
