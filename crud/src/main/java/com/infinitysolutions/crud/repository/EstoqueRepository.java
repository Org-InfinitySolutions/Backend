package com.infinitysolutions.crud.repository;

import com.infinitysolutions.crud.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
}
