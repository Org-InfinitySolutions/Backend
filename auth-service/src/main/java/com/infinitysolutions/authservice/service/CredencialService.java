package com.infinitysolutions.authservice.service;

import com.infinitysolutions.authservice.infra.exception.AutenticacaoException;
import com.infinitysolutions.authservice.infra.exception.RecursoExistenteException;
import com.infinitysolutions.authservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.authservice.model.Cargo;
import com.infinitysolutions.authservice.model.dto.RespostaEmail;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.infinitysolutions.authservice.mapper.CredencialMapper;
import com.infinitysolutions.authservice.model.Credencial;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.infinitysolutions.authservice.repository.CredencialRepository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CredencialService {

    private final CredencialRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final CargoService cargoSerivce;

    @Transactional
    public void criarCredencialUsuario(UUID idUsuario, String email, String senha, String tipoUsuario) {
        log.info("Iniciando criação de credencial para usuário: {}", idUsuario);

        Cargo cargo = cargoSerivce.resgatarCargo(tipoUsuario);
        Credencial credencial = CredencialMapper.toCredencial(idUsuario, email, encoder.encode(senha));
        credencial.getCargos().add(cargo);

        try {
            repository.save(credencial);
            log.info("Credencial criada com sucesso para usuário: {}", idUsuario);
        } catch (DataIntegrityViolationException e){
            log.error("Falha ao criar credencias devido a violação de integridade: {}", e.getMessage());
            throw RecursoExistenteException.credencialJaExiste(idUsuario);
        }
    }

    @Transactional
    public void deletar(UUID usuarioId, String senha) {
        log.info("Deletando a credencial para o usuário: {}", usuarioId);
        Credencial credencialEncontrada = procurarCredencial(usuarioId);
        validarSenha(senha, credencialEncontrada);
        credencialEncontrada.setAtivo(false);
        repository.save(credencialEncontrada);
    }


    private Credencial procurarCredencial(UUID usuarioId) {
        Optional<Credencial> credencial = repository.findByFkUsuarioAndAtivoTrue(usuarioId);
        if (credencial.isEmpty()) {
            log.warn("Credencial não encontrada para o usuário: {}", usuarioId);
            throw RecursoNaoEncontradoException.credencialNaoEncontrada(usuarioId);
        }
        return credencial.get();
    }

    private Credencial procurarCredencial(String email) {
        Optional<Credencial> credencial = repository.findByEmailAndAtivoTrue(email);
        if (credencial.isEmpty()) {
            log.warn("Credencial não encontrada para o email: {}", email);
            throw RecursoNaoEncontradoException.credencialNaoEncontrada(email);
        }
        return credencial.get();
    }

    public Credencial obterCredencial(String email, String senha) {
        Credencial credencial = procurarCredencial(email);
        validarSenha(senha, credencial);
        return credencial;
    }

    private void validarSenha(String senha, Credencial credencial) {
        if (!encoder.matches(senha, credencial.getHashSenha())) {
            log.warn("Senha inválida para o email: {}", credencial.getEmail());
            throw AutenticacaoException.credenciaisInvalidas();
        }
    }

    public RespostaEmail buscarEmail(UUID usuarioId) {
        Credencial credencial = procurarCredencial(usuarioId);
        return new RespostaEmail(credencial.getEmail());
    }

    public boolean verificarEmailExiste(String email) {
        log.info("Verificando se o email {} já existe", email);
        return repository.existsByEmailAndAtivoTrue(email);
    }
}
