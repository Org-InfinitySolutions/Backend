package com.infinitysolutions.applicationservice.repository.produto;

import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido, Integer> {
}
