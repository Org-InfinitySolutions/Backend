package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.UsuarioCadastroDTO;
import com.infinitysolutions.applicationservice.model.dto.UsuarioRespostaCadastroDTO;
import com.infinitysolutions.applicationservice.service.strategy.UsuarioStrategy;
import jakarta.transaction.Transactional;
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


    @Transactional
    public UsuarioRespostaCadastroDTO cadastrar(UsuarioCadastroDTO usuarioCadastroDTO) {
      log.info("Iniciando processo de cadastro do Usuario");
      var estrategia = strategies.stream()
              .filter(strategy -> strategy.getTipoDTO().equals(usuarioCadastroDTO.getClass()))
              .findFirst().orElseThrow(() -> new IllegalArgumentException("Estratégia não encontrada para o tipo de usuário: " + usuarioCadastroDTO.getClass()));
      log.info("Estratégia encontrada: {}", estrategia.getClass().getSimpleName());

      log.info("Configurando endereço do Usuario");
      Endereco enderecoEncontrado = enderecoService.buscarEndereco(usuarioCadastroDTO.getEndereco());

      return estrategia.cadastrar(usuarioCadastroDTO, enderecoEncontrado);



    }

}
