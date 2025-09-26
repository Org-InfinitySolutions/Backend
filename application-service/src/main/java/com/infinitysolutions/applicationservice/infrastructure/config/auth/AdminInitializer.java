package com.infinitysolutions.applicationservice.infrastructure.config.auth;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CargoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CredencialEntity;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth.CargoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.auth.CredencialRepository;
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
        CargoEntity cargoEntityAdmin = garantirCargoAdmin();

        // Verificar se já existe algum usuário com cargo ADMIN
        boolean adminExists = credencialRepository.findAll().stream()
                .anyMatch(credencial ->
                        credencial.getCargoEntities().stream()
                                .anyMatch(cargo -> cargo.getNome() == NomeCargo.ADMIN));

        if (!adminExists) {
            criarUsuarioAdmin(cargoEntityAdmin);
        } else {
            log.info("Usuário administrador já existe no sistema.");
        }
    }

    private void criarUsuarioAdmin(CargoEntity cargoEntityAdmin) {
        log.info("Criando usuário administrador padrão...");

        try {
            UUID usuarioId = UUID.fromString(adminUsuarioId);

            CredencialEntity admin = new CredencialEntity(
                    usuarioId,
                    adminEmail,
                    passwordEncoder.encode(adminSenha),
                    true
            );

            admin.getCargoEntities().add(cargoEntityAdmin);
            credencialRepository.save(admin);

            log.info("Usuário administrador criado com sucesso: {}", adminEmail);
        } catch (Exception e) {
            log.error("Erro ao criar usuário administrador: {}", e.getMessage(), e);
        }
    }

    private CargoEntity garantirCargoAdmin() {
        Optional<CargoEntity> cargoOpt = cargoRepository.findByNome(NomeCargo.ADMIN);

        if (cargoOpt.isPresent()) {
            log.info("Cargo ADMIN já existe no sistema.");
            return cargoOpt.get();
        }

        log.info("Criando cargo ADMIN...");
        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setNome(NomeCargo.ADMIN);
        cargoEntity.setDescricao("Administrador do sistema");
        return cargoRepository.save(cargoEntity);
    }
}
