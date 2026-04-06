package com.oracle.OSfacil.controller;


import com.oracle.OSfacil.dto.request.FuncionarioDTO;
import com.oracle.OSfacil.dto.response.FuncionarioResponseDTO;
import com.oracle.OSfacil.service.FuncionarioService;
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
@RequestMapping("/funcionarios")
@AllArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<EntityModel<FuncionarioResponseDTO>> criar(
            @Valid @RequestBody FuncionarioDTO dto) {

        FuncionarioResponseDTO novo = funcionarioService.criar(dto);

        EntityModel<FuncionarioResponseDTO> resource = EntityModel.of(novo,
                linkTo(methodOn(FuncionarioController.class).listarPorId(novo.getId())).withSelfRel(),
                linkTo(methodOn(FuncionarioController.class).atualizar(novo.getId(), null)).withRel("atualizar"),
                linkTo(methodOn(FuncionarioController.class).deletarPorId(novo.getId())).withRel("deletar")
        );

        URI location = linkTo(methodOn(FuncionarioController.class).listarPorId(novo.getId())).toUri();

        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<FuncionarioResponseDTO>>> listarTodos() {

        List<EntityModel<FuncionarioResponseDTO>> funcionarios = funcionarioService.listarTodos()
                .stream()
                .map(func -> EntityModel.of(func,
                        linkTo(methodOn(FuncionarioController.class).listarPorId(func.getId())).withSelfRel(),
                        linkTo(methodOn(FuncionarioController.class).atualizar(func.getId(), null)).withRel("atualizar"),
                        linkTo(methodOn(FuncionarioController.class).deletarPorId(func.getId())).withRel("deletar")
                ))
                .toList();

        CollectionModel<EntityModel<FuncionarioResponseDTO>> collection =
                CollectionModel.of(funcionarios,
                        linkTo(methodOn(FuncionarioController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<FuncionarioResponseDTO>> listarPorId(@PathVariable Long id) {

        FuncionarioResponseDTO func = funcionarioService.listarPorId(id);

        EntityModel<FuncionarioResponseDTO> resource = EntityModel.of(func,
                linkTo(methodOn(FuncionarioController.class).listarPorId(id)).withSelfRel(),
                linkTo(methodOn(FuncionarioController.class).atualizar(id, null)).withRel("atualizar"),
                linkTo(methodOn(FuncionarioController.class).deletarPorId(id)).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<FuncionarioResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody FuncionarioDTO dto) {

        FuncionarioResponseDTO atualizado = funcionarioService.atualizar(dto, id);

        EntityModel<FuncionarioResponseDTO> resource = EntityModel.of(atualizado,
                linkTo(methodOn(FuncionarioController.class).listarPorId(id)).withSelfRel(),
                linkTo(methodOn(FuncionarioController.class).deletarPorId(id)).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPorId(@PathVariable Long id) {

        funcionarioService.deletarPorId(id);

        return ResponseEntity.noContent().build();
    }
}