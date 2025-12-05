package com.example.backendNUBEPLAY.controller;

public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("No se encontró el producto con id = " + id);
    }
}