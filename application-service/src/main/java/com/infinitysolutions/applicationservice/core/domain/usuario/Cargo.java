package com.infinitysolutions.applicationservice.core.domain.usuario;

import com.infinitysolutions.applicationservice.core.domain.valueobject.NomeCargo;

import java.util.Objects;

/**
 * Entidade de domínio que representa um cargo/função de usuário no sistema.
 * 
 * Define os diferentes tipos de acesso e permissões que um usuário pode ter,
 * encapsulando as regras de negócio relacionadas a cargos.
 */
public class Cargo {
    
    private final Integer id;
    private final NomeCargo nome;
    private String descricao;
    
    public Cargo(Integer id, NomeCargo nome, String descricao) {
        if (nome == null) {
            throw new IllegalArgumentException("Nome do cargo não pode ser nulo");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descrição do cargo não pode ser nula ou vazia");
        }
        
        this.id = id;
        this.nome = nome;
        this.descricao = descricao.trim();
    }
    
    /**
     * Cria um novo cargo sem ID (para criação).
     * 
     * @param nome Nome/tipo do cargo
     * @param descricao Descrição detalhada do cargo
     * @return Nova instância de Cargo
     */
    public static Cargo criar(NomeCargo nome, String descricao) {
        return new Cargo(null, nome, descricao);
    }
    
    /**
     * Cria um cargo existente (com ID).
     * 
     * @param id ID único do cargo
     * @param nome Nome/tipo do cargo
     * @param descricao Descrição detalhada do cargo
     * @return Instância de Cargo existente
     */
    public static Cargo reconstituir(Integer id, NomeCargo nome, String descricao) {
        return new Cargo(id, nome, descricao);
    }
    
    /**
     * Altera a descrição do cargo.
     * 
     * @param novaDescricao Nova descrição do cargo
     */
    public void alterarDescricao(String novaDescricao) {
        if (novaDescricao == null || novaDescricao.isBlank()) {
            throw new IllegalArgumentException("Nova descrição não pode ser nula ou vazia");
        }
        this.descricao = novaDescricao.trim();
    }
    
    /**
     * Verifica se este cargo é de administrador.
     * 
     * @return true se for cargo de admin, false caso contrário
     */
    public boolean isAdmin() {
        return nome == NomeCargo.ADMIN;
    }
    
    /**
     * Verifica se este cargo é de funcionário.
     * 
     * @return true se for cargo de funcionário, false caso contrário
     */
    public boolean isFuncionario() {
        return nome == NomeCargo.FUNCIONARIO;
    }
    
    /**
     * Verifica se este cargo é de usuário (PF ou PJ).
     * 
     * @return true se for cargo de usuário, false caso contrário
     */
    public boolean isUsuario() {
        return nome == NomeCargo.USUARIO_PF || nome == NomeCargo.USUARIO_PJ;
    }
    
    /**
     * Verifica se este cargo é de usuário pessoa física.
     * 
     * @return true se for cargo de usuário PF, false caso contrário
     */
    public boolean isUsuarioPF() {
        return nome == NomeCargo.USUARIO_PF;
    }
    
    /**
     * Verifica se este cargo é de usuário pessoa jurídica.
     * 
     * @return true se for cargo de usuário PJ, false caso contrário
     */
    public boolean isUsuarioPJ() {
        return nome == NomeCargo.USUARIO_PJ;
    }
    
    /**
     * Verifica se este cargo tem permissões administrativas.
     * 
     * @return true se tiver permissões admin, false caso contrário
     */
    public boolean temPermissoesAdministrativas() {
        return isAdmin() || isFuncionario();
    }
    
    public Integer getId() {
        return id;
    }
    
    public NomeCargo getNome() {
        return nome;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    /**
     * Verifica se este cargo é novo (ainda não persistido).
     * 
     * @return true se não tiver ID, false caso contrário
     */
    public boolean isNovo() {
        return id == null;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cargo cargo = (Cargo) o;
        
        // Se ambos têm ID, compara pelo ID
        if (id != null && cargo.id != null) {
            return Objects.equals(id, cargo.id);
        }
        
        // Se não têm ID, compara pelo nome (business key)
        return Objects.equals(nome, cargo.nome);
    }
    
    @Override
    public int hashCode() {
        // Use o nome como hash code para consistência
        return Objects.hash(nome);
    }
    
    @Override
    public String toString() {
        return "Cargo{" +
                "id=" + id +
                ", nome=" + nome +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
