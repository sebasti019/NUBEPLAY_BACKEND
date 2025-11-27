package com.example.backendNUBEPLAY.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class NotFoundAdvice {

    @ExceptionHandler(UsuarioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String usuarioNotFoundHandler(UsuarioNotFoundException ex) {
        return ex.getMessage();
    }
}