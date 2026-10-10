package com.example.tallernucleo1.controller;

import com.example.tallernucleo1.model.Pedido;
import com.example.tallernucleo1.service.PedidoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoRestController {
    private final PedidoService pedidoService;

    public PedidoRestController(PedidoService pedidoService) { this.pedidoService = pedidoService; }

    @GetMapping
    public List<Pedido> listar(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                ? pedidoService.listarTodos() : pedidoService.listar(authentication.getName());
    }

    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable Long id) { return pedidoService.obtener(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(@Valid @RequestBody CrearPedidoRequest request, Authentication authentication) {
        return pedidoService.crear(authentication.getName(), request.getDireccionEntrega(), request.getProductoIds());
    }

    public static class CrearPedidoRequest {
        @NotBlank
        private String direccionEntrega;
        @NotEmpty
        private List<Long> productoIds;

        public String getDireccionEntrega() { return direccionEntrega; }
        public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
        public List<Long> getProductoIds() { return productoIds; }
        public void setProductoIds(List<Long> productoIds) { this.productoIds = productoIds; }
    }
}
