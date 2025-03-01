package com.infinitysolutions.login.mapper;

import com.infinitysolutions.login.model.Usuario;
import com.infinitysolutions.login.model.dto.UsuarioDtoCriacao;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioDtoCriacao dto) {
        return new Usuario(dto);
    }

    public static UsuarioDtoCriacao toDto(Usuario usuario){
        return new UsuarioDtoCriacao(usuario.getNome(), usuario.getEmail(), usuario.getSenha());
    }

}
