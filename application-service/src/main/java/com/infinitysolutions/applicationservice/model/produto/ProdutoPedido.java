package com.infinitysolutions.applicationservice.model.produto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.infinitysolutions.applicationservice.model.Pedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "produto_pedido")
public class ProdutoPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "pedido_id", referencedColumnName = "id")
    @JsonBackReference
    private Pedido pedido;

    @Column(name = "qtd")
    @Positive
    private Integer qtd;
}
