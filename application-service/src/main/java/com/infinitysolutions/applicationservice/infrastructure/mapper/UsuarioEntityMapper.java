package com.infinitysolutions.applicationservice.infrastructure.mapper;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.Endereco;
import com.infinitysolutions.applicationservice.core.domain.usuario.*;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.exception.EstrategiaNaoEncontradaException;
import com.infinitysolutions.applicationservice.core.usecases.endereco.EnderecoInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.AtualizarUsuarioInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoafisica.AtualizarPessoaFisicaInput;
import com.infinitysolutions.applicationservice.core.usecases.usuario.pessoajuridica.AtualizarPessoaJuridicaInput;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.FuncRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PJAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioAtualizacaoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.*;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PessoaFisicaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.fisica.PFRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PessoaJuridicaDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pessoa.juridica.PJRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.infrastructure.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class UsuarioEntityMapper {

    private final S3FileUploadService fileUploadService;

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
    
    private Endereco toEnderecoDomain(EnderecoEntity enderecoEntity) {
        return new Endereco(
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

    public UsuarioRespostaCadastroDTO toUsuarioRespostaCadastroDTO(Usuario usuario, Credencial identificacao) {
        if (usuario == null) {
            return null;
        }

        List<Cargo> cargos = identificacao.getCargos();

        UsuarioRespostaCadastroDTO response = null;

        for (Cargo cargo : cargos) {
            response = switch (cargo.getNome()) {
                case USUARIO_PF -> toPessoaFisicaRespostaCadastroDTO((PessoaFisica) usuario, identificacao);
                case USUARIO_PJ -> toPessoaJuridicaRespostaCadastroDTO((PessoaJuridica) usuario, identificacao);
                case FUNCIONARIO -> toFuncionarioRespostaCadastroDTO((PessoaFisica) usuario, identificacao);
                default -> response = null;
            };
        }

        return response;

    }

    public PFRespostaCadastroDTO toPessoaFisicaRespostaCadastroDTO(PessoaFisica pessoaFisica, Credencial identificacao) {
        return new PFRespostaCadastroDTO(
                pessoaFisica.getId(),
                pessoaFisica.getNome(),
                pessoaFisica.getTelefoneCelular(),
                identificacao.getEmailValor(),
                new EnderecoResumidoDTO(
                        pessoaFisica.getEndereco().getCep(),
                        pessoaFisica.getEndereco().getLogradouro(),
                        pessoaFisica.getEndereco().getNumero(),
                        pessoaFisica.getEndereco().getCidade(),
                        pessoaFisica.getEndereco().getEstado()),
                pessoaFisica.getCpfValueObject().getValorFormatado());
    }

    public PJRespostaCadastroDTO toPessoaJuridicaRespostaCadastroDTO(PessoaJuridica pessoaJuridica, Credencial identificacao) {
        return new PJRespostaCadastroDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getNome(),
                pessoaJuridica.getTelefoneCelular(),
                identificacao.getEmailValor(),
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

    public FuncRespostaCadastroDTO toFuncionarioRespostaCadastroDTO(PessoaFisica pessoaFisica, Credencial identificacao) {
        return new FuncRespostaCadastroDTO(
                pessoaFisica.getId(),
                pessoaFisica.getNome(),
                pessoaFisica.getTelefoneCelular(),
                identificacao.getEmailValor(),
                new EnderecoResumidoDTO(
                        pessoaFisica.getEndereco().getCep(),
                        pessoaFisica.getEndereco().getLogradouro(),
                        pessoaFisica.getEndereco().getNumero(),
                        pessoaFisica.getEndereco().getCidade(),
                        pessoaFisica.getEndereco().getEstado()),
                pessoaFisica.getCpfValueObject().getValorFormatado());
    }

    public UsuarioRespostaDTO toUsuarioRespostaDTO(Usuario usuario, Credencial credencial) {
        if (usuario.getTipoUsuario().equals(TipoUsuario.PF)) {
            return toPessoaFisicaDTO((PessoaFisica) usuario, credencial);
        }
        return toPessoaJuridicaDTO((PessoaJuridica) usuario, credencial);
    }

    public UsuarioRespostaDTO toUsuarioRespostaDTO(Usuario usuario) {
        if (usuario.getTipoUsuario().equals(TipoUsuario.PF)) {
            return toPessoaFisicaDTO((PessoaFisica) usuario, null);
        }
        return toPessoaJuridicaDTO((PessoaJuridica) usuario, null);
    }

    public PessoaFisicaDTO toPessoaFisicaDTO(PessoaFisica pessoaFisica, Credencial credencial) {
        return new PessoaFisicaDTO(
                pessoaFisica.getId(),
                pessoaFisica.getNome(),
                credencial != null ? credencial.getEmailValor() : null,
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
                                            fileUploadService.generatePrivateFilePresignedUrl(documento.getBlobName(), 60),
                                            documento.getMimeType(),
                                            documento.getTipoAnexo().toString()
                                    )).toList()
        );
    }

    public PessoaJuridicaDTO toPessoaJuridicaDTO(PessoaJuridica pessoaJuridica, Credencial credencial) {

        return new PessoaJuridicaDTO(
                pessoaJuridica.getId(),
                pessoaJuridica.getNome(),
                credencial != null ? credencial.getEmailValor() : null,
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
                        fileUploadService.generatePrivateFilePresignedUrl(documento.getBlobName(), 60),
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
