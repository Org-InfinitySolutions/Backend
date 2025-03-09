package com.infinitysolutions.crud.mapper;

import com.infinitysolutions.crud.model.Usuario;
import com.infinitysolutions.crud.model.dto.UsuarioDtoCriacao;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioDtoCriacao dto) {
        return new Usuario(dto);
    }

    public static UsuarioDtoCriacao toDto(Usuario usuario) {
        return new UsuarioDtoCriacao(
                usuario.getNomeCompleto(),
                usuario.getCpf(),
                usuario.getRg(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getNomeFantasia(),
                usuario.getRazaoSocial(),
                usuario.getCnpj()
        );
    }
}