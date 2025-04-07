package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.PessoaFisica;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaAtualizacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaCadastroDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PessoaFisicaMapper {

    @Mapping(target = "copiaRg", ignore = true)
    @Mapping(target = "id", ignore = true)


    @Mapping(target = "usuario.nome", source = "nome")
    @Mapping(target = "usuario.telefoneCelular", source = "telefoneCelular")
    @Mapping(target = "usuario.endereco", source = "endereco")
    void atualizarPessoaFisica(PessoaFisicaAtualizacaoDTO source, @MappingTarget PessoaFisica target);
}