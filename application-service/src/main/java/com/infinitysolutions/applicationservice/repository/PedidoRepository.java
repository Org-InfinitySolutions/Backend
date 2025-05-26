package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuarioOrderByDataCriacaoDesc(Usuario usuario);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario ORDER BY p.dataCriacao DESC")
    List<Pedido> findAllWithUsuarioOrderByDataCriacaoDesc();

    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario WHERE p.usuario.id = :usuarioId ORDER BY p.dataCriacao DESC")
    List<Pedido> findByUsuarioIdOrderByDataCriacaoDesc(@Param("usuarioId") UUID usuarioId);

    @Query("SELECT p FROM Pedido p JOIN FETCH p.usuario WHERE p.id = :id AND p.usuario.id = :usuarioId")
    Optional<Pedido> findWithUsuarioByIdAndByUsuarioId(@Param("id") Integer id, @Param("usuarioId") UUID usuarioId);

    @Query("SELECT p from Pedido p JOIN FETCH p.usuario WHERE p.id = :id")
    Optional<Pedido> findWithUsuarioById(@Param("id") Integer id);
}
