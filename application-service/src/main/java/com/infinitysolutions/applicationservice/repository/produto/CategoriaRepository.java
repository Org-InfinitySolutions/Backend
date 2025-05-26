package com.infinitysolutions.applicationservice.repository.produto;

import com.infinitysolutions.applicationservice.model.produto.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findAllByIsAtivoTrue();
    Optional<Categoria> findByIdAndIsAtivoTrue(Integer id);
    boolean existsByNomeIgnoreCase(String nome);
}
