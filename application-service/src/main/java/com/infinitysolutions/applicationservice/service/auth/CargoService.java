package com.infinitysolutions.applicationservice.service.auth;

import com.infinitysolutions.applicationservice.mapper.auth.CargoMapper;
import com.infinitysolutions.applicationservice.model.auth.Cargo;
import com.infinitysolutions.applicationservice.model.auth.enums.NomeCargo;
import com.infinitysolutions.applicationservice.repository.auth.CargoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CargoService {

    private final CargoRepository cargoRepository;
    public Cargo resgatarCargo(String tipoUsuario) {
        log.info("Buscando um cargo para o usuário do tipo: {}", tipoUsuario);

        NomeCargo nomeCargo = switch (tipoUsuario) {
            case "PJ" -> NomeCargo.USUARIO_PJ;
            case "PF" -> NomeCargo.USUARIO_PF;
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        };
        boolean existePorNome = cargoRepository.existsByNome(nomeCargo);

        if (existePorNome) {
            log.info("Cargo já existe no banco de dados: {}", nomeCargo);
            return cargoRepository.findByNome(nomeCargo).get();
        }

        log.info("Cargo não existe no banco de dados, criando novo cargo: {}", nomeCargo);
        return CargoMapper.toCargo(tipoUsuario);
    }
}
