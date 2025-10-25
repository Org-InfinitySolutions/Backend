package com.infinitysolutions.applicationservice.core.domain.usuario;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Senha;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Entidade de domínio que representa as credenciais de um usuário.
 * 
 * Responsável por encapsular o email e senha de acesso ao sistema,
 * usando value objects para garantir validação e consistência.
 */
public class Credencial {
    
    private final UUID usuarioId;
    private Email email;
    private String hashSenha;
    private LocalDateTime ultimoLogin;
    private boolean ativo;
    private List<Cargo> cargos = new ArrayList<>();
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    private Credencial(UUID usuarioId, Email email, String hashSenha, Set<Cargo> cargosEncontrados, LocalDateTime ultimoLogin) {
        if (usuarioId == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email não pode ser nulo");
        }
        if (hashSenha == null || hashSenha.isBlank()) {
            throw new IllegalArgumentException("Hash da senha não pode ser nulo ou vazio");
        }
        if (ultimoLogin != null) {
            this.ultimoLogin = ultimoLogin;
        }

        this.usuarioId = usuarioId;
        this.email = email;
        this.hashSenha = hashSenha;
        this.ativo = true;
        this.cargos.addAll(cargosEncontrados);
    }

    public static Credencial of(UUID usuarioId, String emailString, String senhaString) {
        Email email = Email.of(emailString);
        Senha senha = Senha.ofSistema(senhaString);
        String hashSenha = encoder.encode(senha.getValor());
        return new Credencial(usuarioId, email, hashSenha, new HashSet<>(), null);
    }

    public static Credencial ofEntity(UUID usuarioId, String emailString, String hashSenha, Set<Cargo> cargos, LocalDateTime ultimoLogin) {
        Email email = Email.of(emailString);
        return new Credencial(usuarioId, email, hashSenha, cargos, ultimoLogin);
    }

    public List<Cargo> getCargos() {
        return cargos;
    }

    public void addCargo(Cargo cargo) {
        this.cargos.add(cargo);
    }

    public void alterarEmail(String novoEmailString) {
        this.email = Email.of(novoEmailString);
    }

    public void alterarSenha(String senhaString) {
        Senha senha = Senha.ofSistema(senhaString);
        this.hashSenha = encoder.encode(senha.getValor());
    }
    public void registrarLogin() {
        this.ultimoLogin = LocalDateTime.now();
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }
    
    public UUID getUsuarioId() {
        return usuarioId;
    }
    
    public Email getEmail() {
        return email;
    }

    public String getEmailValor() {
        return email.getValor();
    }
    
    public String getHashSenha() {
        return hashSenha;
    }
    
    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }
    
    public boolean isAtivo() {
        return ativo;
    }

    public boolean validarSenha(String senha) {
        return encoder.matches(senha, getHashSenha());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Credencial that = (Credencial) o;
        return Objects.equals(usuarioId, that.usuarioId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(usuarioId);
    }
    
    @Override
    public String toString() {
        return "Credencial{" +
                "usuarioId=" + usuarioId +
                ", email=" + email +
                ", ativo=" + ativo +
                ", ultimoLogin=" + ultimoLogin +
                '}';
    }

    public void removeCargo(Cargo cargoFuncionario) {
        this.cargos.remove(cargoFuncionario);
    }
}
