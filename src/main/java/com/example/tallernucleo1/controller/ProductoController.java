package com.example.tallernucleo1.controller;

import com.example.tallernucleo1.model.Producto;
import com.example.tallernucleo1.service.CategoriaService;
import com.example.tallernucleo1.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProductoController {
    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    public ProductoController(ProductoService productoService, CategoriaService categoriaService) {
        this.productoService = productoService;
        this.categoriaService = categoriaService;
    }

    @GetMapping({"/", "/productos"})
    public String listar(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("productos", productoService.listar(q));
        model.addAttribute("busqueda", q == null ? "" : q);
        model.addAttribute("valorInventario", productoService.valorInventario());
        model.addAttribute("unidadesDisponibles", productoService.unidadesDisponibles());
        return "productos/lista";
    }

    @GetMapping("/productos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listar());
        return "productos/formulario";
    }

    @GetMapping("/productos/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", productoService.obtener(id));
        model.addAttribute("categorias", categoriaService.listar());
        return "productos/formulario";
    }

    @PostMapping("/productos/guardar")
    public String guardar(@Valid @ModelAttribute Producto producto, BindingResult result,
                          @RequestParam Long categoriaId, Model model,
                          RedirectAttributes redirect) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listar());
            return "productos/formulario";
        }
        if (producto.getId() == null) productoService.guardar(producto, categoriaId);
        else productoService.actualizar(producto.getId(), producto, categoriaId);
        redirect.addFlashAttribute("mensaje", "Producto guardado correctamente");
        return "redirect:/productos";
    }

    @PostMapping("/productos/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirect) {
        productoService.eliminar(id);
        redirect.addFlashAttribute("mensaje", "Producto eliminado correctamente");
        return "redirect:/productos";
    }
}
