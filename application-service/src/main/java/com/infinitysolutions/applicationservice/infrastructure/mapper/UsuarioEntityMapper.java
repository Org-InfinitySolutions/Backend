package com.infinitysolutions.applicationservice.infrastructure.mapper;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaFisica;
import com.infinitysolutions.applicationservice.core.domain.usuario.PessoaJuridica;
import com.infinitysolutions.applicationservice.core.domain.usuario.Usuario;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.exception.EstrategiaNaoEncontradaException;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.AtualizarUsuarioInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridicaInput;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PJAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.ArquivoMetadadosEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PessoaFisicaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PessoaJuridicaEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PFRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PessoaJuridicaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PJRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.old.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UsuarioEntityMapper {

    private final FileUploadService fileUploadService;

    public PessoaFisicaEntity toPessoaFisica(String nome, String telefoneCelular, String rg, String cpf) {
        return new PessoaFisicaEntity(nome, telefoneCelular, rg, cpf);
    }

    public PessoaJuridicaEntity toPessoaJuridica(String nome, String telefoneCelular, String telefoneResidencial, String cnpj, String razaoSocial) {
        return new PessoaJuridicaEntity(nome, telefoneCelular, telefoneResidencial, cnpj, razaoSocial);
    }

    public Usuario toDomain(UsuarioEntity userEntity) {
        if (userEntity instanceof PessoaFisicaEntity pessoaFisicaEntity) {
            return toPessoaFisicaDomain(pessoaFisicaEntity);
        } else if (userEntity instanceof PessoaJuridicaEntity pessoaJuridicaEntity) {
            return toPessoaJuridicaDomain(pessoaJuridicaEntity);
        }

        throw new EstrategiaNaoEncontradaException("Tipo de usuário não encontrado");
    }

    
    private PessoaFisica toPessoaFisicaDomain(PessoaFisicaEntity pessoaFisica) {
        return new PessoaFisica(
                pessoaFisica.getId(),
                pessoaFisica.getNome(),
                pessoaFisica.getTelefoneCelular(),
                toEnderecoDomain(pessoaFisica.getEnderecoEntity()),
                TipoUsuario.PF,
                pessoaFisica.getDataCriacao(),
                pessoaFisica.getDataAtualizacao(),
                pessoaFisica.isAtivo(),
                pessoaFisica.getCpf(),
                pessoaFisica.getRg(),
                toDocumentosDomain(pessoaFisica.getDocumentos())
        );
    }
    
    private PessoaJuridica toPessoaJuridicaDomain(PessoaJuridicaEntity pessoaJuridica) {
        return new PessoaJuridica(
                pessoaJuridica.getId(),
                pessoaJuridica.getNome(),
                pessoaJuridica.getTelefoneCelular(),
                toEnderecoDomain(pessoaJuridica.getEnderecoEntity()),
                TipoUsuario.PJ,
                pessoaJuridica.getDataCriacao(),
                pessoaJuridica.getDataAtualizacao(),
                pessoaJuridica.isAtivo(),
                pessoaJuridica.getCnpj(),
                pessoaJuridica.getRazaoSocial(),
                pessoaJuridica.getTelefoneResidencial(),
                toDocumentosDomain(pessoaJuridica.getDocumentos())
        );
    }
    
    private com.infinitysolutions.applicationservice.core.domain.Endereco toEnderecoDomain(com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity enderecoEntity) {
        return new com.infinitysolutions.applicationservice.core.domain.Endereco(
                enderecoEntity.getId(),
                enderecoEntity.getCep(),
                enderecoEntity.getLogradouro(),
                enderecoEntity.getBairro(),
                enderecoEntity.getCidade(),
                enderecoEntity.getEstado(),
                enderecoEntity.getNumero(),
                enderecoEntity.getComplemento()
        );
    }
    
    private List<ArquivoMetadado> toDocumentosDomain(List<ArquivoMetadadosEntity> documentos) {
        return documentos.stream()
                .map(doc -> new ArquivoMetadado(
                        doc.getId(),
                        doc.getBlobName(),
                        doc.getBlobUrl(),
                        doc.getOriginalFilename(),
                        doc.getMimeType(),
                        doc.getFileSize(),
                        doc.getTipoAnexo()
                ))
                .toList();
    }

    public UsuarioRespostaCadastroDTO toUsuarioRespostaCadastroDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        if (usuario.getTipoUsuario().equals(TipoUsuario.PF)) return toPessoaFisicaRespostaCadastroDTO((PessoaFisica) usuario);
        return toPessoaJuridicaRespostaCadastroDTO((PessoaJuridica) usuario);
    }

    public PFRespostaCadastroDTO toPessoaFisicaRespostaCadastroDTO(PessoaFisica pessoaFisica) {
        return new PFRespostaCadastroDTO(
                pessoaFisica.getId(),
                pessoaFisica.getNome(),
                pessoaFisica.getTelefoneCelular(),
                new EnderecoResumidoDTO(
                        pessoaFisica.getEndereco().getCep(),
                        pessoaFisica.getEndereco().getLogradouro(),
                        pessoaFisica.getEndereco().getNumero(),
                        pessoaFisica.getEndereco().getCidade(),
                        pessoaFisica.getEndereco().getEstado()),
                pessoaFisica.getCpfValueObject().getValorFormatado());
    }

    public PJRespostaCadastroDTO toPessoaJuridicaRespostaCadastroDTO(PessoaJuridica pessoaJuridica) {
        return new PJRespostaCadastroDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getNome(),
                pessoaJuridica.getTelefoneCelular(),
                new EnderecoResumidoDTO(
                        pessoaJuridica.getEndereco().getCep(),
                        pessoaJuridica.getEndereco().getLogradouro(),
                        pessoaJuridica.getEndereco().getNumero(),
                        pessoaJuridica.getEndereco().getCidade(),
                        pessoaJuridica.getEndereco().getEstado()),
                pessoaJuridica.getCnpjValueObject().getValorFormatado(),
                pessoaJuridica.getRazaoSocial(),
                pessoaJuridica.getTelefoneResidencial()
        );
    }

    public UsuarioRespostaDTO toUsuarioRespostaDTO(Usuario usuario) {
        if (usuario.getTipoUsuario().equals(TipoUsuario.PF)) {
            return toPessoaFisicaDTO((PessoaFisica) usuario);
        }
        return toPessoaJuridicaDTO((PessoaJuridica) usuario);
    }

    public PessoaFisicaDTO toPessoaFisicaDTO(PessoaFisica pessoaFisica) {
        return new PessoaFisicaDTO(
                pessoaFisica.getId(),
                pessoaFisica.getNome(),
                null,
                pessoaFisica.getTelefoneCelular(),
                new EnderecoDTO(
                pessoaFisica.getEndereco().getCep(),
                pessoaFisica.getEndereco().getLogradouro(),
                pessoaFisica.getEndereco().getBairro(),
                pessoaFisica.getEndereco().getCidade(),
                pessoaFisica.getEndereco().getEstado(),
                pessoaFisica.getEndereco().getNumero(),
                pessoaFisica.getEndereco().getComplemento()
                ),
                pessoaFisica.getDataCriacao(),
                pessoaFisica.getDataAtualizacao(),
                pessoaFisica.getCpfValueObject().getValorFormatado(),
                pessoaFisica.getRgValueObject().getValorFormatado(),
                pessoaFisica.PossuiCopiaRg(),
                pessoaFisica.possuiComprovanteEndereco(),
                pessoaFisica.getPossuiCadastroCompleto(),
                pessoaFisica.getDocumentos().stream()
                        .map(documento -> new UsuarioRespostaDTO.DocumentoUsuarioDTO(
                                            documento.getOriginalFilename(),
                                            fileUploadService.generatePrivateFileSasUrl(documento.getBlobName(), 60),
                                            documento.getMimeType(),
                                            documento.getTipoAnexo().toString()
                                    )).toList()
        );
    }

    public PessoaJuridicaDTO toPessoaJuridicaDTO(PessoaJuridica pessoaJuridica) {

        return new PessoaJuridicaDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getNome(),
                null,
                pessoaJuridica.getTelefoneCelular(),
                pessoaJuridica.getTelefoneResidencial(),
                new EnderecoDTO(
                        pessoaJuridica.getEndereco().getCep(),
                        pessoaJuridica.getEndereco().getLogradouro(),
                        pessoaJuridica.getEndereco().getBairro(),
                        pessoaJuridica.getEndereco().getCidade(),
                        pessoaJuridica.getEndereco().getEstado(),
                        pessoaJuridica.getEndereco().getNumero(),
                        pessoaJuridica.getEndereco().getComplemento()
                ),
                pessoaJuridica.getDataCriacao(),
                pessoaJuridica.getDataAtualizacao(),
                pessoaJuridica.getCnpj(),
                pessoaJuridica.getRazaoSocial(),
                pessoaJuridica.possuiContratoSocial(),
                pessoaJuridica.possuiCartaoCnpj(),
                pessoaJuridica.possuiComprovanteEndereco(),
                pessoaJuridica.getPossuiCadastroCompleto(),
                pessoaJuridica.getDocumentos().stream().map(documento -> new UsuarioRespostaDTO.DocumentoUsuarioDTO(
                        documento.getOriginalFilename(),
                        fileUploadService.generatePrivateFileSasUrl(documento.getBlobName(), 60),
                        documento.getMimeType(),
                        documento.getTipoAnexo().toString()
                )).toList()
        );
    }

    public AtualizarUsuarioInput toAtualizarUsuarioInput(UsuarioAtualizacaoDTO usuarioAtualizacaoDTO) {
        return switch (usuarioAtualizacaoDTO.getTipo()) {
            case "PF" -> {
              PessoaFisicaAtualizacaoDTO dto = (PessoaFisicaAtualizacaoDTO) usuarioAtualizacaoDTO;
              yield toAtualizarPessoaFisicaInput(dto.getNome(), dto.getTelefoneCelular(), new EnderecoInput(
                      dto.getEndereco().getCep(),
                      dto.getEndereco().getLogradouro(),
                      dto.getEndereco().getBairro(),
                      dto.getEndereco().getCidade(),
                      dto.getEndereco().getEstado(),
                      dto.getEndereco().getNumero(),
                      dto.getEndereco().getComplemento()
              ));
            }
            case "PJ" -> {
                PJAtualizacaoDTO dto = (PJAtualizacaoDTO) usuarioAtualizacaoDTO;
                yield toAtualizarPessoaJuridicaInput(dto.getNome(), dto.getTelefoneCelular(),
                        new EnderecoInput(
                                dto.getEndereco().getCep(),
                                dto.getEndereco().getLogradouro(),
                                dto.getEndereco().getBairro(),
                                dto.getEndereco().getCidade(),
                                dto.getEndereco().getEstado(),
                                dto.getEndereco().getNumero(),
                                dto.getEndereco().getComplemento()
                        ),
                        dto.getRazaoSocial(),
                        dto.getTelefoneResidencial()
                );

            }
            default -> throw new IllegalStateException("Unexpected value: " + usuarioAtualizacaoDTO.getTipo());
        };
    }

    public AtualizarPessoaFisicaInput toAtualizarPessoaFisicaInput(String nome, String telefoneCelular, EnderecoInput endereco) {
        return new AtualizarPessoaFisicaInput(nome, telefoneCelular, endereco);
    }

    public AtualizarPessoaJuridicaInput toAtualizarPessoaJuridicaInput(String nome, String telefoneCelular, EnderecoInput endereco, String razaoSocial, String telefoneResidencial) {
        return new AtualizarPessoaJuridicaInput(nome, telefoneCelular, endereco, razaoSocial, telefoneResidencial);
    }
}
