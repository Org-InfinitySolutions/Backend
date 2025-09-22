package com.infinitysolutions.applicationservice.old.service;

import com.infinitysolutions.applicationservice.core.domain.valueobject.Email;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoMudancaStatusPedido;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailNotificacaoNovoPedido;
import com.infinitysolutions.applicationservice.core.usecases.email.EnviarEmailPedidoConcluidoUsuario;
import com.infinitysolutions.applicationservice.core.usecases.usuario.BuscarUsuarioPorId;
import com.infinitysolutions.applicationservice.old.infra.exception.OperacaoNaoPermitidaException;
import com.infinitysolutions.applicationservice.infrastructure.mapper.UsuarioEntityMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.EnderecoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.PedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.UsuarioEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.dto.pedido.*;
import com.infinitysolutions.applicationservice.infrastructure.mapper.PedidoMapper;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailNotificacaoMudancaStatus;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailPedidoConcluidoAdmin;
import com.infinitysolutions.applicationservice.core.usecases.email.dto.EmailNotificacaoPedidoConcluido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.SituacaoPedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoPedidoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PedidoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.ProdutoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.UsuarioRepository;
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
    private final ProdutoRepository produtoRepository;
    private final ArquivoMetadadosService arquivoMetadadosService;
    private final FileUploadService fileUploadService;

    private final BuscarUsuarioPorId buscarUsuarioPorId;
    private final UsuarioEntityMapper usuarioMapper;

    private final EnviarEmailPedidoConcluidoUsuario enviarEmailPedidoConcluidoUsuario;
    private final EnviarEmailNotificacaoNovoPedido enviarEmailNotificacaoNovoPedido;
    private final EnviarEmailNotificacaoMudancaStatusPedido enviarEmailNotificacaoMudancaStatusPedido;

    @Transactional
    public PedidoRespostaCadastroDTO cadastrar(PedidoCadastroDTO dto, MultipartFile documentoAuxiliar, UUID usuarioId) {
            UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(usuarioId).orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(usuarioId));

            Map<Integer, PedidoCadastroDTO.ProdutoPedidoDTO> mapaDtos = dto.produtos()
                    .stream().collect(Collectors.toMap(PedidoCadastroDTO.ProdutoPedidoDTO::produtoId, Function.identity()));

            Set<Integer> produtoIds = new HashSet<>(mapaDtos.keySet());

            List<ProdutoEntity> produtoEntities = produtoRepository.findAllById(produtoIds);

            if (produtoEntities.size() != produtoIds.size()) {
                Set<Integer> idsEncontrados = produtoEntities.stream()
                        .map(ProdutoEntity::getId).collect(Collectors.toSet());

                List<Integer> idsNaoEncontrados = produtoIds.stream()
                        .filter(id -> !idsEncontrados.contains(id)).toList();

                throw new RecursoNaoEncontradoException("Produtos não encontrados: " + idsNaoEncontrados);
            }

            EnderecoEntity enderecoEntityEncontrado = null;
//                    enderecoService.buscarEndereco(dto.endereco());

            PedidoEntity pedidoEntityCriado = null;
//                    PedidoMapper.toPedido(dto, usuarioEntity, enderecoEntityEncontrado);

            PedidoEntity pedidoEntitySalvo = pedidoRepository.save(pedidoEntityCriado);

            List<ProdutoPedidoEntity> produtosPedido = produtoEntities.stream()
                    .map(produto -> {
                        PedidoCadastroDTO.ProdutoPedidoDTO dtoCorrespondente = mapaDtos.get(produto.getId());
                        return createProdutoPedido(produto, pedidoEntitySalvo, dtoCorrespondente);
                    }).toList();

            pedidoEntitySalvo.setProdutosPedido(produtosPedido);
            if (documentoAuxiliar != null && !documentoAuxiliar.isEmpty()) {
                enviarDocumentoAuxiliar(documentoAuxiliar, pedidoEntitySalvo);
            }
            
            // Salvar o pedido no banco de dados
            PedidoRespostaCadastroDTO pedidoRespostaCadastroDTO = PedidoMapper.toPedidoRespostaCadastroDTO(pedidoRepository.save(pedidoEntitySalvo));
            
            // Buscar o email do usuário
            String usuarioEmail = "";
            try {
//                usuarioEmail = authServiceConnection.buscarEmailUsuario(usuarioId).email();
                log.info("Email do usuário recuperado com sucesso: {}", usuarioEmail);
            } catch (Exception e) {
                log.error("Erro ao recuperar o email do usuário: {}", e.getMessage());
                // Continua a execução mesmo se não conseguir obter o email
            }
            
            // Enviar email para o usuário
            if (!usuarioEmail.isEmpty()) {
                try {
                    int qtdItens = produtosPedido.size();
                    enviarEmailPedidoConcluidoUsuario.execute(new EmailNotificacaoPedidoConcluido(
                            usuarioEntity.getNome(),
                            pedidoEntitySalvo.getId().toString(),
                            Email.of(usuarioEmail),
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
                    String descricaoPedido = pedidoEntitySalvo.getDescricao() != null && !pedidoEntitySalvo.getDescricao().trim().isEmpty()
                            ? pedidoEntitySalvo.getDescricao()
                            : "Sem descrição";
                    
                    enviarEmailNotificacaoNovoPedido.execute(new EmailPedidoConcluidoAdmin(
                            usuarioEntity.getNome(),
                            pedidoEntitySalvo.getId().toString(),
                            Email.of(usuarioEmail),
                            String.valueOf(qtdItens),
                            usuarioEntity.getTelefoneCelular(),
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

    private void enviarDocumentoAuxiliar(MultipartFile arquivo, PedidoEntity pedidoEntity) {
        long maxSize = 20L * 1024L * 1024L; // 20MB
        if (arquivo.getSize() > maxSize) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 20MB");
        }
        log.info("Documento auxiliar está pronto para ser enviado ao bucket!!!");
        arquivoMetadadosService.uploadAndPersistArquivo(arquivo, TipoAnexo.DOCUMENTO_AUXILIAR, pedidoEntity);
    }

    private static ProdutoPedidoEntity createProdutoPedido(ProdutoEntity produtoEntity, PedidoEntity pedidoEntitySalvo, PedidoCadastroDTO.ProdutoPedidoDTO dtoCorrespondente) {
        ProdutoPedidoEntity produtoPedidoEntity = new ProdutoPedidoEntity();
        produtoPedidoEntity.setPedidoEntity(pedidoEntitySalvo);
        produtoPedidoEntity.setProdutoEntity(produtoEntity);
        produtoPedidoEntity.setQtd(dtoCorrespondente.quantidade());
        return produtoPedidoEntity;
    }

    @Transactional
    public PedidoRespostaDTO atualizarSituacao(Integer pedidoId, PedidoAtualizacaoSituacaoDTO dto, boolean isCustomer) {
        PedidoEntity pedidoEntity = findById(pedidoId);
        var statusAntigo = pedidoEntity.getSituacao();
        if (dto.situacao() == SituacaoPedido.CANCELADO && pedidoEntity.getSituacao() != SituacaoPedido.EM_ANALISE && isCustomer) {
             log.warn("Tentativa do usuário comum de cancelar pedido {} que não está em análise. Situação atual: {}",
                     pedidoId, pedidoEntity.getSituacao());
            throw OperacaoNaoPermitidaException.cancelamentoPedido(pedidoId);
        }        log.info("Atualizando situação do pedido {} de {} para {}",
                pedidoId, pedidoEntity.getSituacao(), dto.situacao());
        
        ajustarEstoque(pedidoEntity, statusAntigo, dto.situacao());
          // Definir a data específica baseada no novo status
        LocalDateTime agora = LocalDateTime.now();
        switch (dto.situacao()) {
            case APROVADO -> pedidoEntity.setDataAprovacao(agora);
            case EM_EVENTO -> pedidoEntity.setDataInicioEvento(agora);
            case FINALIZADO -> pedidoEntity.setDataFinalizacao(agora);
            case CANCELADO -> pedidoEntity.setDataCancelamento(agora);
            case EM_ANALISE -> {
                // Não precisa definir data específica, pois já é o estado inicial
            }
        }
        
        pedidoEntity.setSituacao(dto.situacao());
        PedidoRespostaDTO pedidoRespostaDTO = PedidoMapper.toPedidoRespostaDTO(pedidoRepository.save(pedidoEntity));

        String usuarioEmail = null;
//                authServiceConnection.buscarEmailUsuario(pedidoEntity.getUsuarioEntity().getId()).email();
        
        enviarEmailNotificacaoMudancaStatusPedido.execute(
            new EmailNotificacaoMudancaStatus(
                pedidoEntity.getUsuarioEntity().getNome(),
                pedidoEntity.getId().toString(),
                statusAntigo.getNome(),
                dto.situacao().getNome(),
                Email.of(usuarioEmail)
            )
        );
        
        return pedidoRespostaDTO;
    }

    private void ajustarEstoque(PedidoEntity pedidoEntity, SituacaoPedido statusAntigo, SituacaoPedido novoStatus) {
        log.info("Iniciando ajuste de estoque para pedido {} - Status: {} -> {}", 
                pedidoEntity.getId(), statusAntigo, novoStatus);
        if (statusAntigo == SituacaoPedido.EM_ANALISE && novoStatus == SituacaoPedido.APROVADO) {
            reduzirEstoque(pedidoEntity);
        }
        else if ((novoStatus == SituacaoPedido.FINALIZADO || novoStatus == SituacaoPedido.CANCELADO) && 
                 estoqueJaFoiReduzido(statusAntigo)) {
            devolverEstoque(pedidoEntity);
        }
        
        log.info("Ajuste de estoque concluído para pedido {}", pedidoEntity.getId());
    }

    private boolean estoqueJaFoiReduzido(SituacaoPedido statusAnterior) {
        return statusAnterior == SituacaoPedido.APROVADO || 
               statusAnterior == SituacaoPedido.EM_EVENTO;
    }


    private void reduzirEstoque(PedidoEntity pedidoEntity) {
        log.info("Reduzindo estoque dos produtos do pedido {}", pedidoEntity.getId());
        
        for (ProdutoPedidoEntity produtoPedidoEntity : pedidoEntity.getProdutosPedido()) {
            ProdutoEntity produtoEntity = produtoPedidoEntity.getProdutoEntity();
            int quantidadePedida = produtoPedidoEntity.getQtd();
            int estoqueAtual = produtoEntity.getQtdEstoque();
            
            if (estoqueAtual < quantidadePedida) {
                log.error("Estoque insuficiente para produto {} (ID: {}). Disponível: {}, Solicitado: {}", 
                        produtoEntity.getModelo(), produtoEntity.getId(), estoqueAtual, quantidadePedida);
                throw new OperacaoNaoPermitidaException(
                    String.format("Estoque insuficiente para o produto %s. Disponível: %d, Solicitado: %d",
                            produtoEntity.getModelo(), estoqueAtual, quantidadePedida));
            }
            
            int novoEstoque = estoqueAtual - quantidadePedida;
            produtoEntity.setQtdEstoque(novoEstoque);
            produtoRepository.save(produtoEntity);
            
            log.info("Estoque reduzido para produto {} (ID: {}): {} -> {}", 
                    produtoEntity.getModelo(), produtoEntity.getId(), estoqueAtual, novoEstoque);
        }
    }

    private void devolverEstoque(PedidoEntity pedidoEntity) {
        log.info("Devolvendo estoque dos produtos do pedido {}", pedidoEntity.getId());
        
        for (ProdutoPedidoEntity produtoPedidoEntity : pedidoEntity.getProdutosPedido()) {
            ProdutoEntity produtoEntity = produtoPedidoEntity.getProdutoEntity();
            int quantidadePedida = produtoPedidoEntity.getQtd();
            int estoqueAtual = produtoEntity.getQtdEstoque();
            int novoEstoque = estoqueAtual + quantidadePedida;
            
            produtoEntity.setQtdEstoque(novoEstoque);
            produtoRepository.save(produtoEntity);
            
            log.info("Estoque devolvido para produto {} (ID: {}): {} -> {}", 
                    produtoEntity.getModelo(), produtoEntity.getId(), estoqueAtual, novoEstoque);
        }
    }

    public PedidoEntity findById(Integer id){
        return pedidoRepository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado"));
    }

    public List<PedidoRespostaDTO> listar(UUID id) {
        List<PedidoEntity> pedidoEntities = pedidoRepository.findByUsuarioEntityIdOrderByDataCriacaoDesc(id);
        return PedidoMapper.toPedidoRespostaDTOList(pedidoEntities);
    }

    public List<PedidoRespostaDTO> listarAdmin() {
        List<PedidoEntity> pedidoEntities = pedidoRepository.findAllWithUsuarioEntityOrderByDataCriacaoDesc();
        return PedidoMapper.toPedidoRespostaAdminDTOList(pedidoEntities);
    }

    public PedidoRespostaDetalhadoDTO buscarPorIdAdmin(Integer id) {
        PedidoEntity pedidoEntityEncontrado = pedidoRepository.findWithUsuarioEntityById(id).orElseThrow(() -> RecursoNaoEncontradoException.pedidoNaoEncontrado(id));
        var usuarioEncontrado = usuarioMapper.toUsuarioRespostaDTO(buscarUsuarioPorId.execute(pedidoEntityEncontrado.getUsuarioEntity().getId()));
        List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> urlDocumentos = new ArrayList<>();
        pedidoEntityEncontrado.getDocumentos().forEach(documento -> {
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
        return PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(pedidoEntityEncontrado, usuarioEncontrado, urlDocumentos);
    }

    public PedidoRespostaDetalhadoDTO buscarPorId(Integer id, UUID idUsuario) {
        PedidoEntity pedidoEntityEncontrado = pedidoRepository.findWithUsuarioEntityByIdAndByUsuarioEntityId(id, idUsuario).orElseThrow(() -> RecursoNaoEncontradoException.pedidoNaoEncontradoComUsuario(id, idUsuario));
        var usuarioEncontrado = usuarioMapper.toUsuarioRespostaDTO(buscarUsuarioPorId.execute(idUsuario));
        List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> urlDocumentos = new ArrayList<>();
        pedidoEntityEncontrado.getDocumentos().forEach(documento -> {
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
        return PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(pedidoEntityEncontrado, usuarioEncontrado, urlDocumentos);
    }
}
