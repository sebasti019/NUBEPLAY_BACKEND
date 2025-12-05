package com.example.backendNUBEPLAY.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.*;

import com.example.backendNUBEPLAY.model.Categoria;
import com.example.backendNUBEPLAY.repository.CategoriaRepository;
import com.example.backendNUBEPLAY.assembler.CategoriaModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaRepository repository;
    private final CategoriaModelAssembler assembler;

    public CategoriaController(CategoriaRepository repository, CategoriaModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/api/v1/categorias")
    public ResponseEntity<CollectionModel<EntityModel<Categoria>>> all() {
        List<EntityModel<Categoria>> categorias = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(categorias,
                        linkTo(methodOn(CategoriaController.class).all()).withSelfRel())
        );
    }

    @PostMapping("/api/v1/categorias")
    public ResponseEntity<?> newCategoria(@RequestBody Categoria nueva) {

        repository.findByNombre(nueva.getNombre())
                .ifPresent(c -> { throw new RuntimeException("La categoría ya existe"); });

        EntityModel<Categoria> entityModel = assembler.toModel(repository.save(nueva));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/api/v1/categorias/{id}")
    public ResponseEntity<EntityModel<Categoria>> getById(@PathVariable("id") Long id) {
        Categoria categoria = repository.findById(id)
                .orElseThrow(() -> new CategoriaNotFoundException(id));

        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @PutMapping("/api/v1/categorias/{id}")
    public ResponseEntity<EntityModel<Categoria>> replaceCategoria(
            @RequestBody Categoria nuevaCategoria,
            @PathVariable("id") Long id) {

        Categoria categoria = repository.findById(id)
                .map(c -> {
                    c.setNombre(nuevaCategoria.getNombre());
                    return repository.save(c);
                })
                .orElseGet(() -> {
                    nuevaCategoria.setId(id);
                    return repository.save(nuevaCategoria);
                });

        return ResponseEntity.ok(assembler.toModel(categoria));
    }

    @DeleteMapping("/api/v1/categorias/{id}")
    public ResponseEntity<?> deleteCategoria(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}