package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.model.dto.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioRespostaDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioStrategy <T extends UsuarioCadastroDTO, R extends UsuarioRespostaCadastroDTO, D extends UsuarioRespostaDTO> {
    R cadastrar(T usuarioCadastroDTO);
    R atualizar(T usuarioCadastroDTO);
    void excluir(T usuarioCadastroDTO);
    D buscarPorId(UUID id);
    List<D> listarTodos();
    Class<T> getTipoDTO();
}
