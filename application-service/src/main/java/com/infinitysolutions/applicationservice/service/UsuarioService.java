package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.service.strategy.UsuarioStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {


    private final List<UsuarioStrategy> strategies;
    private final EnderecoService enderecoService;


    public void cadastrar(UsuarioCadastroDTO usuarioCadastroDTO) {
        log.info("Cadastrando Usuario");
      var estrategia = strategies.stream()
              .filter(strategy -> strategy.getTipoDTO().equals(usuarioCadastroDTO.getClass()))
              .findFirst().orElseThrow(() -> new IllegalArgumentException("Estratégia não encontrada para o tipo de usuário: " + usuarioCadastroDTO.getClass()));


      Endereco enderecoEncontrado = enderecoService.buscarEndereco(usuarioCadastroDTO.getEndereco());

      Usuario usuarioCadastrado = estrategia.cadastrar(usuarioCadastroDTO);
    }

}
