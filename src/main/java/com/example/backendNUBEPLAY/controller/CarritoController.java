package com.example.backendNUBEPLAY.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.*;

import com.example.backendNUBEPLAY.model.CarritoItem;
import com.example.backendNUBEPLAY.repository.CarritoRepository;
import com.example.backendNUBEPLAY.assembler.CarritoModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/carrito")
public class CarritoController {

    private final CarritoRepository repository;
    private final CarritoModelAssembler assembler;

    public CarritoController(CarritoRepository repository, CarritoModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }


    @GetMapping("/{usuarioId}")
    public ResponseEntity<CollectionModel<EntityModel<CarritoItem>>> getCarrito(@PathVariable("usuarioId") Long usuarioId) {
        List<EntityModel<CarritoItem>> items = repository.findByUsuarioId(usuarioId)
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(items,
                        linkTo(methodOn(CarritoController.class).getCarrito(usuarioId)).withSelfRel())
        );
    }


    @GetMapping("/item/{id}")
    public ResponseEntity<EntityModel<CarritoItem>> getItemById(@PathVariable("id") Long id) {
        CarritoItem item = repository.findById(id)
                .orElseThrow(() -> new CarritoNotFoundException(id));

        return ResponseEntity.ok(assembler.toModel(item));
    }


    @PostMapping("/agregar")
    public ResponseEntity<?> agregar(@RequestBody Map<String, Object> data) {

        Long usuarioId = Long.valueOf(data.get("usuarioId").toString());
        Long productoId = Long.valueOf(data.get("productoId").toString());
        String nombre = data.get("nombre").toString();
        String imagen = data.get("imagen").toString();
        Integer precio = Integer.valueOf(data.get("precio").toString());

        Integer cantidad = data.get("cantidad") != null
                ? Integer.valueOf(data.get("cantidad").toString())
                : 1;

        CarritoItem item = repository
                .findByUsuarioIdAndProductoId(usuarioId, productoId)
                .map(i -> {
                    i.setCantidad(i.getCantidad() + cantidad);
                    return repository.save(i);
                })
                .orElseGet(() -> {
                    CarritoItem nuevo = new CarritoItem();
                    nuevo.setUsuarioId(usuarioId);
                    nuevo.setProductoId(productoId);
                    nuevo.setNombreProducto(nombre);
                    nuevo.setImagenProducto(imagen);
                    nuevo.setPrecio(precio);
                    nuevo.setCantidad(cantidad);
                    return repository.save(nuevo);
                });

        return ResponseEntity.ok(assembler.toModel(item));
    }


    @PutMapping("/cantidad/{id}")
    public ResponseEntity<EntityModel<CarritoItem>> cambiarCantidad(
            @PathVariable("id") Long id,
            @RequestBody Map<String, Integer> data) {

        Integer nuevaCantidad = data.get("cantidad");

        CarritoItem item = repository.findById(id)
                .map(i -> {
                    i.setCantidad(nuevaCantidad);
                    return repository.save(i);
                })
                .orElseThrow(() -> new CarritoNotFoundException(id));

        return ResponseEntity.ok(assembler.toModel(item));
    }


    @DeleteMapping("/item/{id}")
    public ResponseEntity<?> eliminarItem(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<Map<String, String>> vaciarCarrito(
            @PathVariable("usuarioId") Long usuarioId) {

        repository.deleteByUsuarioId(usuarioId);

        return ResponseEntity.ok(
            Map.of("mensaje", "Carrito vaciado correctamente")
        );
    }
}