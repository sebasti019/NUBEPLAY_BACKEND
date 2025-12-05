package com.example.backendNUBEPLAY.controller;

public class CarritoNotFoundException extends RuntimeException {

    public CarritoNotFoundException(Long id) {
        super("No existe item de carrito con id = " + id);
    }
}