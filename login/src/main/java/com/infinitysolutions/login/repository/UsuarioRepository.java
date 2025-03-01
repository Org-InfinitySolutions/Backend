package com.infinitysolutions.login.repository;

import com.infinitysolutions.login.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

}
