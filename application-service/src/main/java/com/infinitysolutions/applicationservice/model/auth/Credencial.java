package com.infinitysolutions.applicationservice.model.auth;

import jakarta.persistence.*;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Credencial {

    @Id
    @Column(name = "fk_usuario", unique = true, nullable = false)
    private UUID fkUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "hash_senha", nullable = false)
    private String hashSenha;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "credencial_cargo",
            joinColumns = @JoinColumn(name = "fk_usuario"),
            inverseJoinColumns = @JoinColumn(name = "cargo_id")
    )
    private Set<Cargo> cargos;

    @DefaultValue("true")
    @Column(nullable = false)
    private boolean ativo = true;

    public Credencial(UUID fkUsuario, String email, String hashSenha){
        this.fkUsuario = fkUsuario;
        this.email = email;
        this.hashSenha = hashSenha;
        this.cargos = new HashSet<>();
    }

    public void atualizarUltimoLogin() {
        this.ultimoLogin = LocalDateTime.now();
    }
}
