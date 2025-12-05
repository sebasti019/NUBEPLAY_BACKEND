package com.example.backendNUBEPLAY.controller;

import com.example.backendNUBEPLAY.assembler.ProductoModelAssembler;
import com.example.backendNUBEPLAY.model.Producto;
import com.example.backendNUBEPLAY.repository.ProductoRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoRepository repository;
    private final ProductoModelAssembler assembler;

    public ProductoController(ProductoRepository repository, ProductoModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Producto>>> all() {
        List<EntityModel<Producto>> productos = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(productos,
                        linkTo(methodOn(ProductoController.class).all()).withSelfRel())
        );
    }

    @PostMapping
    public ResponseEntity<?> newProducto(@RequestBody Producto nuevoProducto) {
        EntityModel<Producto> entityModel = assembler.toModel(repository.save(nuevoProducto));
        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> getById(@PathVariable("id") Long id) {
        Producto producto = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id " + id));
        return ResponseEntity.ok(assembler.toModel(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Producto>> replaceProducto(@RequestBody Producto nuevoProducto, @PathVariable("id") Long id) {
        Producto producto = repository.findById(id)
                .map(p -> {
                    p.setNombre(nuevoProducto.getNombre());
                    p.setDescripcion(nuevoProducto.getDescripcion());
                    p.setPrecio(nuevoProducto.getPrecio());
                    p.setImagen(nuevoProducto.getImagen());
                    p.setCategoria(nuevoProducto.getCategoria());
                    return repository.save(p);
                })
                .orElseGet(() -> {
                    nuevoProducto.setId(id);
                    return repository.save(nuevoProducto);
                });

        EntityModel<Producto> entityModel = assembler.toModel(producto);
        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProducto(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}