package com.example.backendNUBEPLAY.controller;

public class CategoriaNotFoundException extends RuntimeException {

    public CategoriaNotFoundException(Long id) {
        super("No existe categoría con id = " + id);
    }
}