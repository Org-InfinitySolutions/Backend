package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.mapper.PessoaFisicaMapper;
import com.infinitysolutions.applicationservice.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.repository.PessoaFisicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaFisicaStrategy implements UsuarioStrategy<PessoaFisicaCadastroDTO, PessoaFisicaAtualizacaoDTO ,PessoaFisicaRespostaCadastroDTO, PessoaFisicaDTO> {

    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final PessoaFisicaMapper pessoaFisicaMapper;

    @Override
    public PessoaFisicaRespostaCadastroDTO cadastrar(PessoaFisicaCadastroDTO usuarioCadastroDTO, Endereco usuarioEndereco) {
        boolean existePorCpf = pessoaFisicaRepository.existsByCpf(usuarioCadastroDTO.getCpf());
        boolean existePorRg = pessoaFisicaRepository.existsByRgContaining(usuarioCadastroDTO.getRg());

        if (existePorCpf || existePorRg) {

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


    private PessoaFisica findById(UUID id) {
        log.info("Buscando pessoa fisica com ID: {}", id);
        return pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada com ID: " + id));
    }

    @Override
    public PessoaFisicaDTO buscarPorId(UUID id) {
        PessoaFisica pessoaFisica = findById(id);
        boolean possuiCopiaRG = pessoaFisica.getCopiaRg() != null;
        return UsuarioMapper.toPessoaFisicaDTO(pessoaFisica, possuiCopiaRG, possuiCopiaRG);
    }

    @Override
    public List<PessoaFisicaDTO> listarTodos() {
        return pessoaFisicaRepository.findAll()
                .stream()
                .map(pessoaFisica -> {
                    boolean possuiCopiaRG = pessoaFisica.getCopiaRg() != null;
                    return UsuarioMapper.toPessoaFisicaDTO(pessoaFisica, possuiCopiaRG, possuiCopiaRG);
                })
                .toList();
    }

    @Override
    public Class<PessoaFisicaCadastroDTO> getTipoDTO() {
        return PessoaFisicaCadastroDTO.class;
    }
}
