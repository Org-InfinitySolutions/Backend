package com.infinitysolutions.authservice.service;

import com.infinitysolutions.authservice.infra.exception.RecursoExistenteException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.infinitysolutions.authservice.mapper.CredencialMapper;
import com.infinitysolutions.authservice.model.Credencial;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.infinitysolutions.authservice.repository.CredencialRepository;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class CredencialService {

    private final CredencialRepository repository;
    private final BCryptPasswordEncoder encoder;

    @Transactional
    public void criarCredencialUsuario(UUID idUsuario, String email, String senha) {
        log.info("Iniciando criação de credencial para usuário: {}", idUsuario);
        boolean credencialExistente = repository.existsById(idUsuario);
        boolean emailExistente = repository.existsByEmail(email);
        if (credencialExistente) {
            log.warn("Tentativa de criar credencial para ID já existente: {}", idUsuario);
            throw RecursoExistenteException.credencialJaExiste(idUsuario);
        }
        if (emailExistente) {
            log.warn("Tentativa de criar credencial com email já existente: {}", email);
            throw RecursoExistenteException.emailJaEmUso(email);
        }

        Credencial credencial = CredencialMapper.toCredencial(idUsuario, email, encoder.encode(senha));
        repository.save(credencial);
        log.info("Credencial criada com sucesso para usuário: {}", idUsuario);
    }
}
