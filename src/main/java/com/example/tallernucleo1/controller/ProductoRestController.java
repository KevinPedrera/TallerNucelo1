package com.example.tallernucleo1.controller;

import com.example.tallernucleo1.model.Producto;
import com.example.tallernucleo1.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoRestController {
    private final ProductoService productoService;

    public ProductoRestController(ProductoService productoService) { this.productoService = productoService; }

    @GetMapping
    public List<Producto> listar(@RequestParam(required = false) String q) {
        return productoService.listar(q);
    }

    @GetMapping("/{id}")
    public Producto obtener(@PathVariable Long id) { return productoService.obtener(id); }

    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Long categoriaId = producto.getCategoria() == null ? null : producto.getCategoria().getId();
        if (categoriaId == null) throw new IllegalArgumentException("categoria.id es obligatorio");
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.guardar(producto, categoriaId));
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        Long categoriaId = producto.getCategoria() == null ? null : producto.getCategoria().getId();
        if (categoriaId == null) throw new IllegalArgumentException("categoria.id es obligatorio");
        return productoService.actualizar(id, producto, categoriaId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { productoService.eliminar(id); }
}
