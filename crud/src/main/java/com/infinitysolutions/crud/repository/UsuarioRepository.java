package com.infinitysolutions.crud.repository;

import com.infinitysolutions.crud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
