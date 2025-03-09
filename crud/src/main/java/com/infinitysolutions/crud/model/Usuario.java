package com.infinitysolutions.crud.model;

import com.infinitysolutions.crud.model.dto.UsuarioDtoCriacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "O nome completo é obrigatório.")
    private String nomeCompleto;

    @NotBlank(message = "O Nome Fantasia é obrigatório.")
    private String nomeFantasia;

    @NotNull(message = "A Razão Social é obrigatório.")
    private String razaoSocial;

    @NotBlank(message = "O CNPJ é obrigatório.")
    private String cnpj;

    @NotBlank(message = "O CPF é obrigatório.")
    private String cpf;

    @NotBlank(message = "O RG é obrigatório.")
    private String rg;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    public Usuario() {}

    public Usuario(UsuarioDtoCriacao dto) {
        this.nomeCompleto = dto.nomeCompleto();
        this.nomeFantasia = dto.nomeFantasia();
        this.razaoSocial = dto.razaoSocial();
        this.cnpj = dto.cnpj();
        this.cpf = dto.cpf();
        this.rg = dto.rg();
        this.email = dto.email();
        this.telefone = dto.telefone();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "O nome completo é obrigatório.") String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(@NotBlank(message = "O nome completo é obrigatório.") String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public @NotBlank(message = "O Nome Fantasia é obrigatório.") String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(@NotBlank(message = "O Nome Fantasia é obrigatório.") String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public @NotNull(message = "A Razão Social é obrigatório.") String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(@NotNull(message = "A Razão Social é obrigatório.") String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public @NotBlank(message = "O CNPJ é obrigatório.") String getCnpj() {
        return cnpj;
    }

    public void setCnpj(@NotBlank(message = "O CNPJ é obrigatório.") String cnpj) {
        this.cnpj = cnpj;
    }

    public @NotBlank(message = "O CPF é obrigatório.") String getCpf() {
        return cpf;
    }

    public void setCpf(@NotBlank(message = "O CPF é obrigatório.") String cpf) {
        this.cpf = cpf;
    }

    public @NotBlank(message = "O RG é obrigatório.") String getRg() {
        return rg;
    }

    public void setRg(@NotBlank(message = "O RG é obrigatório.") String rg) {
        this.rg = rg;
    }

    public @Email(message = "E-mail inválido.") @NotBlank(message = "O e-mail é obrigatório.") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "E-mail inválido.") @NotBlank(message = "O e-mail é obrigatório.") String email) {
        this.email = email;
    }

    public @NotBlank(message = "O telefone é obrigatório.") String getTelefone() {
        return telefone;
    }

    public void setTelefone(@NotBlank(message = "O telefone é obrigatório.") String telefone) {
        this.telefone = telefone;
    }
}
