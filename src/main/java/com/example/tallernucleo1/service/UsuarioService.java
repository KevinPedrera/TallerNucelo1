package com.example.tallernucleo1.service;

import com.example.tallernucleo1.model.Role;
import com.example.tallernucleo1.model.Usuario;
import com.example.tallernucleo1.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRole(Role.USER);
        return usuarioRepository.save(usuario);
    }
}
