package com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity;

import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.infrastructure.mapper.ArquivoMetadadosMapper;
import com.infinitysolutions.applicationservice.infrastructure.mapper.EnderecoMapper;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(
        name = "pessoa_juridica",
        indexes = {
                @Index(name = "idx_pj_telefone_residencial", columnList = "telefone_residencial"),
        }
)
public class PessoaJuridicaEntity extends UsuarioEntity {

    @Column(name = "telefone_residencial", nullable = false, length = 20)
    private String telefoneResidencial;

    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    public PessoaJuridicaEntity(String nome, String telefoneCelular, String telefoneResidencial, String cnpj, String razaoSocial) {
        super(nome, telefoneCelular);
        this.telefoneResidencial = telefoneResidencial;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
    }
}
