package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.exception.OperacaoNaoPermitidaException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.mapper.PedidoMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.email.EmailNotificacaoMudancaStatusDTO;
import com.infinitysolutions.applicationservice.model.dto.email.EmailNotificacaoPedidoConcluidoAdminDTO;
import com.infinitysolutions.applicationservice.model.dto.email.EmailNotificacaoPedidoConcluidoDTO;
import com.infinitysolutions.applicationservice.model.dto.pedido.*;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;
import com.infinitysolutions.applicationservice.repository.PedidoRepository;
import com.infinitysolutions.applicationservice.repository.produto.ProdutoRepository;
import com.infinitysolutions.applicationservice.repository.UsuarioRepository;
import com.infinitysolutions.applicationservice.service.email.EnvioEmailService;
import com.infinitysolutions.applicationservice.service.strategy.AuthServiceConnection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final ProdutoRepository produtoRepository;
    private final EnderecoService enderecoService;
    private final ArquivoMetadadosService arquivoMetadadosService;
    private final FileUploadService fileUploadService;
    private final EnvioEmailService envioEmailService;
    private final AuthServiceConnection authServiceConnection;
    
    @Transactional
    public PedidoRespostaCadastroDTO cadastrar(PedidoCadastroDTO dto, MultipartFile documentoAuxiliar, UUID usuarioId) {
            Usuario usuario = usuarioRepository.findByIdAndIsAtivoTrue(usuarioId).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(usuarioId));

            Map<Integer, PedidoCadastroDTO.ProdutoPedidoDTO> mapaDtos = dto.produtos()
                    .stream().collect(Collectors.toMap(PedidoCadastroDTO.ProdutoPedidoDTO::produtoId, Function.identity()));

            Set<Integer> produtoIds = new HashSet<>(mapaDtos.keySet());

            List<Produto> produtos = produtoRepository.findAllById(produtoIds);

            if (produtos.size() != produtoIds.size()) {
                Set<Integer> idsEncontrados = produtos.stream()
                        .map(Produto::getId).collect(Collectors.toSet());

                List<Integer> idsNaoEncontrados = produtoIds.stream()
                        .filter(id -> !idsEncontrados.contains(id)).toList();

                throw new RecursoNaoEncontradoException("Produtos não encontrados: " + idsNaoEncontrados);
            }

            Endereco enderecoEncontrado = enderecoService.buscarEndereco(dto.endereco());

            Pedido pedidoCriado = PedidoMapper.toPedido(dto, usuario, enderecoEncontrado);

            Pedido pedidoSalvo = pedidoRepository.save(pedidoCriado);
            List<ProdutoPedido> produtosPedido = produtos.stream()
                    .map(produto -> {
                        PedidoCadastroDTO.ProdutoPedidoDTO dtoCorrespondente = mapaDtos.get(produto.getId());
                        return createProdutoPedido(produto, pedidoSalvo, dtoCorrespondente);
                    }).collect(Collectors.toList());

            pedidoSalvo.setProdutosPedido(produtosPedido);
            if (documentoAuxiliar != null && !documentoAuxiliar.isEmpty()) {
                enviarDocumentoAuxiliar(documentoAuxiliar, pedidoSalvo);
            }
            
            // Salvar o pedido no banco de dados
            PedidoRespostaCadastroDTO pedidoRespostaCadastroDTO = PedidoMapper.toPedidoRespostaCadastroDTO(pedidoRepository.save(pedidoSalvo));
            
            // Buscar o email do usuário
            String usuarioEmail = "";
            try {
                usuarioEmail = authServiceConnection.buscarEmailUsuario(usuarioId).email();
                log.info("Email do usuário recuperado com sucesso: {}", usuarioEmail);
            } catch (Exception e) {
                log.error("Erro ao recuperar o email do usuário: {}", e.getMessage());
                // Continua a execução mesmo se não conseguir obter o email
            }
            
            // Enviar email para o usuário
            if (!usuarioEmail.isEmpty()) {
                try {
                    int qtdItens = produtosPedido.size();
                    envioEmailService.enviarPedidoConcluidoUsuario(new EmailNotificacaoPedidoConcluidoDTO(
                            usuario.getNome(),
                            pedidoSalvo.getId().toString(),
                            usuarioEmail,
                            String.valueOf(qtdItens)
                    ));
                    log.info("Email de confirmação de pedido enviado para o usuário: {}", usuarioEmail);
                } catch (Exception e) {
                    log.error("Erro ao enviar email de confirmação para o usuário: {}", e.getMessage());
                    // Continua a execução mesmo se não conseguir enviar o email
                }                // Enviar email para o administrador
                try {
                    int qtdItens = produtosPedido.size();
                    
                    // Usa a descrição do próprio pedido
                    String descricaoPedido = pedidoSalvo.getDescricao() != null && !pedidoSalvo.getDescricao().trim().isEmpty() 
                            ? pedidoSalvo.getDescricao() 
                            : "Sem descrição";
                    
                    envioEmailService.enviarNotificacaoNovoPedido(new EmailNotificacaoPedidoConcluidoAdminDTO(
                            usuario.getNome(),
                            pedidoSalvo.getId().toString(),
                            usuarioEmail,
                            String.valueOf(qtdItens),
                            usuario.getTelefoneCelular(),
                            descricaoPedido
                    ));
                    log.info("Email de notificação de novo pedido enviado para o administrador");
                } catch (Exception e) {
                    log.error("Erro ao enviar email de notificação para o administrador: {}", e.getMessage());
                    // Continua a execução mesmo se não conseguir enviar o email
                }
            }

            return pedidoRespostaCadastroDTO;
    }

    private void enviarDocumentoAuxiliar(MultipartFile arquivo, Pedido pedido) {
        long maxSize = 20L * 1024L * 1024L; // 20MB
        if (arquivo.getSize() > maxSize) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 20MB");
        }
        log.info("Documento auxiliar está pronto para ser enviado ao bucket!!!");
        arquivoMetadadosService.uploadAndPersistArquivo(arquivo, TipoAnexo.DOCUMENTO_AUXILIAR, pedido);
    }    private static ProdutoPedido createProdutoPedido(Produto produto, Pedido pedidoSalvo, PedidoCadastroDTO.ProdutoPedidoDTO dtoCorrespondente) {
        ProdutoPedido produtoPedido = new ProdutoPedido();
        produtoPedido.setPedido(pedidoSalvo);
        produtoPedido.setProduto(produto);
        produtoPedido.setQtd(dtoCorrespondente.quantidade());
        return produtoPedido;
    }
      @Transactional
    public PedidoRespostaDTO atualizarSituacao(Integer pedidoId, PedidoAtualizacaoSituacaoDTO dto, boolean isCustomer) {
        Pedido pedido = findById(pedidoId);
        var statusAntigo = pedido.getSituacao();
        if (dto.situacao() == SituacaoPedido.CANCELADO && pedido.getSituacao() != SituacaoPedido.EM_ANALISE && isCustomer) {
             log.warn("Tentativa do usuário comum de cancelar pedido {} que não está em análise. Situação atual: {}",
                     pedidoId, pedido.getSituacao());
            throw OperacaoNaoPermitidaException.cancelamentoPedido(pedidoId);
        }        log.info("Atualizando situação do pedido {} de {} para {}",
                pedidoId, pedido.getSituacao(), dto.situacao());
        
        ajustarEstoque(pedido, statusAntigo, dto.situacao());
          // Definir a data específica baseada no novo status
        LocalDateTime agora = LocalDateTime.now();
        switch (dto.situacao()) {
            case APROVADO -> pedido.setDataAprovacao(agora);
            case EM_EVENTO -> pedido.setDataInicioEvento(agora);
            case FINALIZADO -> pedido.setDataFinalizacao(agora);
            case CANCELADO -> pedido.setDataCancelamento(agora);
            case EM_ANALISE -> {
                // Não precisa definir data específica, pois já é o estado inicial
            }
        }
        
        pedido.setSituacao(dto.situacao());
        PedidoRespostaDTO pedidoRespostaDTO = PedidoMapper.toPedidoRespostaDTO(pedidoRepository.save(pedido));

        String usuarioEmail = authServiceConnection.buscarEmailUsuario(pedido.getUsuario().getId()).email();
        
        envioEmailService.enviarNotificacaoMudancaStatusPedido(
            new EmailNotificacaoMudancaStatusDTO(
                pedido.getUsuario().getNome(), 
                pedido.getId().toString(), 
                statusAntigo.getNome(),
                dto.situacao().getNome(),
                usuarioEmail
            )
        );
        
        return pedidoRespostaDTO;
    }

    private void ajustarEstoque(Pedido pedido, SituacaoPedido statusAntigo, SituacaoPedido novoStatus) {
        log.info("Iniciando ajuste de estoque para pedido {} - Status: {} -> {}", 
                pedido.getId(), statusAntigo, novoStatus);        
        if (statusAntigo == SituacaoPedido.EM_ANALISE && novoStatus == SituacaoPedido.APROVADO) {
            reduzirEstoque(pedido);
        }
        else if ((novoStatus == SituacaoPedido.FINALIZADO || novoStatus == SituacaoPedido.CANCELADO) && 
                 estoqueJaFoiReduzido(statusAntigo)) {
            devolverEstoque(pedido);
        }
        
        log.info("Ajuste de estoque concluído para pedido {}", pedido.getId());
    }

    private boolean estoqueJaFoiReduzido(SituacaoPedido statusAnterior) {
        return statusAnterior == SituacaoPedido.APROVADO || 
               statusAnterior == SituacaoPedido.EM_EVENTO;
    }


    private void reduzirEstoque(Pedido pedido) {
        log.info("Reduzindo estoque dos produtos do pedido {}", pedido.getId());
        
        for (ProdutoPedido produtoPedido : pedido.getProdutosPedido()) {
            Produto produto = produtoPedido.getProduto();
            int quantidadePedida = produtoPedido.getQtd();
            int estoqueAtual = produto.getQtdEstoque();
            
            if (estoqueAtual < quantidadePedida) {
                log.error("Estoque insuficiente para produto {} (ID: {}). Disponível: {}, Solicitado: {}", 
                        produto.getModelo(), produto.getId(), estoqueAtual, quantidadePedida);
                throw new OperacaoNaoPermitidaException(
                    String.format("Estoque insuficiente para o produto %s. Disponível: %d, Solicitado: %d",
                            produto.getModelo(), estoqueAtual, quantidadePedida));
            }
            
            int novoEstoque = estoqueAtual - quantidadePedida;
            produto.setQtdEstoque(novoEstoque);
            produtoRepository.save(produto);
            
            log.info("Estoque reduzido para produto {} (ID: {}): {} -> {}", 
                    produto.getModelo(), produto.getId(), estoqueAtual, novoEstoque);
        }
    }
    private void devolverEstoque(Pedido pedido) {
        log.info("Devolvendo estoque dos produtos do pedido {}", pedido.getId());
        
        for (ProdutoPedido produtoPedido : pedido.getProdutosPedido()) {
            Produto produto = produtoPedido.getProduto();
            int quantidadePedida = produtoPedido.getQtd();
            int estoqueAtual = produto.getQtdEstoque();
            int novoEstoque = estoqueAtual + quantidadePedida;
            
            produto.setQtdEstoque(novoEstoque);
            produtoRepository.save(produto);
            
            log.info("Estoque devolvido para produto {} (ID: {}): {} -> {}", 
                    produto.getModelo(), produto.getId(), estoqueAtual, novoEstoque);
        }
    }

    public Pedido findById(Integer id){
        return pedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    public List<PedidoRespostaDTO> listar(UUID id) {
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdOrderByDataCriacaoDesc(id);
        return PedidoMapper.toPedidoRespostaDTOList(pedidos);
    }

    public List<PedidoRespostaDTO> listarAdmin() {
        List<Pedido> pedidos = pedidoRepository.findAllWithUsuarioOrderByDataCriacaoDesc();
        return PedidoMapper.toPedidoRespostaAdminDTOList(pedidos);
    }

    public PedidoRespostaDetalhadoDTO buscarPorIdAdmin(Integer id) {
        Pedido pedidoEncontrado = pedidoRepository.findWithUsuarioById(id).orElseThrow(() -> RecursoNaoEncontradoException.pedidoNaoEncontrado(id));
        var usuarioEncontrado = usuarioService.buscarPorId(pedidoEncontrado.getUsuario().getId());
        List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> urlDocumentos = new ArrayList<>();
        pedidoEncontrado.getDocumentos().forEach(documento -> {
            String blobName = documento.getBlobName();
            String originalFilename = documento.getOriginalFilename();
            String mimeType = documento.getMimeType();
            urlDocumentos.add(
                    new PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO(
                            originalFilename,
                            fileUploadService.generatePrivateFileSasUrl(blobName, 60),
                            mimeType
                    )
            );
        });
        return PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(pedidoEncontrado, usuarioEncontrado, urlDocumentos);
    }

    public PedidoRespostaDetalhadoDTO buscarPorId(Integer id, UUID idUsuario) {
        Pedido pedidoEncontrado = pedidoRepository.findWithUsuarioByIdAndByUsuarioId(id, idUsuario).orElseThrow(() -> RecursoNaoEncontradoException.pedidoNaoEncontradoComUsuario(id, idUsuario));
        var usuarioEncontrado = usuarioService.buscarPorId(idUsuario);
        List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> urlDocumentos = new ArrayList<>();
        pedidoEncontrado.getDocumentos().forEach(documento -> {
            String originalFilename = documento.getOriginalFilename();
            String blobName = documento.getBlobName();
            String mimeType = documento.getMimeType();
            urlDocumentos.add(
                    new PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO(
                            originalFilename,
                            fileUploadService.generatePrivateFileSasUrl(blobName, 60),
                            mimeType
                    )
            );
        });
        return PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(pedidoEncontrado, usuarioEncontrado, urlDocumentos);
    }
}
