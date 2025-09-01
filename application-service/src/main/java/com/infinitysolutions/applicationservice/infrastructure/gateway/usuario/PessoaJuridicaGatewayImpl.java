package com.infinitysolutions.applicationservice.infrastructure.gateway.usuario;

import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.port.PessoaJuridicaGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PessoaJuridicaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.EnderecoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PessoaJuridicaRepository;
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
public class PessoaJuridicaGatewayImpl implements PessoaJuridicaGateway {

    private final PessoaJuridicaRepository repository;
    private final UsuarioEntityMapper usuarioMapper;
    private final EnderecoRepository enderecoRepository;

    @Override
    @Transactional
    public PessoaJuridica save(PessoaJuridica usuario) {
        log.info("Criando pessoa juridica com CNPJ: {}", usuario.getCnpj());
        Endereco enderecoDomain = usuario.getEndereco();
        PessoaJuridicaEntity newPJEntity = usuarioMapper.toPessoaJuridica(usuario.getNome(), usuario.getTelefoneCelular(), usuario.getTelefoneResidencial(), usuario.getCnpj(), usuario.getRazaoSocial());
        EnderecoEntity enderecoEntity = enderecoRepository.findById(enderecoDomain.getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Endereco não encontrado."));
        newPJEntity.setEnderecoEntity(enderecoEntity);
        return (PessoaJuridica) usuarioMapper.toDomain(repository.save(newPJEntity));
    }

    @Override
    public boolean existsByCnpj(String cnpj) {
        return repository.existsByCnpj(cnpj);
    }

    @Override
    public List<PessoaJuridica> findAll() {
        return repository.findAllByIsAtivoTrue().stream().map(entity -> (PessoaJuridica) usuarioMapper.toDomain(entity)).toList();
    }

    @Override
    public Optional<PessoaJuridica> findById(UUID id) {
        return repository.findByIdAndIsAtivoTrue(id).map(entity -> (PessoaJuridica) usuarioMapper.toDomain(entity));
    }

    @Override
    public PessoaJuridica update(PessoaJuridica usuario) {
        log.info("Atualizando pessoa juridica com CNPJ: {}", usuario.getCnpj());
        PessoaJuridicaEntity pJEntity = repository.findByIdAndIsAtivoTrue(usuario.getId()).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(usuario.getId()));
        pJEntity.setNome(usuario.getNome());
        pJEntity.setTelefoneCelular(usuario.getTelefoneCelular());
        pJEntity.setTelefoneResidencial(usuario.getTelefoneResidencial());
        pJEntity.setRazaoSocial(usuario.getRazaoSocial());

        EnderecoEntity enderecoEntity = enderecoRepository.findById(usuario.getEndereco().getId()).orElseThrow(() -> new RecursoNaoEncontradoException("Falha ao buscar o endereço"));
        pJEntity.setEnderecoEntity(enderecoEntity);

        return (PessoaJuridica) usuarioMapper.toDomain(repository.save(pJEntity));
    }
}
