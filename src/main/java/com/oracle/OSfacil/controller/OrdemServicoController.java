package com.oracle.OSfacil.controller;


import com.oracle.OSfacil.dto.request.OrdemServicoDTO;
import com.oracle.OSfacil.dto.response.OrdemServicoResponseDTO;
import com.oracle.OSfacil.service.OrdemServicoService;
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
@RequestMapping("/ordem-servicos")
@AllArgsConstructor
public class OrdemServicoController {

    private final OrdemServicoService ordemServicoService;

    @PostMapping
    public ResponseEntity<EntityModel<OrdemServicoResponseDTO>> criar(
            @RequestBody @Valid OrdemServicoDTO dto) {

        OrdemServicoResponseDTO ordemNova = ordemServicoService.criar(dto);

        EntityModel<OrdemServicoResponseDTO> resource = EntityModel.of(ordemNova,
                linkTo(methodOn(OrdemServicoController.class).buscar(ordemNova.getId())).withSelfRel(),
                linkTo(methodOn(OrdemServicoController.class).atualizar(ordemNova.getId(), null)).withRel("atualizar"),
                linkTo(methodOn(OrdemServicoController.class).deletar(ordemNova.getId())).withRel("deletar")
        );

        URI location = linkTo(methodOn(OrdemServicoController.class).buscar(ordemNova.getId())).toUri();

        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<OrdemServicoResponseDTO>>> listarTodos() {

        List<EntityModel<OrdemServicoResponseDTO>> ordens = ordemServicoService.listarTodos()
                .stream()
                .map(ordem -> EntityModel.of(ordem,
                        linkTo(methodOn(OrdemServicoController.class).buscar(ordem.getId())).withSelfRel(),
                        linkTo(methodOn(OrdemServicoController.class).atualizar(ordem.getId(), null)).withRel("atualizar"),
                        linkTo(methodOn(OrdemServicoController.class).deletar(ordem.getId())).withRel("deletar")
                ))
                .toList();

        CollectionModel<EntityModel<OrdemServicoResponseDTO>> collection =
                CollectionModel.of(ordens,
                        linkTo(methodOn(OrdemServicoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<OrdemServicoResponseDTO>> buscar(@PathVariable Long id) {

        OrdemServicoResponseDTO ordem = ordemServicoService.buscar(id);

        EntityModel<OrdemServicoResponseDTO> resource = EntityModel.of(ordem,
                linkTo(methodOn(OrdemServicoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(OrdemServicoController.class).atualizar(id, null)).withRel("atualizar"),
                linkTo(methodOn(OrdemServicoController.class).deletar(id)).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<OrdemServicoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody OrdemServicoDTO dto) {

        OrdemServicoResponseDTO atualizado = ordemServicoService.atualizar(dto, id);

        EntityModel<OrdemServicoResponseDTO> resource = EntityModel.of(atualizado,
                linkTo(methodOn(OrdemServicoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(OrdemServicoController.class).deletar(id)).withRel("deletar")
        );

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        ordemServicoService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}