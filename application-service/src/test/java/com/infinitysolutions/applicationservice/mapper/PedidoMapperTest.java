package com.infinitysolutions.applicationservice.mapper;

import com.infinitysolutions.applicationservice.model.Endereco;
import com.infinitysolutions.applicationservice.model.Pedido;
import com.infinitysolutions.applicationservice.model.Usuario;
import com.infinitysolutions.applicationservice.model.dto.endereco.EnderecoResumidoDTO;
import com.infinitysolutions.applicationservice.model.dto.pedido.*;
import com.infinitysolutions.applicationservice.model.dto.usuario.UsuarioRespostaDTO;
import com.infinitysolutions.applicationservice.model.enums.SituacaoPedido;
import com.infinitysolutions.applicationservice.model.enums.TipoPedido;
import com.infinitysolutions.applicationservice.model.produto.Categoria;
import com.infinitysolutions.applicationservice.model.produto.Produto;
import com.infinitysolutions.applicationservice.model.produto.ProdutoPedido;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PedidoMapperTest {

    private Pedido pedido;
    private ProdutoPedido produtoPedido;
    private Produto produto;
    private Categoria categoria;
    private PedidoCadastroDTO pedidoCadastroDTO;
    private Usuario usuario;
    private Endereco endereco;
    private UsuarioRespostaDTO usuarioRespostaDTO;
    private List<PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO> documentos;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        now = LocalDateTime.now();
        
        // Configuração do usuário
        usuario = new Usuario("João da Silva", "11999998888");
        usuario.setId(UUID.randomUUID());
        
        // Configuração do endereço
        endereco = new Endereco(
                "12345-678",
                "Rua Exemplo",
                "Bairro Teste",
                "Cidade Teste",
                "SP",
                "123",
                "Apto 45"
        );
        endereco.setId(1);
        
        // Configuração do PedidoCadastroDTO
        List<PedidoCadastroDTO.ProdutoPedidoDTO> produtos = new ArrayList<>();
        produtos.add(new PedidoCadastroDTO.ProdutoPedidoDTO(1, 2));
        
        pedidoCadastroDTO = new PedidoCadastroDTO(
                produtos,
                null, // enderecoDTO não é utilizado diretamente no mapper
                TipoPedido.INDOOR,
                now.plusDays(1),
                now.plusDays(2),
                "Pedido para evento corporativo"
        );
        
        // Configuração do pedido
        pedido = new Pedido(pedidoCadastroDTO, usuario, endereco);
        pedido.setId(1);
        pedido.setSituacao(SituacaoPedido.EM_ANALISE);
        pedido.setDataCriacao(now);

        categoria = new Categoria();
        categoria.setId(5);
        categoria.setNome("categoria");
        categoria.setAtivo(true);
        categoria.setProdutos(new ArrayList<>());


        produto = new Produto();
        produto.setId(11);
        produto.setModelo("modelo");
        produto.setMarca("marca");
        produto.setUrlFrabricante("url");
        produto.setImagens(new ArrayList<>());
        produto.setDescricao("descricao");
        produto.setQtdEstoque(12);
        produto.setAtivo(true);
        produto.setCategoria(categoria);


        produtoPedido = new ProdutoPedido();
        produtoPedido.setId(1);
        produtoPedido.setProduto(produto);
        produtoPedido.setPedido(pedido);
        produtoPedido.setQtd(12);

        // Configuração dos produtos do pedido
        List<ProdutoPedido> produtosPedido = new ArrayList<>();
        produtosPedido.add(produtoPedido);
        pedido.setProdutosPedido(produtosPedido);
        
        // Configuração do UsuarioRespostaDTO
        usuarioRespostaDTO = Mockito.mock(UsuarioRespostaDTO.class);
        when(usuarioRespostaDTO.getId()).thenReturn(usuario.getId());
        when(usuarioRespostaDTO.getNome()).thenReturn(usuario.getNome());
        
        // Configuração dos documentos
        documentos = new ArrayList<>();
        documentos.add(new PedidoRespostaDetalhadoDTO.DocumentoPedidoDTO(
                "documento.pdf",
                "http://example.com/documentos/123",
                "application/pdf"
        ));
    }

    @Test
    @DisplayName("Deve converter um PedidoCadastroDTO para Pedido com sucesso")
    void toPedido_deveConverterParaPedido() {
        // Act
        Pedido resultado = PedidoMapper.toPedido(pedidoCadastroDTO, usuario, endereco);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(pedidoCadastroDTO.tipo(), resultado.getTipo());
        assertEquals(pedidoCadastroDTO.dataEntrega(), resultado.getDataEntrega());
        assertEquals(pedidoCadastroDTO.dataRetirada(), resultado.getDataRetirada());
        assertEquals(pedidoCadastroDTO.descricao(), resultado.getDescricao());
        assertEquals(usuario, resultado.getUsuario());
        assertEquals(endereco, resultado.getEndereco());
    }

    @Test
    @DisplayName("Deve converter um Pedido para PedidoRespostaCadastroDTO com sucesso")
    void toPedidoRespostaCadastroDTO_deveConverterComSucesso() {
        // Act
        PedidoRespostaCadastroDTO resultado = PedidoMapper.toPedidoRespostaCadastroDTO(pedido);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        assertEquals(pedido.getUsuario().getId(), resultado.getUsuarioId());
        assertEquals(pedido.getUsuario().getNome(), resultado.getNomeUsuario());
        assertEquals(pedido.getQtdItens(), resultado.getQtdItens());
        assertEquals(pedido.getSituacao(), resultado.getSituacao());
        assertEquals(pedido.getTipo(), resultado.getTipo());
        assertEquals(pedido.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(pedido.getDescricao(), resultado.getDescricao());
    }

    @Test
    @DisplayName("Deve converter um Pedido para PedidoRespostaDTO com sucesso")
    void toPedidoRespostaDTO_deveConverterComSucesso() {
        // Act
        PedidoRespostaDTO resultado = PedidoMapper.toPedidoRespostaDTO(pedido);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        assertEquals(pedido.getQtdItens(), resultado.getQtdItens());
        assertEquals(pedido.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(pedido.getDataEntrega(), resultado.getDataEntrega());
        assertEquals(pedido.getSituacao(), resultado.getSituacao());
    }

    @Test
    @DisplayName("Deve converter um Pedido para PedidoRespostaAdminDTO com sucesso")
    void toPedidoRespostaAdminDTO_deveConverterComSucesso() {
        // Act
        PedidoRespostaAdminDTO resultado = PedidoMapper.toPedidoRespostaAdminDTO(pedido);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        assertEquals(pedido.getQtdItens(), resultado.getQtdItens());
        assertEquals(pedido.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(pedido.getDataEntrega(), resultado.getDataEntrega());
        assertEquals(pedido.getUsuario().getNome(), resultado.getNome());
        assertEquals(pedido.getSituacao(), resultado.getSituacao());
    }

    @Test
    @DisplayName("Deve converter um Pedido para PedidoRespostaDetalhadoDTO com sucesso")
    void toPedidoRespostaDetalhadoAdminDTO_deveConverterComSucesso() {
        // Act
        PedidoRespostaDetalhadoDTO resultado = PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(pedido, usuarioRespostaDTO, documentos);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(pedido.getId(), resultado.getId());
        assertEquals(usuarioRespostaDTO, resultado.getUsuario());
        assertNotNull(resultado.getProdutos());
        assertEquals(1, resultado.getProdutos().size());
        
        // Verifica o endereço resumido
        EnderecoResumidoDTO enderecoDTO = resultado.getEndereco();
        assertNotNull(enderecoDTO);
        assertEquals(endereco.getCep(), enderecoDTO.cep());
        assertEquals(endereco.getLogradouro(), enderecoDTO.logradouro());
        assertEquals(endereco.getNumero(), enderecoDTO.numero());
        assertEquals(endereco.getCidade(), enderecoDTO.cidade());
        assertEquals(endereco.getEstado(), enderecoDTO.estado());
        
        assertEquals(pedido.getQtdItens(), resultado.getQtdItens());
        assertEquals(pedido.getDataCriacao(), resultado.getDataCriacao());
        assertEquals(pedido.getDataEntrega(), resultado.getDataEntrega());
        assertEquals(pedido.getDataRetirada(), resultado.getDataRetirada());
        assertEquals(pedido.getSituacao(), resultado.getSituacao());
        assertEquals(documentos, resultado.getDocumentos());
        assertEquals(pedido.getTipo(), resultado.getTipoPedido());
    }

    @Test
    @DisplayName("Deve converter uma lista de Pedidos para lista de PedidoRespostaDTO com sucesso")
    void toPedidoRespostaDTOList_deveConverterComSucesso() {
        // Arrange
        List<Pedido> pedidos = Collections.singletonList(pedido);
        
        // Act
        List<PedidoRespostaDTO> resultado = PedidoMapper.toPedidoRespostaDTOList(pedidos);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        PedidoRespostaDTO dto = resultado.get(0);
        assertEquals(pedido.getId(), dto.getId());
        assertEquals(pedido.getQtdItens(), dto.getQtdItens());
        assertEquals(pedido.getDataCriacao(), dto.getDataCriacao());
        assertEquals(pedido.getDataEntrega(), dto.getDataEntrega());
        assertEquals(pedido.getSituacao(), dto.getSituacao());
    }

    @Test
    @DisplayName("Deve converter uma lista de Pedidos para lista de PedidoRespostaAdminDTO com sucesso")
    void toPedidoRespostaAdminDTOList_deveConverterComSucesso() {
        // Arrange
        List<Pedido> pedidos = Collections.singletonList(pedido);
        
        // Act
        List<PedidoRespostaDTO> resultado = PedidoMapper.toPedidoRespostaAdminDTOList(pedidos);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        
        PedidoRespostaDTO dto = resultado.get(0);
        assertTrue(dto instanceof PedidoRespostaAdminDTO);
        assertEquals(pedido.getId(), dto.getId());
        assertEquals(pedido.getQtdItens(), dto.getQtdItens());
        assertEquals(pedido.getDataCriacao(), dto.getDataCriacao());
        assertEquals(pedido.getDataEntrega(), dto.getDataEntrega());
        assertEquals(pedido.getSituacao(), dto.getSituacao());
        assertEquals(pedido.getUsuario().getNome(), ((PedidoRespostaAdminDTO) dto).getNome());
    }

    // Testes de caminho triste


    @Test
    @DisplayName("Deve lidar com pedido nulo ao converter para PedidoRespostaCadastroDTO")
    void toPedidoRespostaCadastroDTO_comPedidoNulo_deveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> PedidoMapper.toPedidoRespostaCadastroDTO(null));
    }

    @Test
    @DisplayName("Deve lidar com pedido com usuário nulo ao converter para PedidoRespostaCadastroDTO")
    void toPedidoRespostaCadastroDTO_comUsuarioNulo_deveLancarExcecao() {
        // Arrange
        pedido.setUsuario(null);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> PedidoMapper.toPedidoRespostaCadastroDTO(pedido));
    }

    @Test
    @DisplayName("Deve lidar com pedido nulo ao converter para PedidoRespostaDTO")
    void toPedidoRespostaDTO_comPedidoNulo_deveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> PedidoMapper.toPedidoRespostaDTO(null));
    }

    @Test
    @DisplayName("Deve lidar com pedido nulo ao converter para PedidoRespostaAdminDTO")
    void toPedidoRespostaAdminDTO_comPedidoNulo_deveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> PedidoMapper.toPedidoRespostaAdminDTO(null));
    }

    @Test
    @DisplayName("Deve lidar com pedido com usuário nulo ao converter para PedidoRespostaAdminDTO")
    void toPedidoRespostaAdminDTO_comUsuarioNulo_deveLancarExcecao() {
        // Arrange
        pedido.setUsuario(null);
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> PedidoMapper.toPedidoRespostaAdminDTO(pedido));
    }

    @Test
    @DisplayName("Deve lidar com lista vazia ao converter para lista de PedidoRespostaDTO")
    void toPedidoRespostaDTOList_comListaVazia_deveRetornarListaVazia() {
        // Arrange
        List<Pedido> pedidos = Collections.emptyList();
        
        // Act
        List<PedidoRespostaDTO> resultado = PedidoMapper.toPedidoRespostaDTOList(pedidos);
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lidar com lista vazia ao converter para lista de PedidoRespostaAdminDTO")
    void toPedidoRespostaAdminDTOList_comListaVazia_deveRetornarListaVazia() {
        // Arrange
        List<Pedido> pedidos = Collections.emptyList();
        
        // Act
        List<PedidoRespostaDTO> resultado = PedidoMapper.toPedidoRespostaAdminDTOList(pedidos);
        
        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Deve lidar com pedido nulo ao converter para PedidoRespostaDetalhadoDTO")
    void toPedidoRespostaDetalhadoAdminDTO_comPedidoNulo_deveLancarExcecao() {
        // Act & Assert
        assertThrows(NullPointerException.class, () ->
                PedidoMapper.toPedidoRespostaDetalhadoAdminDTO(null, usuarioRespostaDTO, documentos));
    }
}
