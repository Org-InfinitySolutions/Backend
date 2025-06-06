package com.infinitysolutions.applicationservice.controller.produto;

import com.infinitysolutions.applicationservice.model.dto.produto.CategoriaCriacaoDTO;
import com.infinitysolutions.applicationservice.model.dto.produto.CategoriaRespostaDTO;
import com.infinitysolutions.applicationservice.service.produto.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Validated
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Endpoints para gerenciamento de categorias de produtos")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Listar todas as categorias",
            description = "Retorna uma lista com todas as categorias cadastradas no sistema."
    )
    public List<CategoriaRespostaDTO> listarTodasCategorias() {
        return categoriaService.listarTodasCategorias();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Buscar categoria por ID",
            description = "Retorna os detalhes de uma categoria específica a partir do seu ID"
    )
    public CategoriaRespostaDTO buscarCategoriaPorId(
            @Parameter(description = "ID da categoria a ser buscada", required = true)
            @PathVariable @Positive Integer id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Criar uma nova categoria",
            description = "Cria uma nova categoria no sistema a partir dos dados fornecidos"
    )
    public CategoriaRespostaDTO criarCategoria(
            @Valid @RequestBody CategoriaCriacaoDTO dto) {
        return categoriaService.criar(dto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Atualizar uma categoria",
            description = "Atualiza os dados de uma categoria existente a partir do seu ID"
    )
    public CategoriaRespostaDTO atualizarCategoria(
            @Parameter(description = "ID da categoria a ser atualizada", required = true)
            @PathVariable @Positive Integer id,
            @Valid @RequestBody CategoriaCriacaoDTO dto) {
        return categoriaService.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Remover uma categoria",
            description = "Remove uma categoria do sistema a partir do seu ID"
    )
    public void removerCategoria(
            @Parameter(description = "ID da categoria a ser removida", required = true)
            @PathVariable @Positive Integer id) {
        categoriaService.remover(id);
    }
}
