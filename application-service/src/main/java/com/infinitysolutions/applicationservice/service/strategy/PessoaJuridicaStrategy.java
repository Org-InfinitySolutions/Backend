package com.infinitysolutions.applicationservice.service.strategy;

import com.infinitysolutions.applicationservice.model.dto.PessoaFisicaRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.PessoaJuridicaCadastroDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PessoaJuridicaStrategy implements UsuarioStrategy<PessoaJuridicaCadastroDTO> {
    @Override
    public PessoaFisicaRespostaCadastroDTO cadastrar(PessoaJuridicaCadastroDTO usuarioCadastroDTO) {
        System.out.println("Cadastrando pessoa jurídica com CNPJ: " + usuarioCadastroDTO.getCnpj());
        return null;
    }

    @Override
    public void atualizar(PessoaJuridicaCadastroDTO usuarioCadastroDTO) {
        System.out.println("Atualizando pessoa jurídica com CNPJ: " + usuarioCadastroDTO.getCnpj());
    }

    @Override
    public void excluir(PessoaJuridicaCadastroDTO usuarioCadastroDTO) {
        System.out.println("Excluindo pessoa jurídica com CNPJ: " + usuarioCadastroDTO.getCnpj());
    }

    @Override
    public void buscarPorId(PessoaJuridicaCadastroDTO usuarioCadastroDTO) {
        System.out.println("Buscando pessoa jurídica com CNPJ: " + usuarioCadastroDTO.getCnpj());
    }

    @Override
    public void listarTodos() {
        System.out.println("Listando todas as pessoas jurídicas");
    }

    @Override
    public Class<PessoaJuridicaCadastroDTO> getTipoDTO() {
        return PessoaJuridicaCadastroDTO.class;
    }
}
