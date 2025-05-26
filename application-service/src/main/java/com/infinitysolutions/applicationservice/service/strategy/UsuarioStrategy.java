package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioStrategy < T extends UsuarioCadastroDTO, U extends UsuarioAtualizacaoDTO, R extends UsuarioRespostaCadastroDTO, D extends UsuarioRespostaDTO> {

    R cadastrar(T usuarioCadastroDTO, Endereco usuarioEndereco);
    R atualizar(U usuarioAtualizacaoDTO, UUID usuarioId);
    void excluir(UUID id);
    D buscarPorId(UUID id);
    List<D> listarTodos();


    Class<T> getTipoDTO();
    Class<U> getTipoAtualizacaoDTO();
}
