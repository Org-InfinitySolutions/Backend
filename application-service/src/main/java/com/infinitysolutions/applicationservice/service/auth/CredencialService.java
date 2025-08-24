package com.infinitysolutions.applicationservice.service.auth;

import com.infinitysolutions.applicationservice.infra.exception.AutenticacaoException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoExistenteException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.mapper.auth.CredencialMapper;
import com.infinitysolutions.applicationservice.model.auth.Cargo;
import com.infinitysolutions.applicationservice.model.auth.Credencial;
import com.infinitysolutions.applicationservice.model.dto.auth.RespostaEmail;
import com.infinitysolutions.applicationservice.repository.auth.CredencialRepository;
import com.infinitysolutions.applicationservice.service.email.EnvioEmailService;
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
    private final CargoService cargoSerivce;
    private final EnvioEmailService envioEmailService;

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

    public Credencial procurarCredencialPorEmail(String email) {
        return procurarCredencial(email);
    }
    
    @Transactional
    public void resetarSenhaPorEmail(String email, String novaSenha) {
        log.info("Iniciando reset de senha para email: {}", email);
        
        Credencial credencial = procurarCredencial(email);
        
        String novaSenhaCriptografada = encoder.encode(novaSenha);
        
        credencial.setHashSenha(novaSenhaCriptografada);
        
        repository.save(credencial);
        
        log.info("Senha resetada com sucesso para email: {}", email);
        envioEmailService.enviarConfirmacaoResetSenha(email);
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

    @Transactional
    public void alterarSenha(UUID usuarioId, String senhaAtual, String novaSenha) {
        log.info("Iniciando alteração de senha para usuário: {}", usuarioId);
        
        Credencial credencial = procurarCredencial(usuarioId);
        
        validarSenha(senhaAtual, credencial);
        
        String novaSenhaCriptografada = encoder.encode(novaSenha);
        
        credencial.setHashSenha(novaSenhaCriptografada);
        
        repository.save(credencial);
        
        log.info("Senha alterada com sucesso para usuário: {}", usuarioId);
        envioEmailService.enviarConfirmacaoResetSenha(credencial.getEmail());
    }

    @Transactional
    public void alterarEmail(UUID usuarioId, String senha, String novoEmail) {
        log.info("Iniciando alteração de email para usuário: {}", usuarioId);
        
        if (verificarEmailExiste(novoEmail)) {
            log.warn("Tentativa de alterar para email já existente: {}", novoEmail);
            throw RecursoExistenteException.emailJaEmUso(novoEmail);
        }

        Credencial credencial = procurarCredencial(usuarioId);
        validarSenha(senha, credencial);
        credencial.setEmail(novoEmail);

        try {
            repository.save(credencial);
            log.info("Email alterado com sucesso para usuário: {} - Novo email: {}", usuarioId, novoEmail);
            envioEmailService.enviarConfirmacaoResetEmail(novoEmail);
        } catch (DataIntegrityViolationException e) {
            log.error("Falha ao alterar email devido a violação de integridade: {}", e.getMessage());
            throw RecursoExistenteException.emailJaEmUso(novoEmail);
        }
    }
}
