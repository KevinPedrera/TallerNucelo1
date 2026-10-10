package com.example.tallernucleo1.service;

import com.example.tallernucleo1.model.Pedido;
import com.example.tallernucleo1.model.Producto;
import com.example.tallernucleo1.model.Usuario;
import com.example.tallernucleo1.repository.PedidoRepository;
import com.example.tallernucleo1.repository.ProductoRepository;
import com.example.tallernucleo1.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
    }

    public List<Pedido> listar(String username) {
        return pedidoRepository.findByUsuarioUsernameOrderByFechaDesc(username);
    }

    public List<Pedido> listarTodos() { return pedidoRepository.findAll(); }

    public Pedido obtener(Long id) { return pedidoRepository.findById(id).orElseThrow(); }

    @Transactional
    public Pedido crear(String username, String direccionEntrega, List<Long> productoIds) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow();
        if (productoIds == null || productoIds.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un producto");
        }
        List<Producto> productos = productoRepository.findAllById(productoIds);
        if (productos.size() != productoIds.stream().distinct().count()) {
            throw new IllegalArgumentException("Uno o más productos no existen");
        }
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEntrega(direccionEntrega);
        pedido.setProductos(new java.util.HashSet<>(productos));
        return pedidoRepository.save(pedido);
    }
}
