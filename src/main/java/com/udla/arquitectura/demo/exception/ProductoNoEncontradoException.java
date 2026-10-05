package com.udla.arquitectura.demo.exception;

public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("Producto no encontrado: " + id);
    }
}
