package com.example.tallernucleo1.repository;

import com.example.tallernucleo1.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuarioUsernameOrderByFechaDesc(String username);
}
