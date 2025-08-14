package com.infinitysolutions.applicationservice.repository;

import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    /**
     * Busca tipos de evento mais populares nos últimos meses
     */
    @Query("SELECT p.tipo, COUNT(p) as quantidade " +
           "FROM Pedido p " +
           "WHERE p.dataCriacao >= :dataInicio " +
           "GROUP BY p.tipo " +
           "ORDER BY quantidade DESC")
    List<Object[]> findTipoEventoMaisPopular(@Param("dataInicio") LocalDateTime dataInicio);

    /**
     * Busca categorias mais populares nos últimos meses
     */
    @Query("SELECT c.nome, COUNT(pp) as quantidade " +
           "FROM Pedido p " +
           "JOIN p.produtosPedido pp " +
           "JOIN pp.produto prod " +
           "JOIN prod.categoria c " +
           "WHERE p.dataCriacao >= :dataInicio " +
           "GROUP BY c.nome " +
           "ORDER BY quantidade DESC")
    List<Object[]> findCategoriaMaisPopular(@Param("dataInicio") LocalDateTime dataInicio);    /**
     * Busca tempo médio de atendimento (tempo entre criação e aprovação do pedido)
     * Retorna data de criação (início em análise) e data de aprovação
     * Considera apenas pedidos que foram aprovados (têm dataAprovacao preenchida)
     */
    @Query("SELECT p.dataCriacao, p.dataAprovacao " +
           "FROM Pedido p " +
           "WHERE p.dataAprovacao IS NOT NULL")
    List<Object[]> findTempoMedioAtendimento();

    /**
     * Busca tempo médio de duração de eventos (tempo entre início do evento e finalização)
     */
    @Query("SELECT p.dataInicioEvento, p.dataFinalizacao " +
           "FROM Pedido p " +
           "WHERE p.dataInicioEvento IS NOT NULL AND p.dataFinalizacao IS NOT NULL")
    List<Object[]> findTempoMedioDuracaoEvento();

    /**
     * Busca pedidos aprovados em um período específico
     */
    @Query("SELECT p FROM Pedido p " +
           "WHERE p.dataAprovacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY p.dataAprovacao DESC")
    List<Pedido> findPedidosAprovadosEntreDatas(@Param("dataInicio") LocalDateTime dataInicio, 
                                                @Param("dataFim") LocalDateTime dataFim);

    /**
     * Busca quantidade de pedidos por mês
     */
    @Query("SELECT YEAR(p.dataCriacao) as ano, MONTH(p.dataCriacao) as mes, COUNT(p) as quantidade " +
           "FROM Pedido p " +
           "WHERE p.dataCriacao >= :dataInicio " +
           "GROUP BY YEAR(p.dataCriacao), MONTH(p.dataCriacao) " +
           "ORDER BY ano DESC, mes DESC")
    List<Object[]> findPedidosPorMes(@Param("dataInicio") LocalDateTime dataInicio);

    /**
     * Busca equipamentos mais populares
     */
    @Query(value = "SELECT CONCAT(prod.modelo, ' - ', prod.marca) as equipamento, " +
                   "COUNT(pp.id) as quantidade " +
                   "FROM pedidos p " +
                   "JOIN produto_pedido pp ON pp.pedido_id = p.id " +
                   "JOIN produtos prod ON prod.id = pp.produto_id " +
                   "WHERE p.data_criacao >= :dataInicio " +
                   "GROUP BY prod.id, prod.modelo, prod.marca " +
                   "ORDER BY quantidade DESC " +
                   "LIMIT :limite", 
           nativeQuery = true)
    List<Object[]> findEquipamentosPopulares(@Param("dataInicio") LocalDateTime dataInicio, 
                                             @Param("limite") int limite);
}
