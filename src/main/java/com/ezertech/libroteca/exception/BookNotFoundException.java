package com.ezertech.libroteca.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(Long id) {
        super("No se encontró el libro con ID: " + id);
    }
}