package com.example.backendNUBEPLAY.controller;

import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.hateoas.*;

import com.example.backendNUBEPLAY.model.Usuario;
import com.example.backendNUBEPLAY.repository.UsuarioRepository;
import com.example.backendNUBEPLAY.assembler.UsuarioModelAssembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioRepository repository;
    private final UsuarioModelAssembler assembler;

    public UsuarioController(UsuarioRepository repository, UsuarioModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @GetMapping("/api/v1/usuarios")
    public ResponseEntity<CollectionModel<EntityModel<Usuario>>> all() {
        List<EntityModel<Usuario>> usuarios = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(
                CollectionModel.of(usuarios,
                        linkTo(methodOn(UsuarioController.class).all()).withSelfRel())
        );
    }

    @PostMapping("/api/v1/usuarios")
    public ResponseEntity<?> newUsuario(@RequestBody Usuario nuevo) {
        EntityModel<Usuario> entityModel = assembler.toModel(repository.save(nuevo));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @GetMapping("/api/v1/usuarios/{id}")
    public ResponseEntity<EntityModel<Usuario>> getById(@PathVariable("id") Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException(id));

        return ResponseEntity.ok(assembler.toModel(usuario));
    }

    @PutMapping("/api/v1/usuarios/{id}")
    public ResponseEntity<EntityModel<Usuario>> replaceUsuario(
            @RequestBody Usuario nuevoUsuario,
            @PathVariable("id") Long id) {

        Usuario usuario = repository.findById(id)
                .map(u -> {
                    u.setNombre(nuevoUsuario.getNombre());
                    u.setCorreo(nuevoUsuario.getCorreo());
                    u.setPassword(nuevoUsuario.getPassword());
                    u.setRol(nuevoUsuario.getRol());
                    return repository.save(u);
                })
                .orElseGet(() -> {
                    nuevoUsuario.setId(id);
                    return repository.save(nuevoUsuario);
                });

        EntityModel<Usuario> entityModel = assembler.toModel(usuario);

        return ResponseEntity.ok(entityModel);
    }

    @DeleteMapping("/api/v1/usuarios/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/v1/usuarios/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> data) {

        String correo = data.get("correo").trim();
        String password = data.get("password").trim();

        Usuario usuario = repository.findByCorreo(correo)
                .filter(u -> u.getPassword().equals(password))
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        return ResponseEntity.ok(assembler.toModel(usuario));
    }
}