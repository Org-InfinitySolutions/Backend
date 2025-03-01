package com.infinitysolutions.login.model;

import com.infinitysolutions.login.model.dto.UsuarioDtoCriacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.DefaultValue;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Insira um nome válido.")
    @Column(nullable = false)
    private String nome;

    @Email(message = "Insira um email válido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Insira uma senha válida")
    @Column(nullable = false)
    private String senha;

    public Usuario(UsuarioDtoCriacao dto){
        this.nome = dto.nome();
        this.email = dto.email();
        this.senha = dto.senha();
    }

}
