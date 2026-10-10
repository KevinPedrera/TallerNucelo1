package com.example.tallernucleo1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(NoSuchElementException.class)
    ResponseEntity<Map<String, String>> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Recurso no encontrado"));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    ResponseEntity<Map<String, String>> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
