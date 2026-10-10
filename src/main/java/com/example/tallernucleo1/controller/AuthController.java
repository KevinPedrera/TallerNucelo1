package com.example.tallernucleo1.controller;

import com.example.tallernucleo1.model.Usuario;
import com.example.tallernucleo1.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) { this.usuarioService = usuarioService; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/registro")
    public String registro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrar(@Valid @ModelAttribute("usuario") Usuario usuario,
                            BindingResult result, Model model) {
        if (result.hasErrors()) return "registro";
        try {
            usuarioService.registrar(usuario);
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
}
