package com.example.tallernucleo1.controller;

import com.example.tallernucleo1.model.Categoria;
import com.example.tallernucleo1.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaRestController {
    private final CategoriaService service;

    public CategoriaRestController(CategoriaService service) { this.service = service; }

    @GetMapping
    public List<Categoria> listar() { return service.listar(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Categoria crear(@Valid @RequestBody Categoria categoria) { return service.guardar(categoria); }
}
