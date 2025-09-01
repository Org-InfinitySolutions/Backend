package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArquivoMetadadosRepository extends JpaRepository<ArquivoMetadadosEntity, Long> {
    List<ArquivoMetadadosEntity> findByProduto(ProdutoEntity produtoEntity);
    List<ArquivoMetadadosEntity> findByUsuario(UsuarioEntity usuarioEntity);
}
