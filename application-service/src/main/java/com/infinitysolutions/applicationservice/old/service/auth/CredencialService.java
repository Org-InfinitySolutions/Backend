package com.infinitysolutions.applicationservice.old.service.auth;

import com.infinitysolutions.applicationservice.core.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.old.infra.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.infrastructure.mapper.auth.CredencialMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CargoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.auth.RespostaEmail;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth.CredencialRepository;
import com.infinitysolutions.applicationservice.old.service.email.EnvioEmailService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CredencialService {

    private final CredencialRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final EnvioEmailService envioEmailService;


    @Transactional
    public void deletar(UUID usuarioId, String senha) {
        log.info("Deletando a credencial para o usuário: {}", usuarioId);
        CredencialEntity credencialEntityEncontrada = procurarCredencial(usuarioId);
        validarSenha(senha, credencialEntityEncontrada);
        credencialEntityEncontrada.setAtivo(false);
        repository.save(credencialEntityEncontrada);
    }


    private CredencialEntity procurarCredencial(UUID usuarioId) {
        Optional<CredencialEntity> credencial = repository.findByFkUsuarioAndAtivoTrue(usuarioId);
        if (credencial.isEmpty()) {
            log.warn("Credencial não encontrada para o usuário: {}", usuarioId);
            throw RecursoNaoEncontradoException.credencialNaoEncontrada(usuarioId);
        }
        return credencial.get();
    }

    private CredencialEntity procurarCredencial(String email) {
        Optional<CredencialEntity> credencial = repository.findByEmailAndAtivoTrue(email);
        if (credencial.isEmpty()) {
            log.warn("Credencial não encontrada para o email: {}", email);
            throw RecursoNaoEncontradoException.credencialNaoEncontrada(email);
        }
        return credencial.get();
    }

    public CredencialEntity procurarCredencialPorEmail(String email) {
        return procurarCredencial(email);
    }
    
    @Transactional
    public void resetarSenhaPorEmail(String email, String novaSenha) {
        log.info("Iniciando reset de senha para email: {}", email);
        
        CredencialEntity credencialEntity = procurarCredencial(email);
        
        String novaSenhaCriptografada = encoder.encode(novaSenha);
        
        credencialEntity.setHashSenha(novaSenhaCriptografada);
        
        repository.save(credencialEntity);
        
        log.info("Senha resetada com sucesso para email: {}", email);
        envioEmailService.enviarConfirmacaoResetSenha(email);
    }

    public CredencialEntity obterCredencial(String email, String senha) {
        CredencialEntity credencialEntity = procurarCredencial(email);
        validarSenha(senha, credencialEntity);
        return credencialEntity;
    }

    private void validarSenha(String senha, CredencialEntity credencialEntity) {
        if (!encoder.matches(senha, credencialEntity.getHashSenha())) {
            log.warn("Senha inválida para o email: {}", credencialEntity.getEmail());
            throw AutenticacaoException.credenciaisInvalidas();
        }
    }

    public RespostaEmail buscarEmail(UUID usuarioId) {
        CredencialEntity credencialEntity = procurarCredencial(usuarioId);
        return new RespostaEmail(credencialEntity.getEmail());
    }

    public boolean verificarEmailExiste(String email) {
        log.info("Verificando se o email {} já existe", email);
        return repository.existsByEmailAndAtivoTrue(email);
    }

    @Transactional
    public void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) {
        log.info("Iniciando alteração de senha para usuário: {}", usuarioId);
        
        CredencialEntity credencialEntity = procurarCredencial(usuarioId);
        
        validarSenha(senhaAtual, credencialEntity);
        
        String novaSenhaCriptografada = encoder.encode(novaSenha);
        
        credencialEntity.setHashSenha(novaSenhaCriptografada);
        
        repository.save(credencialEntity);
        
        log.info("Senha alterada com sucesso para usuário: {}", usuarioId);
        envioEmailService.enviarConfirmacaoResetSenha(credencialEntity.getEmail());
    }

    @Transactional
    public void alterarEmail(UUID usuarioId, String senha, String novoEmail) {
        log.info("Iniciando alteração de email para usuário: {}", usuarioId);
        
        if (verificarEmailExiste(novoEmail)) {
            log.warn("Tentativa de alterar para email já existente: {}", novoEmail);
            throw RecursoExistenteException.emailJaEmUso(novoEmail);
        }

        CredencialEntity credencialEntity = procurarCredencial(usuarioId);
        validarSenha(senha, credencialEntity);
        credencialEntity.setEmail(novoEmail);

        try {
            repository.save(credencialEntity);
            log.info("Email alterado com sucesso para usuário: {} - Novo email: {}", usuarioId, novoEmail);
            envioEmailService.enviarConfirmacaoResetEmail(novoEmail);
        } catch (DataIntegrityViolationException e) {
            log.error("Falha ao alterar email devido a violação de integridade: {}", e.getMessage());
            throw RecursoExistenteException.emailJaEmUso(novoEmail);
        }
    }
}
