package com.infinitysolutions.applicationservice.repository.produto;

import com.infinitysolutions.applicationservice.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    List<Produto> findAllByIsAtivoTrue();
    Optional<Produto> findByIdAndIsAtivoTrue(Integer integer);
}
