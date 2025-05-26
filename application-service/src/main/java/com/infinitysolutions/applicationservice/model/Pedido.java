package com.infinitysolutions.applicationservice.model;

import com.infinitysolutions.applicationservice.model.dto.pedido.PedidoCadastroDTO;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import com.infinitysolutions.applicationservice.model.enums.TipoPedido;
import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Entity
@Table(name = "pedidos")
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_usuario", nullable = false)
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pedido_id")
    @Column(name = "produtos_pedido")
    private List<ProdutoPedido> produtosPedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_endereco_entrega")
    private Endereco endereco;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoPedido situacao = SituacaoPedido.EM_ANALISE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoPedido tipo;

    @Column(name = "data_criacao", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao", nullable = false)
    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    @Column(name = "data_retirada")
    private LocalDateTime dataRetirada;
    
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "documento_auxiliar")
    private byte[] documentoAuxiliar;

    public Pedido(PedidoCadastroDTO dto, Usuario usuario, Endereco endereco) {
        this.tipo = dto.tipo();
        this.dataEntrega = dto.dataEntrega();
        this.dataRetirada = dto.dataRetirada();
        this.descricao = dto.descricao();
        if (dto.documentoAuxiliar() != null) {
            this.documentoAuxiliar = dto.documentoAuxiliar();
        }else {
            this.documentoAuxiliar = null;
        }
        this.usuario = usuario;
        this.endereco = endereco;
    }

    public Integer getQtdItens(){
        if (this.produtosPedido == null || this.produtosPedido.isEmpty()) return 0;

        return this.produtosPedido.stream().mapToInt(ProdutoPedido::getQtd).sum();
    }

    @PrePersist
    public void PrePersist(){
        this.produtosPedido = new ArrayList<>();
    }
}
