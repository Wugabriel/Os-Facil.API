package com.oracle.OSfacil.controller;


import com.oracle.OSfacil.dto.request.PagamentoDTO;
import com.oracle.OSfacil.dto.response.PagamentoResponseDTO;
import com.oracle.OSfacil.model.Cliente;
import com.oracle.OSfacil.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/pagamentos")
@AllArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<EntityModel<PagamentoResponseDTO>> criar(
            @Valid @RequestBody PagamentoDTO dto,
            @AuthenticationPrincipal Cliente logado) {

        PagamentoResponseDTO pagamentoNovo = pagamentoService.criar(dto, logado);

        EntityModel<PagamentoResponseDTO> resource = EntityModel.of(pagamentoNovo,
                linkTo(methodOn(PagamentoController.class).buscar(pagamentoNovo.getId())).withSelfRel(),
                linkTo(methodOn(PagamentoController.class).atualizar(pagamentoNovo.getId(), null, null)).withRel("atualizar"),
                linkTo(methodOn(PagamentoController.class).deletar(pagamentoNovo.getId())).withRel("deletar"),
                linkTo(methodOn(PagamentoController.class).listarTodos()).withRel("todos-pagamentos")
        );

        URI uri = linkTo(methodOn(PagamentoController.class).buscar(pagamentoNovo.getId())).toUri();
        return ResponseEntity.created(uri).body(resource);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<PagamentoResponseDTO>>> listarTodos() {

        List<EntityModel<PagamentoResponseDTO>> pagamentos = pagamentoService.listarTodos()
                .stream()
                .map(p -> EntityModel.of(p,
                        linkTo(methodOn(PagamentoController.class).buscar(p.getId())).withSelfRel(),
                        linkTo(methodOn(PagamentoController.class).atualizar(p.getId(), null, null)).withRel("atualizar"),
                        linkTo(methodOn(PagamentoController.class).deletar(p.getId())).withRel("deletar")
                ))
                .toList();

        return ResponseEntity.ok(CollectionModel.of(pagamentos,
                linkTo(methodOn(PagamentoController.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PagamentoResponseDTO>> buscar(@PathVariable Long id) {

        PagamentoResponseDTO pagamento = pagamentoService.buscar(id);

        return ResponseEntity.ok(EntityModel.of(pagamento,
                linkTo(methodOn(PagamentoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(PagamentoController.class).atualizar(id, null, null)).withRel("atualizar"),
                linkTo(methodOn(PagamentoController.class).deletar(id)).withRel("deletar"),
                linkTo(methodOn(PagamentoController.class).listarTodos()).withRel("todos-pagamentos")
        ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PagamentoResponseDTO>> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagamentoDTO dto,
            @AuthenticationPrincipal Cliente logado) {

        PagamentoResponseDTO atualizado = pagamentoService.atualizar(id, dto, logado);

        return ResponseEntity.ok(EntityModel.of(atualizado,
                linkTo(methodOn(PagamentoController.class).buscar(id)).withSelfRel(),
                linkTo(methodOn(PagamentoController.class).deletar(id)).withRel("deletar")
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}