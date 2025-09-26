package com.infinitysolutions.applicationservice.infrastructure.gateway;

import com.infinitysolutions.applicationservice.core.domain.ArquivoMetadado;
import com.infinitysolutions.applicationservice.core.domain.pedido.Pedido;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoAnexo;
import com.infinitysolutions.applicationservice.core.domain.valueobject.TipoUsuario;
import com.infinitysolutions.applicationservice.core.exception.RecursoNaoEncontradoException;
import com.infinitysolutions.applicationservice.core.gateway.ArquivoMetadadoGateway;
import com.infinitysolutions.applicationservice.infrastructure.mapper.ArquivoMetadadosMapper;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.*;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.entity.produto.ProdutoEntity;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.ArquivoMetadadosRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.PedidoRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.UsuarioRepository;
import com.infinitysolutions.applicationservice.infrastructure.persistence.jpa.repository.produto.ProdutoRepository;
import com.infinitysolutions.applicationservice.infrastructure.exception.DocumentoInvalidoException;
import com.infinitysolutions.applicationservice.infrastructure.service.S3ArquivoMetadadosService;
import com.infinitysolutions.applicationservice.infrastructure.service.S3FileUploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ArquivoMetadadoGatewayImpl implements ArquivoMetadadoGateway {

    private final S3ArquivoMetadadosService s3ArquivoMetadadosService;
    private final UsuarioRepository usuarioRepository;
    private final ArquivoMetadadosRepository arquivoMetadadosRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;
    private final S3FileUploadService s3FileUploadService;

    @Override
    public List<ArquivoMetadado> findAllByUsuarioId(UUID id) {
        return List.of();
    }

    @Override
    public List<ArquivoMetadado> findAllByProdutoId(Integer produtoId) {
        ProdutoEntity entity = produtoRepository.findById(produtoId).orElseThrow(() -> RecursoNaoEncontradoException.produtoNaoEncontrado(produtoId));
        return arquivoMetadadosRepository.findByProduto(entity).stream().map(ArquivoMetadadosMapper::toDomain).toList();
    }

    @Override
    public void deleteAllByProdutoId(Integer produtoId) {
        ProdutoEntity entity = produtoRepository.findById(produtoId).orElseThrow(() -> RecursoNaoEncontradoException.produtoNaoEncontrado(produtoId));
        List<ArquivoMetadadosEntity> entities = arquivoMetadadosRepository.findByProduto(entity);
        for (ArquivoMetadadosEntity imagemAntiga : entities) {
            try {
                s3FileUploadService.deletarArquivo(imagemAntiga.getBlobUrl());
                arquivoMetadadosRepository.delete(imagemAntiga);
                log.info("Arquivo excluído do S3: {}", imagemAntiga.getBlobName());
            } catch (Exception e) {
                log.error("Erro ao excluir arquivo do S3: {}, erro: {}", imagemAntiga.getBlobName(), e.getMessage());
            }
        }
    }

    @Override
    public ArquivoMetadado enviarArquivoUsuario(MultipartFile documento, TipoAnexo tipoAnexo, UUID idUsuario) {

        UsuarioEntity usuarioEntity = usuarioRepository.findByIdAndIsAtivoTrue(idUsuario)
                .orElseThrow(() -> RecursoNaoEncontradoException.usuarioNaoEncontrado(idUsuario));
        if (usuarioEntity instanceof PessoaFisicaEntity pessoaFisicaEntity) {
            if (!tipoAnexo.equals(TipoAnexo.COPIA_RG) && !tipoAnexo.equals(TipoAnexo.COMPROVANTE_ENDERECO)) {
                throw DocumentoInvalidoException.tipoAnexoInvalidoPorUsuario(tipoAnexo, TipoUsuario.PF.name());
            }

            ArquivoMetadadosEntity savedEntity = s3ArquivoMetadadosService.uploadAndPersistArquivo(documento, tipoAnexo, pessoaFisicaEntity);
            return ArquivoMetadadosMapper.toDomain(savedEntity);
        } else if (usuarioEntity instanceof PessoaJuridicaEntity pessoaJuridicaEntity) {
            if (!tipoAnexo.equals(TipoAnexo.COPIA_CNPJ) && !tipoAnexo.equals(TipoAnexo.COPIA_CONTRATO_SOCIAL) && !tipoAnexo.equals(TipoAnexo.COMPROVANTE_ENDERECO)) {
                throw DocumentoInvalidoException.tipoAnexoInvalidoPorUsuario(tipoAnexo, TipoUsuario.PJ.name());
            }
            ArquivoMetadadosEntity savedEntity = s3ArquivoMetadadosService.uploadAndPersistArquivo(documento, tipoAnexo, pessoaJuridicaEntity);
            return ArquivoMetadadosMapper.toDomain(savedEntity);
        }
        throw new RecursoNaoEncontradoException("Tipo de usuário não encontrado");
    }

    @Override
    public ArquivoMetadado enviarArquivoProduto(MultipartFile documento, TipoAnexo tipoAnexo, Integer produtoId) {
        ProdutoEntity produtoEntity = produtoRepository.findByIdAndIsAtivoTrue(produtoId).orElseThrow(() -> RecursoNaoEncontradoException.produtoNaoEncontrado(produtoId));
        ArquivoMetadadosEntity arquivoMetadadosEntity = s3ArquivoMetadadosService.uploadAndPersistArquivo(documento, tipoAnexo, produtoEntity);
        return ArquivoMetadadosMapper.toDomain(arquivoMetadadosEntity);
    }

    @Override
    public ArquivoMetadado enviarDocumentoAuxiliar(MultipartFile documento, Pedido pedido) {
        PedidoEntity pedidoEntity = pedidoRepository.findById(pedido.getId()).orElseThrow(() -> RecursoNaoEncontradoException.pedidoNaoEncontrado(pedido.getId()));
        ArquivoMetadadosEntity arquivoMetadadosEntity = s3ArquivoMetadadosService.uploadAndPersistArquivo(documento, TipoAnexo.DOCUMENTO_AUXILIAR, pedidoEntity);
        return ArquivoMetadadosMapper.toDomain(arquivoMetadadosEntity);
    }


}
