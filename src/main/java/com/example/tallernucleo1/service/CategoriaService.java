package com.example.tallernucleo1.service;

import com.example.tallernucleo1.model.Categoria;
import com.example.tallernucleo1.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository repository;

    public CategoriaService(CategoriaRepository repository) { this.repository = repository; }

    public List<Categoria> listar() { return repository.findAll(); }
    public Categoria obtener(Long id) { return repository.findById(id).orElseThrow(); }
    public Categoria guardar(Categoria categoria) { return repository.save(categoria); }
}
