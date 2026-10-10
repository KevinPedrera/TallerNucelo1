package com.example.tallernucleo1.controller;

import com.example.tallernucleo1.service.PedidoService;
import com.example.tallernucleo1.service.ProductoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PedidoController {
    private final PedidoService pedidoService;
    private final ProductoService productoService;

    public PedidoController(PedidoService pedidoService, ProductoService productoService) {
        this.pedidoService = pedidoService;
        this.productoService = productoService;
    }

    @GetMapping("/pedidos")
    public String listar(Authentication authentication, Model model) {
        boolean admin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("pedidos", admin ? pedidoService.listarTodos() : pedidoService.listar(authentication.getName()));
        model.addAttribute("productos", productoService.listar(null));
        return "pedidos/lista";
    }

    @GetMapping("/pedidos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("productos", productoService.listar(null));
        return "pedidos/formulario";
    }

    @PostMapping("/pedidos/guardar")
    public String guardar(@RequestParam String direccionEntrega,
                          @RequestParam List<Long> productoIds,
                          Authentication authentication,
                          RedirectAttributes redirect) {
        pedidoService.crear(authentication.getName(), direccionEntrega, productoIds);
        redirect.addFlashAttribute("mensaje", "Pedido registrado correctamente");
        return "redirect:/pedidos";
    }
}
