package com.infinitysolutions.authservice.service;

import com.infinitysolutions.authservice.infra.exception.RecursoExistenteException;
import com.infinitysolutions.authservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.authservice.mapper.CargoMapper;
import com.infinitysolutions.authservice.model.Cargo;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
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
    private final CargoService cargoSerivce;

    @Transactional
    public void criarCredencialUsuario(UUID idUsuario, String email, String senha, String tipoUsuario) {
        log.info("Iniciando criação de credencial para usuário: {}", idUsuario);
        if (credencialJaExiste(idUsuario)) {
            log.warn("Tentativa de criar credencial para ID já existente: {}", idUsuario);
            throw RecursoExistenteException.credencialJaExiste(idUsuario);
        }
        if (credencialJaExiste(email)) {
            log.warn("Tentativa de criar credencial com email já existente: {}", email);
            throw RecursoExistenteException.emailJaEmUso(email);
        }

        Cargo cargo = cargoSerivce.resgatarCargo(tipoUsuario);


        Credencial credencial = CredencialMapper.toCredencial(idUsuario, email, encoder.encode(senha));
        credencial.getCargos().add(cargo);

        repository.save(credencial);
        log.info("Credencial criada com sucesso para usuário: {}", idUsuario);
    }

    public void atualizarEstado(UUID usuarioId, boolean ativo) {
        log.info("Atualizando estado da credencial para usuário: {}", usuarioId);
        if (!credencialJaExiste(usuarioId)) {
            log.warn("Tentativa de atualizar estado de credencial não existente: {}", usuarioId);
            throw RecursoNaoEncontradoException.credencialNaoEncontrada(usuarioId);
        }
        Credencial credencialEncontrada = repository.findByFkUsuarioAndAtivoTrue(usuarioId);
        credencialEncontrada.setAtivo(ativo);
        repository.save(credencialEncontrada);
    }

    private boolean credencialJaExiste(UUID idUsuario) {
        return repository.existsByFkUsuarioAndAtivoTrue(idUsuario);
    }

    private boolean credencialJaExiste(String email) {
        return repository.existsByEmailAndAtivoTrue(email);
    }
}
