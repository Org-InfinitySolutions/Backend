package com.infinitysolutions.crud.controller;
import com.infinitysolutions.crud.mapper.EstoqueMapper;
import com.infinitysolutions.crud.model.Estoque;
import com.infinitysolutions.crud.model.dto.EstoqueDtoCriacao;
import com.infinitysolutions.crud.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {
    @Autowired
    private EstoqueService service;

    @PostMapping
    public ResponseEntity<EstoqueDtoCriacao> criar(@RequestBody EstoqueDtoCriacao dto){
        Estoque estoque = EstoqueMapper.toEstoque(dto);
        Estoque estoqueSalvo = service.criar(estoque);
        return new ResponseEntity<>(EstoqueMapper.toDto(estoqueSalvo), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Estoque>> listar(){
        return new ResponseEntity<>(service.listar(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarPorId(@PathVariable Integer id){
        return new ResponseEntity<>(service.buscarPorId(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estoque> atualizar(@PathVariable Integer id, @RequestBody EstoqueDtoCriacao dto){
        Estoque estoque = EstoqueMapper.toEstoque(dto);
        return new ResponseEntity<>(service.atualizar(id, estoque), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
        service.deletar(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
