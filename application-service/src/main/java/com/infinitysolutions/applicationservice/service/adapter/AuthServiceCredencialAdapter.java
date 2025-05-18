package com.infinitysolutions.applicationservice.service.adapter;

import com.infinitysolutions.applicationservice.model.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioCadastroDTO;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthServiceCredencialAdapter {

    public AuthServiceCadastroRequestDTO adaptarParaCadastroRequest(UUID usuarioId, UsuarioCadastroDTO dto) {

        if (dto == null) {
            throw new IllegalArgumentException("UsuarioCadastroDTO não pode ser nulo");
        }

        return new AuthServiceCadastroRequestDTO(
                dto.getEmail(),
                dto.getSenha(),
                usuarioId,
                dto.getTipo()
        );
    }
}
