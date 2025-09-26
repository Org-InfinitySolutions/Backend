package com.infinitysolutions.applicationservice.core.domain.usuario;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Cpf;
import com.infinitysolutions.applicationservice.core.domain.valueobject.Rg;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisicaInput;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class PessoaFisica extends Usuario {

    private Cpf cpf;
    private Rg rg;
    private boolean possuiCopiaRg;

    public PessoaFisica(UUID id, String nome, String telefoneCelular, Endereco endereco, TipoUsuario tipoUsuario, String cpf, String rg) {
        super(id, nome, telefoneCelular, endereco, tipoUsuario);
        this.cpf = Cpf.of(cpf);
        this.rg = Rg.of(rg);
    }

    public PessoaFisica(
            UUID id,
            String nome,
            String telefoneCelular,
            Endereco endereco,
            TipoUsuario tipoUsuario,
            LocalDateTime dataCriacao,
            LocalDateTime dataAtualizacao,
            boolean isAtivo,
            String cpf,
            String rg,
            List<ArquivoMetadado> documentos
    ) {
        super(id, nome, telefoneCelular, endereco, tipoUsuario);
        setDataCriacao(dataCriacao);
        setDataAtualizacao(dataAtualizacao);
        setAtivo(isAtivo);
        this.cpf = Cpf.of(cpf);
        this.rg = Rg.of(rg);
        setDocumentos(documentos);
        setCadastroCompleto(documentos);
    }

    @Override
    public void setCadastroCompleto(List<ArquivoMetadado> documentos) {
        boolean possuiComprovanteEndereco = documentos.stream().anyMatch(d -> d.getTipoAnexo().equals(TipoAnexo.COMPROVANTE_ENDERECO));
        boolean possuiCopiaRG = documentos.stream().anyMatch(d -> d.getTipoAnexo().equals(TipoAnexo.COPIA_RG));
        this.setPossuiCopiaRg(possuiCopiaRG);
        this.setPossuiComprovanteEndereco(possuiComprovanteEndereco);
        this.possuiCadastroCompleto = possuiComprovanteEndereco && possuiCopiaRG;
    }

    public void atualizarDados(AtualizarPessoaFisicaInput input, Endereco endereco) {
        if (input.getNome() != null) setNome(input.getNome());
        if (input.getEndereco() != null) setEndereco(endereco);
        if (input.getTelefoneCelular() != null) setTelefoneCelular(input.getTelefoneCelular());
    }

    public String getCpf() {
        return cpf.getValor();
    }
    
    public Cpf getCpfValueObject() {
        return cpf;
    }

    public String getRg() {
        return rg.getValor();
    }
    
    public Rg getRgValueObject() {
        return rg;
    }

    public boolean PossuiCopiaRg() {
        return possuiCopiaRg;
    }

    public void setPossuiCopiaRg(boolean possuiCopiaRg) {
        this.possuiCopiaRg = possuiCopiaRg;
    }
}
