package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.ArquivoMetadados;
import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface ArquivoMetadadosRepository extends JpaRepository<ArquivoMetadados, Long> {
    List<ArquivoMetadados> findByProduto(Produto produto);
    List<ArquivoMetadados> findByUsuario(Usuario usuario);
    List<ArquivoMetadados> findByProdutoIdAndTipoAnexo(Long productId, TipoAnexo tipoAnexo);

}
