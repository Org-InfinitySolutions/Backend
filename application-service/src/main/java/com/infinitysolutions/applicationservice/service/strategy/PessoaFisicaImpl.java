package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.infra.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.mapper.PessoaFisicaMapper;
import com.infinitysolutions.applicationservice.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.model.dto.pessoa.fisica.PessoaFisicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.repository.PessoaFisicaRepository;
import com.infinitysolutions.applicationservice.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaFisicaImpl implements UsuarioStrategy<PessoaFisicaCadastroDTO, PessoaFisicaAtualizacaoDTO, PessoaFisicaRespostaCadastroDTO, PessoaFisicaDTO> {

    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final PessoaFisicaMapper pessoaFisicaMapper;
    private final FileUploadService fileUploadService;

    @Override
    public PessoaFisicaRespostaCadastroDTO cadastrar(PessoaFisicaCadastroDTO usuarioCadastroDTO, Endereco usuarioEndereco) {
        boolean existePorCpf = pessoaFisicaRepository.existsByCpf(usuarioCadastroDTO.getCpf().replaceAll("[.\\-\\s]", ""));
        boolean existePorRg = pessoaFisicaRepository.existsByRgContaining(usuarioCadastroDTO.getRg());

        if (existePorCpf) {
            log.warn("Tentativa de criar uma pessoa física com um CPF já existente: {}", usuarioCadastroDTO.getCpf());
            throw RecursoExistenteException.cpfJaEmUso(usuarioCadastroDTO.getCpf());
        }

        if (existePorRg) {
            log.warn("Tentativa de criar uma pessoa física com um RG já existente: {}", usuarioCadastroDTO.getRg());
            throw RecursoExistenteException.rgJaEmUso(usuarioCadastroDTO.getRg());
        }

        log.info("Cadastrando pessoa física com CPF: {}", usuarioCadastroDTO.getCpf());
        Usuario novoUsuario = UsuarioMapper.toUsuario(usuarioCadastroDTO.getNome(), usuarioCadastroDTO.getTelefoneCelular());
        novoUsuario.setEndereco(usuarioEndereco);
        PessoaFisica novaPessoaFisica = UsuarioMapper.toPessoaFisica(novoUsuario, usuarioCadastroDTO.getRg(), usuarioCadastroDTO.getCpf());
        return UsuarioMapper.toPessoaFisicaRespostaCadastroDTO(pessoaFisicaRepository.save(novaPessoaFisica));
    }

    @Override
    public PessoaFisicaRespostaCadastroDTO atualizar(PessoaFisicaAtualizacaoDTO pessoaFisicaAtualizacaoDTO, UUID pessoaFisicaId) {

        PessoaFisica pessoaFisica = findById(pessoaFisicaId);
        log.info("Atualizando pessoa fisica com CPF: {}", pessoaFisica.getCpf());

        pessoaFisicaMapper.atualizarPessoaFisica(pessoaFisicaAtualizacaoDTO, pessoaFisica);
        PessoaFisica pessoaFisicaAtualizada = pessoaFisicaRepository.save(pessoaFisica);
        
        return UsuarioMapper.toPessoaFisicaRespostaCadastroDTO(pessoaFisicaAtualizada);
    }

    @Override
    public void excluir(UUID pessoaFisicaId) {
        PessoaFisica pessoaFisica = findById(pessoaFisicaId);
        log.info("Excluindo pessoa fisica com CPF: {}", pessoaFisica.getCpf());
        pessoaFisica.getUsuario().setAtivo(false);
        pessoaFisicaRepository.save(pessoaFisica);
    }


    public PessoaFisica findById(UUID id) {
        log.info("Buscando pessoa fisica com ID: {}", id);
        Optional<PessoaFisica> optPessoaFisica = pessoaFisicaRepository.findByIdAndUsuario_IsAtivoTrue(id);
        if (optPessoaFisica.isEmpty()){
            log.warn("Pessoa fisica com ID: {} não encontrado", id);
            throw RecursoNaoEncontradoException.usuarioNaoEncontrado(id);
        }
        return optPessoaFisica.get();
    }

    @Override
    public PessoaFisicaDTO buscarPorId(UUID id) {
        PessoaFisica pessoaFisica = findById(id);
        boolean possuiCopiaRG = false;
        List<UsuarioRespostaDTO.DocumentoUsuarioDTO> documentosUsuario = new ArrayList<>();
        if (pessoaFisica.getUsuario().temDocumentos()){
            List<ArquivoMetadados> documentos = pessoaFisica.getUsuario().getDocumentos();
            possuiCopiaRG = documentos.stream().anyMatch(documento -> documento.getTipoAnexo().equals(TipoAnexo.COPIA_RG));
            documentosUsuario = documentos.stream().map(documento -> new UsuarioRespostaDTO.DocumentoUsuarioDTO(
                    documento.getOriginalFilename(),
                    fileUploadService.generatePrivateFileSasUrl(documento.getBlobName(), 60),
                    documento.getMimeType(),
                    documento.getTipoAnexo().toString()
            )).toList();

        }
        return UsuarioMapper.toPessoaFisicaDTO(pessoaFisica, possuiCopiaRG, possuiCopiaRG, documentosUsuario);
    }

    @Override
    public List<PessoaFisicaRespostaCadastroDTO> listarTodos() {
        return pessoaFisicaRepository.findAllByUsuario_IsAtivoTrue()
                .stream()
                .map(UsuarioMapper::toPessoaFisicaRespostaCadastroDTO)
                .toList();
    }

    @Override
    public Class<PessoaFisicaCadastroDTO> getTipoDTO() {
        return PessoaFisicaCadastroDTO.class;
    }

    @Override
    public Class<PessoaFisicaAtualizacaoDTO> getTipoAtualizacaoDTO() {
        return PessoaFisicaAtualizacaoDTO.class;
    }

    public boolean verificarCpf(String cpf) {
        return pessoaFisicaRepository.existsByCpf(cpf.replaceAll("[.\\-\\s]", ""));
    }
}
