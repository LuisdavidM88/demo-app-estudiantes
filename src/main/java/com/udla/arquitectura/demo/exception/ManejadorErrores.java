package com.udla.arquitectura.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Centraliza la conversión de excepciones en respuestas HTTP, de manera que
 * los controladores no necesiten gestionar errores individualmente.
 */
@RestControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(ProductoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> solicitudInvalida(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}
