package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.dto.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioRespostaDTO;

import java.util.List;
import java.util.UUID;

public interface UsuarioStrategy <
        T extends UsuarioCadastroDTO,
        U extends UsuarioAtualizacaoDTO,
        R extends UsuarioRespostaCadastroDTO,
        D extends UsuarioRespostaDTO

        > {
    R cadastrar(T usuarioCadastroDTO, Endereco usuarioEndereco);
    R atualizar(U usuarioAtualizacaoDTO, UUID usuarioId);
    void excluir(UUID id);
    D buscarPorId(UUID id);
    List<D> listarTodos();
    Class<T> getTipoDTO();
}
