package com.example.backendNUBEPLAY.assembler;

import com.example.backendNUBEPLAY.model.CarritoItem;
import com.example.backendNUBEPLAY.controller.CarritoController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CarritoModelAssembler implements RepresentationModelAssembler<CarritoItem, EntityModel<CarritoItem>> {

    @Override
    public EntityModel<CarritoItem> toModel(CarritoItem item) {
        return EntityModel.of(
                item,
                linkTo(methodOn(CarritoController.class).getCarrito(item.getUsuarioId())).withRel("carrito-usuario"),
                linkTo(methodOn(CarritoController.class).getItemById(item.getId())).withSelfRel()
        );
    }
}