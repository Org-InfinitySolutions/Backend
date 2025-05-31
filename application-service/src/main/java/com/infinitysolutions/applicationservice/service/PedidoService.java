package com.infinitysolutions.applicationservice.service;

import com.infinitysolutions.applicationservice.infra.exception.OperacaoNaoPermitidaException;
import com.infinitysolutions.applicationservice.infra.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.mapper.PedidoMapper;
import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.pedido.*;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import com.infinitysolutions.applicationservice.model.enums.TipoAnexo;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;
import com.infinitysolutions.applicationservice.repository.PedidoRepository;
import com.infinitysolutions.applicationservice.repository.produto.ProdutoRepository;
import com.infinitysolutions.applicationservice.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

            return PedidoMapper.toPedidoRespostaCadastroDTO(pedidoRepository.save(pedidoSalvo));
    }

    private void enviarDocumentoAuxiliar(MultipartFile arquivo, Pedido pedido) {
        long maxSize = 20L * 1024L * 1024L; // 20MB
        if (arquivo.getSize() > maxSize) {
            throw new IllegalArgumentException("Arquivo muito grande. Tamanho máximo: 20MB");
        }
        log.info("Documento auxiliar está pronto para ser enviado ao bucket!!!");
        arquivoMetadadosService.uploadAndPersistArquivo(arquivo, TipoAnexo.DOCUMENTO_AUXILIAR, pedido);
    }

    private static ProdutoPedido createProdutoPedido(Produto produto, Pedido pedidoSalvo, PedidoCadastroDTO.ProdutoPedidoDTO dtoCorrespondente) {
        ProdutoPedido produtoPedido = new ProdutoPedido();
        produtoPedido.setPedido(pedidoSalvo);
        produtoPedido.setProduto(produto);
        produtoPedido.setQtd(dtoCorrespondente.quantidade());
        return produtoPedido;
    }

    @Transactional
    public PedidoRespostaDTO atualizarSituacao(Integer pedidoId, PedidoAtualizacaoSituacaoDTO dto) {
        Pedido pedido = findById(pedidoId);
        if (dto.situacao() == SituacaoPedido.CANCELADO && pedido.getSituacao() != SituacaoPedido.EM_ANALISE) {
             log.warn("Tentativa de cancelar pedido {} que não está em análise. Situação atual: {}",
                     pedidoId, pedido.getSituacao());
            throw OperacaoNaoPermitidaException.cancelamentoPedido(pedidoId);
        }

        log.info("Atualizando situação do pedido {} de {} para {}",
                pedidoId, pedido.getSituacao(), dto.situacao());
        pedido.setSituacao(dto.situacao());
        return PedidoMapper.toPedidoRespostaDTO(pedidoRepository.save(pedido));
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
