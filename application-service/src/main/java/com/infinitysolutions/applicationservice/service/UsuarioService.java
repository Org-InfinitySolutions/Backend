package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.exception.DocumentoNaoEncontradoException;
import com.infinitysolutions.applicationservice.infra.exception.ErroInesperadoException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoIncompativelException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.dto.auth.AuthServiceCadastroRequestDTO;
import com.infinitysolutions.applicationservice.model.dto.auth.RespostaEmail;
import com.infinitysolutions.applicationservice.model.dto.usuario.*;
import com.infinitysolutions.applicationservice.model.dto.validacao.RespostaVerificacao;
import com.infinitysolutions.applicationservice.service.adapter.AuthServiceCredencialAdapter;
import com.infinitysolutions.applicationservice.service.email.EnvioEmailService;
import com.infinitysolutions.applicationservice.service.strategy.AuthServiceConnection;
import com.infinitysolutions.applicationservice.service.strategy.PessoaFisicaImpl;
import com.infinitysolutions.applicationservice.service.strategy.PessoaJuridicaImpl;
import com.infinitysolutions.applicationservice.service.strategy.UsuarioStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SuppressWarnings("unchecked")
@Service
@Slf4j
@RequiredArgsConstructor
public class UsuarioService {


    private final List<UsuarioStrategy> strategies;
    private final EnderecoService enderecoService;
    private final AuthServiceConnection httpAuthServiceConnection;
    private final AuthServiceCredencialAdapter credencialAdapter;
    private final PessoaFisicaImpl pessoaFisica;
    private final PessoaJuridicaImpl pessoaJuridica;
    private final EnvioEmailService envioEmailService;

    @Transactional
    public UsuarioRespostaCadastroDTO cadastrar(UsuarioCadastroDTO usuarioCadastroDTO) {

      log.info("Iniciando processo de cadastro do Usuario");
      Optional<UsuarioStrategy> estrategia = strategies.stream()

              .filter(strategy -> strategy.getTipoDTO().equals(usuarioCadastroDTO.getClass()))
              .findFirst();

      if (estrategia.isEmpty()) {
          log.warn("Estratégia não encontrada para o tipo de usuário: {}", usuarioCadastroDTO.getClass());
          throw RecursoNaoEncontradoException.estrategiaNaoEncontrada(usuarioCadastroDTO.getClass().toString());
      }

      log.info("Estratégia encontrada: {}", estrategia.getClass().getSimpleName());

      log.info("Configurando endereço do Usuario");
      Endereco enderecoEncontrado = enderecoService.buscarEndereco(usuarioCadastroDTO.getEndereco());
      var usuarioCadastrado = estrategia.get().cadastrar(usuarioCadastroDTO, enderecoEncontrado);

      AuthServiceCadastroRequestDTO authServiceCadastroRequestDTO = credencialAdapter.adaptarParaCadastroRequest(usuarioCadastrado.getId(), usuarioCadastroDTO);

      httpAuthServiceConnection.enviarCredenciais(authServiceCadastroRequestDTO);

      // Se chegou até aqui, o cadastro foi bem-sucedido, então podemos enviar os emails
      log.info("Credenciais criadas com sucesso, enviando emails de confirmação");
      
      // Preparar o DTO para os emails
      UsuarioAutenticacaoCadastroDTO dto = new UsuarioAutenticacaoCadastroDTO(usuarioCadastroDTO.getNome(), usuarioCadastroDTO.getEmail());
      
      // Enviar email de cadastro completo para o usuário
      try {
          envioEmailService.enviarCadastroSucessoUsuario(dto);
          log.info("Email de cadastro completo enviado com sucesso para o usuário: {}", dto.email());
      } catch (Exception e) {
          log.error("Erro ao enviar email de cadastro completo para o usuário: {}", e.getMessage());
          // Não interrompe o fluxo se houver erro no envio
      }
      
      // Enviar notificação de novo cadastro para o administrador
      try {
          envioEmailService.enviarNotificacaoNovoCadastro(dto);
          log.info("Email de notificação de novo cadastro enviado para o administrador");
      } catch (Exception e) {
          log.error("Erro ao enviar email de notificação de novo cadastro para o administrador: {}", e.getMessage());
          // Não interrompe o fluxo se houver erro no envio
      }
      
      return usuarioCadastrado;
    }

    public List<UsuarioRespostaCadastroDTO> listarTodos() {
        log.info("Listando todos os usuários");
        List<UsuarioRespostaCadastroDTO> usuariosTotais = new ArrayList<>();
        for (UsuarioStrategy<?, ?, ?, ?> strategy : strategies) {
            List<? extends UsuarioRespostaCadastroDTO> usuariosDaVez = strategy.listarTodos();
            usuariosTotais.addAll(usuariosDaVez);
        }
        log.info("Total de usuários encontrados: {}", usuariosTotais.size());
        return usuariosTotais;
    }

    public UsuarioRespostaDTO buscarPorId(UUID usuarioId) {
        UsuarioRespostaDTO usuarioEncontrado = null;
        
        // Busca o usuário usando as estratégias disponíveis
        for (UsuarioStrategy<?, ?, ?, ?> strategy : strategies) {
            try {
                usuarioEncontrado = strategy.buscarPorId(usuarioId);
                log.info("Usuário encontrado pela estratégia: {}", strategy.getClass().getSimpleName());
                break;
            } catch (RecursoNaoEncontradoException e) {
                log.debug("Estratégia {} não encontrou o usuário com ID: {}", strategy.getClass().getSimpleName(), usuarioId);
            } catch (DocumentoNaoEncontradoException e) {
                log.error(e.getMessage());
                throw e;
            } catch (Exception e) {
                log.error("Erro inesperado ao buscar usuário com ID {} usando a estratégia {}: {}", usuarioId, strategy.getClass().getSimpleName(), e.getMessage());
            }
        }
        
        if (usuarioEncontrado == null) {
            log.warn("Nenhuma estratégia encontrou o usuário com ID: {}", usuarioId);
            throw RecursoNaoEncontradoException.usuarioNaoEncontrado(usuarioId);
        }
        
        // Busca o email do usuário no serviço de autenticação
        try {
            RespostaEmail respostaEmail = httpAuthServiceConnection.buscarEmailUsuario(usuarioId);
            if (respostaEmail != null && respostaEmail.email() != null) {
                // Define o email no DTO de resposta
                usuarioEncontrado.setEmail(respostaEmail.email());
                log.info("Email do usuário ID {} adicionado à resposta", usuarioId);
            }
        } catch (Exception e) {
            log.warn("Não foi possível obter o email do usuário ID {}: {}", usuarioId, e.getMessage());
            // Continua sem o email, não bloqueia a resposta
        }
        
        return usuarioEncontrado;
    }

    @Transactional
    public UsuarioRespostaCadastroDTO atualizar(UUID usuarioId, UsuarioAtualizacaoDTO usuarioAtualizacaoDTO) {
        log.info("Iniciando processo de atualização do Usuario com ID: {}", usuarioId);
        for (UsuarioStrategy strategy : strategies) {
            try {
                strategy.buscarPorId(usuarioId);
                log.info("Usuário encontrado pela estratégia: {}. Verificando compatibilidade do DTO.", strategy.getClass().getSimpleName());
                Class<?> tipoAtualizacaoDTO = strategy.getTipoAtualizacaoDTO();
                if (!tipoAtualizacaoDTO.isInstance(usuarioAtualizacaoDTO)) {
                    log.warn("Tentativa de atualizar usuário ID {} (tipo {}) com DTO incompatível (tipo {}).",
                            usuarioId, strategy.getClass().getSimpleName(), usuarioAtualizacaoDTO.getClass().getSimpleName());
                    throw RecursoIncompativelException.recursoIncompativel(
                            String.format("O tipo de dados de atualização (%s) não é compatível com o tipo deste usuário.",
                                    usuarioAtualizacaoDTO.getClass().getSimpleName()));
                }
                log.info("DTO compatível. Iniciando atualização com a estratégia {}.", strategy.getClass().getSimpleName());
                @SuppressWarnings("unchecked")
                UsuarioRespostaCadastroDTO resultado = strategy.atualizar(usuarioAtualizacaoDTO, usuarioId);
                log.info("Usuário com ID {} atualizado com sucesso pela estratégia {}.", usuarioId, strategy.getClass().getSimpleName());
                return resultado;
            } catch (RecursoNaoEncontradoException e) {
                log.debug("Estratégia {} não encontrou o usuário com ID {} para atualização.", strategy.getClass().getSimpleName(), usuarioId);
            } catch (RecursoIncompativelException rie) {
                throw rie;
            } catch (Exception e) {
                log.error("Erro inesperado ao tentar atualizar usuário com ID {} usando a estratégia {}: {}", usuarioId, strategy.getClass().getSimpleName(), e.getMessage(), e);
                throw ErroInesperadoException.erroInesperado("Erro inesperado durante a atualização do usuário " + usuarioId, e.getMessage());
            }
        }
        log.warn("Nenhuma estratégia encontrou o usuário com ID {} para atualização.", usuarioId);
        throw RecursoNaoEncontradoException.usuarioNaoEncontrado(usuarioId);
    }

    public RespostaVerificacao verificarCpf(String cpf){
        boolean cpfDisponivel = !pessoaFisica.verificarCpf(cpf);
        return new RespostaVerificacao(cpf, cpfDisponivel);

    }

    public RespostaVerificacao verificarCnpj(String cnpj){
        boolean cnpjDisponivel = !pessoaJuridica.verificarCnpj(cnpj);
        return new RespostaVerificacao(cnpj, cnpjDisponivel);

    }

    public RespostaVerificacao verificarRg(String rg) {
        boolean rgDisponivel = !pessoaFisica.verificarRg(rg);
        return new RespostaVerificacao(rg, rgDisponivel);
    }
}
