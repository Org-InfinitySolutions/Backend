package com.infinitysolutions.applicationservice.infrastructure.gateway.usuario;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.PessoaFisicaGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PessoaFisicaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.EnderecoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PessoaFisicaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class PessoaFisicaGatewayImpl implements PessoaFisicaGateway {

    private final PessoaFisicaRepository repository;
    private final UsuarioEntityMapper usuarioMapper;
    private final EnderecoRepository enderecoRepository;

    @Override
    @Transactional
    public PessoaFisica save(PessoaFisica usuario) {
        log.info("Cadastrando pessoa física com CPF: {}", usuario.getCpf());
        Endereco enderecoDomain = usuario.getEndereco();
        PessoaFisicaEntity newPFEntity = usuarioMapper.toPessoaFisica(usuario.getNome(), usuario.getTelefoneCelular(), usuario.getRg(), usuario.getCpf());
        EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoDomain.getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Endereco não encontrado."));
        newPFEntity.setEnderecoEntity(enderecoEntity);
        return (PessoaFisica) usuarioMapper.toDomain(repository.save(newPFEntity));
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return repository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByRg(String rg) {
        return repository.existsByRg(rg);
    }

    @Override
    public List<PessoaFisica> findAll() {
        return repository.findAllByIsAtivoTrue().stream().map(entity -> (PessoaFisica) usuarioMapper.toDomain(entity)).toList();
    }

    @Override
    public Optional<PessoaFisica> findById(UUID id) {
        return repository.findByIdAndIsAtivoTrue(id).map(entity -> (PessoaFisica) usuarioMapper.toDomain(entity));
    }

    @Override
    public PessoaFisica update(PessoaFisica usuario) {
        log.info("Atualizando pessoa física com CPF: {}", usuario.getCpf());
        PessoaFisicaEntity pFEntity = repository.findByIdAndIsAtivoTrue(usuario.getId()).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(usuario.getId()));
        pFEntity.setNome(usuario.getNome());
        pFEntity.setTelefoneCelular(usuario.getTelefoneCelular());
        pFEntity.setAtivo(usuario.isAtivo());

        EnderecoEntity enderecoEntity = enderecoRepository.findById(usuario.getEndereco().getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Falha ao buscar o endereço"));
        pFEntity.setEnderecoEntity(enderecoEntity);
        return (PessoaFisica) usuarioMapper.toDomain(repository.save(pFEntity));
    }
}
