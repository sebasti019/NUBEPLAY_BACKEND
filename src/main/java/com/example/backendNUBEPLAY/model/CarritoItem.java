package com.example.backendNUBEPLAY.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;  // ID del usuario al que pertenece este carrito
    private Long productoId; // ID del producto

    private String nombreProducto;
    private String imagenProducto;

    private Integer precio;
    private Integer cantidad;
}