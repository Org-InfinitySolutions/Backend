package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.mapper.UsuarioMapper;
import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.repository.PessoaFisicaRepository;
import com.infinitysolutions.applicationservice.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaFisicaStrategy implements UsuarioStrategy<PessoaFisicaCadastroDTO, PessoaFisicaRespostaCadastroDTO, PessoaFisicaDTO> {

    private final PessoaFisicaRepository pessoaFisicaRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PessoaFisicaRespostaCadastroDTO cadastrar(PessoaFisicaCadastroDTO usuarioCadastroDTO) {
        boolean existePorCpf = pessoaFisicaRepository.existsByCpf(usuarioCadastroDTO.getCpf());
        boolean existePorRg = pessoaFisicaRepository.existsByRgContaining(usuarioCadastroDTO.getRg());

        if (existePorCpf || existePorRg) {

        }

        log.info("Cadastrando pessoa física com CPF: {}", usuarioCadastroDTO.getCpf());
        Usuario novoUsuario = UsuarioMapper.toUsuario(usuarioCadastroDTO.getNome(), usuarioCadastroDTO.getTelefone());
        PessoaFisica novaPessoaFisica = UsuarioMapper.toPessoaFisica(novoUsuario, usuarioCadastroDTO.getRg(), usuarioCadastroDTO.getCpf());
        return UsuarioMapper.toPessoaFisicaRespostaCadastroDTO(pessoaFisicaRepository.save(novaPessoaFisica));
    }

    @Override
    public PessoaFisicaDTO atualizar(UsuarioCadastroDTO usuarioCadastroDTO) {
        log.info("Atualizando pessoa fisica com ID: {}", usuarioCadastroDTO.getEmail());

    }

    @Override
    public void excluir(PessoaFisicaCadastroDTO usuarioCadastroDTO) {
        System.out.println("Excluindo pessoa física com CPF: " + usuarioCadastroDTO.getCpf());
    }

    @Override
    public PessoaFisicaDTO buscarPorId(UUID id) {
        System.out.println("Buscando pessoa física com id: " + id);

        PessoaFisica pessoaFisica = pessoaFisicaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pessoa física não encontrada com ID: " + id));

        boolean possuiCopiaRG = pessoaFisica.getCopiaRg() != null;
        String email = "";
        return UsuarioMapper.toPessoaFisicaDTO(pessoaFisica, email, possuiCopiaRG, possuiCopiaRG);
    }

    @Override
    public List<PessoaFisicaDTO> listarTodos() {
        System.out.println("Listando todas as pessoas físicas");
    }

    @Override
    public Class<PessoaFisicaCadastroDTO> getTipoDTO() {
        return PessoaFisicaCadastroDTO.class;
    }
}
