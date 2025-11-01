package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Integer> {
    List<ProdutoEntity> findAllByIsAtivoTrue();
    Optional<ProdutoEntity> findByIdAndIsAtivoTrue(Integer integer);
    Set<ProdutoEntity> findByIdInAndIsAtivoTrue(Set<Integer> ids);

    Page<ProdutoEntity> findAllByIsAtivoTrue(Pageable pageable);
}
