package com.example.tallernucleo1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiSecurityTests {
    @Autowired MockMvc mockMvc;

    @Test
    void productosGetEsPublico() throws Exception {
        mockMvc.perform(get("/api/productos")).andExpect(status().isOk());
    }

    @Test
    void crearProductoSinCredencialesDevuelve401() throws Exception {
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Teclado\",\"precio\":25.50,\"stock\":4,\"categoria\":{\"id\":1}}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void categoriaPuedeCrearLaAdmin() throws Exception {
        mockMvc.perform(post("/api/categorias")
                .with(httpBasic("admin", "admin12345"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Pruebas\"}"))
                .andExpect(status().isCreated());
    }
}
