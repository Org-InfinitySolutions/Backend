package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {
    List<ProdutoEntity> findAllByIsAtivoTrue();
    Optional<ProdutoEntity> findByIdAndIsAtivoTrue(Integer integer);
}
