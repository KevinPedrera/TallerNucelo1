package com.example.tallernucleo1.service;

import com.example.tallernucleo1.model.Categoria;
import com.example.tallernucleo1.model.Producto;
import com.example.tallernucleo1.repository.CategoriaRepository;
import com.example.tallernucleo1.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<Producto> listar(String busqueda) {
        return busqueda == null || busqueda.isBlank()
                ? productoRepository.findAll()
                : productoRepository.findByNombreContainingIgnoreCase(busqueda.trim());
    }

    public Producto obtener(Long id) {
        return productoRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Producto guardar(Producto producto, Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId).orElseThrow();
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }

    @Transactional
    public Producto actualizar(Long id, Producto datos, Long categoriaId) {
        Producto producto = obtener(id);
        producto.setNombre(datos.getNombre());
        producto.setDescripcion(datos.getDescripcion());
        producto.setPrecio(datos.getPrecio());
        producto.setStock(datos.getStock());
        producto.setCategoria(categoriaRepository.findById(categoriaId).orElseThrow());
        return productoRepository.save(producto);
    }

    @Transactional
    public void eliminar(Long id) { productoRepository.deleteById(id); }

    public BigDecimal valorInventario() {
        return productoRepository.findAll().stream()
                .map(p -> p.getPrecio().multiply(BigDecimal.valueOf(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int unidadesDisponibles() {
        return productoRepository.findAll().stream().mapToInt(Producto::getStock).sum();
    }
}
