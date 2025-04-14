package com.infinitysolutions.authservice.infra.configuration;

import com.infinitysolutions.authservice.model.Cargo;
import com.infinitysolutions.authservice.model.Credencial;
import com.infinitysolutions.authservice.model.enums.NomeCargo;
import com.infinitysolutions.authservice.repository.CargoRepository;
import com.infinitysolutions.authservice.repository.CredencialRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminInitializer implements CommandLineRunner {
    private final CredencialRepository credencialRepository;
    private final CargoRepository cargoRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.senha}")
    private String adminSenha;

    @Value("${admin.usuario-id}")
    private String adminUsuarioId;

    @Override
    @Transactional
    public void run(String... args) {
        log.info("Verificando se existe um usuário administrador...");

        // Garantir que o cargo ADMIN existe
        Cargo cargoAdmin = garantirCargoAdmin();

        // Verificar se já existe algum usuário com cargo ADMIN
        boolean adminExists = credencialRepository.findAll().stream()
                .anyMatch(credencial ->
                        credencial.getCargos().stream()
                                .anyMatch(cargo -> cargo.getNome() == NomeCargo.ADMIN));

        if (!adminExists) {
            criarUsuarioAdmin(cargoAdmin);
        } else {
            log.info("Usuário administrador já existe no sistema.");
        }
    }

    private void criarUsuarioAdmin(Cargo cargoAdmin) {
        log.info("Criando usuário administrador padrão...");

        try {
            UUID usuarioId = UUID.fromString(adminUsuarioId);

            Credencial admin = new Credencial(
                    usuarioId,
                    adminEmail,
                    passwordEncoder.encode(adminSenha)
            );

            admin.getCargos().add(cargoAdmin);
            credencialRepository.save(admin);

            log.info("Usuário administrador criado com sucesso: {}", adminEmail);
        } catch (Exception e) {
            log.error("Erro ao criar usuário administrador: {}", e.getMessage(), e);
        }
    }

    private Cargo garantirCargoAdmin() {
        Optional<Cargo> cargoOpt = cargoRepository.findByNome(NomeCargo.ADMIN);

        if (cargoOpt.isPresent()) {
            log.info("Cargo ADMIN já existe no sistema.");
            return cargoOpt.get();
        }

        log.info("Criando cargo ADMIN...");
        Cargo cargo = new Cargo();
        cargo.setNome(NomeCargo.ADMIN);
        cargo.setDescricao("Administrador do sistema");
        return cargoRepository.save(cargo);
    }
}
