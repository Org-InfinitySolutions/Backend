package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.infra.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.mapper.PessoaJuridicaMapper;
import com.infinitysolutions.applicationservice.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.repository.PessoaJuridicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaJuridicaImpl implements UsuarioStrategy<PessoaJuridicaCadastroDTO, PessoaJuridicaAtualizacaoDTO, PessoaJuridicaRespostaCadastroDTO, PessoaJuridicaDTO> {

    private final PessoaJuridicaRepository pessoaJuridicaRepository;
    private final PessoaJuridicaMapper pessoaJuridicaMapper;

    @Override
    public PessoaJuridicaRespostaCadastroDTO cadastrar(PessoaJuridicaCadastroDTO usuarioCadastroDTO, Endereco usuarioEndereco) {
        boolean existePorCnpj = pessoaJuridicaRepository.existsByCnpj(usuarioCadastroDTO.getCnpj().replaceAll("[.\\-/\\\\s]", ""));
        if (existePorCnpj) {
            log.warn("Tentativa de criar uma pessoa jurídica com um CNPJ já existente: {}", usuarioCadastroDTO.getCnpj());
            throw RecursoExistenteException.cnpjJaEmUso(usuarioCadastroDTO.getCnpj());
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
        Optional<PessoaJuridica> optPessoaJuridica = pessoaJuridicaRepository.findByIdAndUsuario_IsAtivoTrue(id);
        if (optPessoaJuridica.isEmpty()){
            log.warn("Tentativa de buscar pessoa jurídica com ID não existente: {}", id);
            throw RecursoNaoEncontradoException.usuarioNaoEncontrado(id);
        }
        return optPessoaJuridica.get();
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
        return pessoaJuridicaRepository.findAllByUsuario_IsAtivoTrue().stream()
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

    @Override
    public Class<PessoaJuridicaAtualizacaoDTO> getTipoAtualizacaoDTO() {return PessoaJuridicaAtualizacaoDTO.class;}
}
