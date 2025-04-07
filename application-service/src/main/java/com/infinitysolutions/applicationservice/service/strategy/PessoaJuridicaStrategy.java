package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.mapper.PessoaJuridicaMapper;
import com.infinitysolutions.applicationservice.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.*;
import com.infinitysolutions.applicationservice.repository.PessoaJuridicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaJuridicaStrategy implements UsuarioStrategy<PessoaJuridicaCadastroDTO, PessoaJuridicaAtualizacaoDTO, PessoaJuridicaRespostaCadastroDTO, PessoaJuridicaDTO> {

    private final PessoaJuridicaRepository pessoaJuridicaRepository;
    private final PessoaJuridicaMapper pessoaJuridicaMapper;

    @Override
    public PessoaJuridicaRespostaCadastroDTO cadastrar(PessoaJuridicaCadastroDTO usuarioCadastroDTO, Endereco usuarioEndereco) {
        boolean existePorCnpj = pessoaJuridicaRepository.existsByCnpj(usuarioCadastroDTO.getCnpj());

        if (existePorCnpj) {

        }

        log.info("Cadastrando pessoa jurídica com CNPJ: {}", usuarioCadastroDTO.getCnpj());
        Usuario novoUsuario = UsuarioMapper.toUsuario(usuarioCadastroDTO.getNome(), usuarioCadastroDTO.getTelefoneCelular());
        novoUsuario.setEndereco(usuarioEndereco);
        PessoaJuridica novaPessoaJuridica = UsuarioMapper.toPessoaJuridica(novoUsuario, usuarioCadastroDTO.getTelefoneResidencial(), usuarioCadastroDTO.getCnpj(), usuarioCadastroDTO.getRazaoSocial());
        return UsuarioMapper.toPessoaJuridicaRespostaCadastroDTO(pessoaJuridicaRepository.save(novaPessoaJuridica));
    }

    @Override
    public PessoaJuridicaRespostaCadastroDTO atualizar(PessoaJuridicaAtualizacaoDTO usuarioCadastroDTO, UUID pessoaJuridicaId) {
        PessoaJuridica pessoaJuridica = findById(pessoaJuridicaId);
        log.info("Atualizando pessoa jurídica com CNPJ: {}", pessoaJuridica.getCnpj());

        pessoaJuridicaMapper.atualizarPessoaJuridica(usuarioCadastroDTO, pessoaJuridica);
        PessoaJuridica pessoaJuridicaAtualizada = pessoaJuridicaRepository.save(pessoaJuridica);

        return UsuarioMapper.toPessoaJuridicaRespostaCadastroDTO(pessoaJuridicaAtualizada);
    }

    @Override
    public void excluir(UUID pessoaJuridicaId) {
        PessoaJuridica pessoaJuridica = findById(pessoaJuridicaId);
        log.info("Excluindo pessoa jurídica com CNPJ: {}", pessoaJuridica.getCnpj());
        pessoaJuridica.getUsuario().setAtivo(false);
        pessoaJuridicaRepository.save(pessoaJuridica);
    }

    private PessoaJuridica findById(UUID id) {
        log.info("Buscando pessoa jurídica com ID: {}", id);
        return pessoaJuridicaRepository.findById(id).orElseThrow(() -> new RuntimeException("Pessoa Jurídica não encontrada"));
    }

    @Override
    public PessoaJuridicaDTO buscarPorId(UUID pessoaJuridicaId) {
        PessoaJuridica pessoaJuridica = findById(pessoaJuridicaId);
        boolean possuiContratoSocial = pessoaJuridica.getContratoSocial() != null;
        boolean possuiCartaoCnpj = pessoaJuridica.getCartaoCnpj() != null;
        boolean cadastroCompleto = possuiContratoSocial && possuiCartaoCnpj;

        return UsuarioMapper.toPessoaJuridicaDTO(pessoaJuridica, possuiCartaoCnpj, possuiContratoSocial, cadastroCompleto);
    }

    @Override
    public List<PessoaJuridicaDTO> listarTodos() {
        return pessoaJuridicaRepository.findAll().stream()
                .map(pessoaJuridica -> {
                    boolean possuiContratoSocial = pessoaJuridica.getContratoSocial() != null;
                    boolean possuiCartaoCnpj = pessoaJuridica.getCartaoCnpj() != null;
                    boolean cadastroCompleto = possuiContratoSocial && possuiCartaoCnpj;

                    return UsuarioMapper.toPessoaJuridicaDTO(pessoaJuridica, possuiCartaoCnpj, possuiContratoSocial, cadastroCompleto);
                })
                .toList();
    }

    @Override
    public Class<PessoaJuridicaCadastroDTO> getTipoDTO() {
        return PessoaJuridicaCadastroDTO.class;
    }
}
