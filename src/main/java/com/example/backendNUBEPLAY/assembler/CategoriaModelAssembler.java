package com.example.backendNUBEPLAY.assembler;

import com.example.backendNUBEPLAY.model.Categoria;
import com.example.backendNUBEPLAY.controller.CategoriaController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CategoriaModelAssembler implements RepresentationModelAssembler<Categoria, EntityModel<Categoria>> {

    @Override
    public EntityModel<Categoria> toModel(Categoria categoria) {

        return EntityModel.of(
                categoria,
                linkTo(methodOn(CategoriaController.class).getById(categoria.getId())).withSelfRel(),
                linkTo(methodOn(CategoriaController.class).all()).withRel("categorias")
        );
    }
}