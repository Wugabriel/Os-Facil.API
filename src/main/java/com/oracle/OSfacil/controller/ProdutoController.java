package com.oracle.OSfacil.controller;


import com.oracle.OSfacil.dto.request.ProdutoDTO;
import com.oracle.OSfacil.dto.response.ProdutoResponseDTO;
import com.oracle.OSfacil.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/produtos")
@AllArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<EntityModel<ProdutoResponseDTO>> criar(
            @RequestBody @Valid ProdutoDTO dto) {

        ProdutoResponseDTO produtoNovo = produtoService.criar(dto);

        EntityModel<ProdutoResponseDTO> resource = EntityModel.of(produtoNovo,
                linkTo(methodOn(ProdutoController.class).buscarPorId(produtoNovo.getId())).withSelfRel(),
                linkTo(methodOn(ProdutoController.class).atualizar(produtoNovo.getId(), null)).withRel("atualizar"),
                linkTo(methodOn(ProdutoController.class).deletar(produtoNovo.getId())).withRel("deletar"),
                linkTo(methodOn(ProdutoController.class).listar()).withRel("listar_todos")
        );

        URI location = linkTo(methodOn(ProdutoController.class).buscarPorId(produtoNovo.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ProdutoResponseDTO>>> listar() {

        List<EntityModel<ProdutoResponseDTO>> produtos = produtoService.listarTodos()
                .stream()
                .map(produto -> EntityModel.of(produto,
                        linkTo(methodOn(ProdutoController.class).buscarPorId(produto.getId())).withSelfRel(),
                        linkTo(methodOn(ProdutoController.class).atualizar(produto.getId(), null)).withRel("atualizar"),
                        linkTo(methodOn(ProdutoController.class).deletar(produto.getId())).withRel("deletar")
                ))
                .toList();

        CollectionModel<EntityModel<ProdutoResponseDTO>> collection =
                CollectionModel.of(produtos,
                        linkTo(methodOn(ProdutoController.class).listar()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoResponseDTO>> buscarPorId(@PathVariable Long id) {

        ProdutoResponseDTO produto = produtoService.buscar(id);

        EntityModel<ProdutoResponseDTO> resource = EntityModel.of(produto,
                linkTo(methodOn(ProdutoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ProdutoController.class).atualizar(id, null)).withRel("atualizar"),
                linkTo(methodOn(ProdutoController.class).deletar(id)).withRel("deletar"),
                linkTo(methodOn(ProdutoController.class).listar()).withRel("listar_todos")
        );

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoResponseDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ProdutoDTO dto) {

        ProdutoResponseDTO atualizado = produtoService.atualizar(id, dto);

        EntityModel<ProdutoResponseDTO> resource = EntityModel.of(atualizado,
                linkTo(methodOn(ProdutoController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ProdutoController.class).deletar(id)).withRel("deletar"),
                linkTo(methodOn(ProdutoController.class).listar()).withRel("listar_todos")
        );

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}