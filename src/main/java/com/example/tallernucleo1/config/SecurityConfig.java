package com.example.tallernucleo1.config;

import com.example.tallernucleo1.model.Role;
import com.example.tallernucleo1.model.Usuario;
import com.example.tallernucleo1.repository.UsuarioRepository;
import com.example.tallernucleo1.repository.CategoriaRepository;
import com.example.tallernucleo1.model.Categoria;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final UsuarioRepository usuarioRepository;

    public SecurityConfig(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsername(username)
                .map(u -> User.withUsername(u.getUsername())
                        .password(u.getPassword())
                        .roles(u.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/login", "/registro", "/error").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**", "/api/categorias/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/productos/**", "/api/categorias/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().authenticated())
            .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/", true).permitAll())
            .httpBasic(basic -> {})
            .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());
        return http.build();
    }

    @Bean
    CommandLineRunner seedAdmin(PasswordEncoder encoder,
                                CategoriaRepository categoriaRepository,
                                @Value("${ADMIN_USERNAME:admin}") String username,
                                @Value("${ADMIN_PASSWORD:admin12345}") String password) {
        return args -> {
            if (!usuarioRepository.existsByUsername(username)) {
                Usuario admin = new Usuario();
                admin.setUsername(username);
                admin.setNombreCompleto("Administrador del sistema");
                admin.setPassword(encoder.encode(password));
                admin.setRole(Role.ADMIN);
                usuarioRepository.save(admin);
            }
            if (categoriaRepository.count() == 0) {
                for (String nombre : new String[]{"Tecnología", "Oficina", "Hogar"}) {
                    Categoria categoria = new Categoria();
                    categoria.setNombre(nombre);
                    categoriaRepository.save(categoria);
                }
            }
        };
    }
}
