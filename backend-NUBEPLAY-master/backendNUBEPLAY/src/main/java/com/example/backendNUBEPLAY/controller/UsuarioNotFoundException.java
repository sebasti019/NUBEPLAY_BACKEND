package com.example.backendNUBEPLAY.controller;

public class UsuarioNotFoundException extends RuntimeException {

    public UsuarioNotFoundException(Long id) {
        super("no hay usuario con el id =  " + id);
    }
}