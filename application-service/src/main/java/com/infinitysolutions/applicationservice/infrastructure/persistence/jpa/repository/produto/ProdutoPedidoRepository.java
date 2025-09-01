package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto;

import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido, Integer> {
    
    @Query("SELECT COUNT(pp) > 0 FROM ProdutoPedido pp WHERE pp.produtoEntity.id = :produtoId")
    boolean existsByProdutoEntityId(@Param("produtoId") Integer produtoId);
}
