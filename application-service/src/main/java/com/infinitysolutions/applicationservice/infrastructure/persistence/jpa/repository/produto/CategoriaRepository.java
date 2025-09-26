package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    List<CategoriaEntity> findAllByIsAtivoTrue();
    Optional<CategoriaEntity> findByIdAndIsAtivoTrue(Integer id);
    boolean existsByNomeIgnoreCase(String nome);
}
