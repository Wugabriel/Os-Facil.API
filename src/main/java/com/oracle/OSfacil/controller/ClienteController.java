package com.oracle.OSfacil.controller;


import com.oracle.OSfacil.dto.request.ClienteDTO;
import com.oracle.OSfacil.dto.response.ClienteResponseDTO;
import com.oracle.OSfacil.service.ClienteService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;



@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<EntityModel<ClienteResponseDTO>> criar(
            @Valid @RequestBody ClienteDTO dto) {

        ClienteResponseDTO novoCliente = clienteService.criar(dto);

        EntityModel<ClienteResponseDTO> resource = EntityModel.of(novoCliente,
                linkTo(methodOn(ClienteController.class).listarPorId(novoCliente.getId())).withSelfRel(),
                linkTo(methodOn(ClienteController.class).atualizar(novoCliente.getId(), null)).withRel("atualizar"),
                linkTo(methodOn(ClienteController.class).deletar(novoCliente.getId())).withRel("deletar"),
                linkTo(methodOn(ClienteController.class).listarTodos()).withRel("clientes")
        );

        URI location = linkTo(methodOn(ClienteController.class).listarPorId(novoCliente.getId())).toUri();
        return ResponseEntity.created(location).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ClienteResponseDTO>>> listarTodos() {

        List<EntityModel<ClienteResponseDTO>> clientes = clienteService.listarTodos()
                .stream()
                .map(c -> EntityModel.of(c,
                        linkTo(methodOn(ClienteController.class).listarPorId(c.getId())).withSelfRel(),
                        linkTo(methodOn(ClienteController.class).atualizar(c.getId(), null)).withRel("atualizar"),
                        linkTo(methodOn(ClienteController.class).deletar(c.getId())).withRel("deletar")
                ))
                .toList();

        CollectionModel<EntityModel<ClienteResponseDTO>> collection =
                CollectionModel.of(clientes,
                        linkTo(methodOn(ClienteController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> listarPorId(@PathVariable Long id) {

        ClienteResponseDTO dto = clienteService.listarPorId(id);

        EntityModel<ClienteResponseDTO> resource = EntityModel.of(dto,
                linkTo(methodOn(ClienteController.class).listarPorId(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).atualizar(id, null)).withRel("atualizar"),
                linkTo(methodOn(ClienteController.class).deletar(id)).withRel("deletar"),
                linkTo(methodOn(ClienteController.class).listarTodos()).withRel("clientes")
        );

        return ResponseEntity.ok(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ClienteResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteDTO dto) {

        ClienteResponseDTO atualizado = clienteService.atualizar(id, dto);

        EntityModel<ClienteResponseDTO> resource = EntityModel.of(atualizado,
                linkTo(methodOn(ClienteController.class).listarPorId(id)).withSelfRel(),
                linkTo(methodOn(ClienteController.class).deletar(id)).withRel("deletar"),
                linkTo(methodOn(ClienteController.class).listarTodos()).withRel("clientes")
        );

        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}


