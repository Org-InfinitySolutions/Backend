package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.PessoaJuridica;
import com.infinitysolutions.applicationservice.model.dto.pessoa.juridica.PessoaJuridicaAtualizacaoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PessoaJuridicaMapper {

    @Mapping(target = "id", ignore = true)

    @Mapping(target = "usuario.nome", source = "nome")
    @Mapping(target = "usuario.telefoneCelular", source = "telefoneCelular")
    @Mapping(target = "usuario.endereco", source = "endereco")

    @Mapping(target = "razaoSocial", source = "razaoSocial")
    @Mapping(target = "telefoneResidencial", source = "telefoneResidencial")
    void atualizarPessoaJuridica(PessoaJuridicaAtualizacaoDTO source, @MappingTarget PessoaJuridica target);
}
