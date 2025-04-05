package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
}
