package com.oracle.OSfacil.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import com.oracle.OSfacil.dto.request.ItemProdutoDTO;
import com.oracle.OSfacil.dto.response.ItemProdutoResponseDTO;
import com.oracle.OSfacil.service.ItemProdutoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/item-produtos")
@AllArgsConstructor
public class ItemProdutoController {

    private final ItemProdutoService itemProdutoService;

    @PostMapping
    public ResponseEntity<EntityModel<ItemProdutoResponseDTO>> criar(
            @RequestBody @Valid ItemProdutoDTO dto) {

        ItemProdutoResponseDTO itemNovo = itemProdutoService.criar(dto);

        EntityModel<ItemProdutoResponseDTO> resource = EntityModel.of(itemNovo,
                linkTo(methodOn(ItemProdutoController.class).buscar(itemNovo.getId())).withSelfRel(),
                linkTo(methodOn(ItemProdutoController.class).atualizar(itemNovo.getId(), null)).withRel("atualizar"),
                linkTo(methodOn(ItemProdutoController.class).deletar(itemNovo.getId())).withRel("deletar")
        );

        URI location = linkTo(methodOn(ItemProdutoController.class).buscar(itemNovo.getId())).toUri();

        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ItemProdutoResponseDTO>>> listarTodos() {

        List<EntityModel<ItemProdutoResponseDTO>> itens = itemProdutoService.listarTodos()
                .stream()
                .map(item -> EntityModel.of(item,
                        linkTo(methodOn(ItemProdutoController.class).buscar(item.getId())).withSelfRel(),
                        linkTo(methodOn(ItemProdutoController.class).atualizar(item.getId(), null)).withRel("atualizar"),
                        linkTo(methodOn(ItemProdutoController.class).deletar(item.getId())).withRel("deletar")
                ))
                .toList();

        CollectionModel<EntityModel<ItemProdutoResponseDTO>> collection =
                CollectionModel.of(itens,
                        linkTo(methodOn(ItemProdutoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ItemProdutoResponseDTO>> buscar(@PathVariable Long id) {

        ItemProdutoResponseDTO item = itemProdutoService.buscar(id);

        EntityModel<ItemProdutoResponseDTO> resource = EntityModel.of(item,
                linkTo(methodOn(ItemProdutoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(ItemProdutoController.class).atualizar(id, null)).withRel("atualizar"),
                linkTo(methodOn(ItemProdutoController.class).deletar(id)).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ItemProdutoResponseDTO>> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ItemProdutoDTO dto) {

        ItemProdutoResponseDTO atualizado = itemProdutoService.atualizar(id, dto);

        EntityModel<ItemProdutoResponseDTO> resource = EntityModel.of(atualizado,
                linkTo(methodOn(ItemProdutoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(ItemProdutoController.class).deletar(id)).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        itemProdutoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}