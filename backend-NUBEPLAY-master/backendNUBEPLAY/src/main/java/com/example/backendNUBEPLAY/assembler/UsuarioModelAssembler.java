package com.example.backendNUBEPLAY.assembler;

import com.example.backendNUBEPLAY.model.Usuario;
import com.example.backendNUBEPLAY.controller.UsuarioController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {

        return EntityModel.of(
                usuario,
                linkTo(methodOn(UsuarioController.class).getById(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).all()).withRel("usuarios")
        );
    }
}