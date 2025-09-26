package com.infinitysolutions.applicationservice.infrastructure.mapper.auth;

import com.infinitysolutions.applicationservice.core.domain.usuario.Cargo;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.CargoEntity;
import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;

public class CargoMapper {

    private CargoMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Converte entidade de domínio para entidade JPA.
     * 
     * @param cargo Entidade de domínio
     * @return Entidade JPA
     */
    public static CargoEntity toEntity(Cargo cargo) {
        if (cargo == null) {
            return null;
        }
        
        CargoEntity entity = new CargoEntity(cargo.getNome(), cargo.getDescricao());
        if (!cargo.isNovo()) {
            entity.setId(cargo.getId());
        }
        
        return entity;
    }
    
    /**
     * Converte entidade JPA para entidade de domínio.
     * 
     * @param entity Entidade JPA
     * @return Entidade de domínio
     */
    public static Cargo toDomain(CargoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return Cargo.reconstituir(entity.getId(), entity.getNome(), entity.getDescricao());
    }
    
    /**
     * Cria entidade de domínio Cargo a partir de string de tipo de usuário.
     * 
     * @param tipoUsuario String representando o tipo de usuário
     * @return Entidade de domínio Cargo
     */
    public static Cargo criarCargoPorTipo(String tipoUsuario) {
        return switch (tipoUsuario) {
            case "PF" -> Cargo.criar(NomeCargo.USUARIO_PF, "Usuário Pessoa Física");
            case "PJ" -> Cargo.criar(NomeCargo.USUARIO_PJ, "Usuário Pessoa Jurídica");
            default -> throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        };
    }
}
