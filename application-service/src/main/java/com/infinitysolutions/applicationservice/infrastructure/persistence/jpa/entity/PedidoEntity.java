package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity;

import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido.PedidoCadastroDTO;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoPedido;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedidoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
public class PedidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_usuario", nullable = false)
    private UsuarioEntity usuarioEntity;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    @ToString.Exclude
    private List<ProdutoPedidoEntity> produtosPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_endereco_entrega")
    private EnderecoEntity enderecoEntity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoPedido situacao = SituacaoPedido.EM_ANALISE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPedido tipo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;    @Column(name = "data_atualizacao", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_aprovacao")
    private LocalDateTime dataAprovacao;

    @Column(name = "data_inicio_evento")
    private LocalDateTime dataInicioEvento;

    @Column(name = "data_finalizacao")
    private LocalDateTime dataFinalizacao;

    @Column(name = "data_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "data_retirada")
    private LocalDateTime dataRetirada;

    @Column(name = "descricao")
    private String descricao;

    @OneToMany(
            mappedBy = "pedido",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ArquivoMetadadosEntity> documentos = new ArrayList<>();


    public Integer getQtdItens(){
        if (this.produtosPedido == null || this.produtosPedido.isEmpty()) return 0;

        return this.produtosPedido.stream().mapToInt(ProdutoPedidoEntity::getQtd).sum();
    }

    // Removed @PrePersist method that was resetting produtosPedido list
    // This was causing issues with product associations being lost during persistence
}
