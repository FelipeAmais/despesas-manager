package com.felipe.despesas.controller;

import com.felipe.despesas.config.JwtAuthFilter;
import com.felipe.despesas.config.PasswordConfig;
import com.felipe.despesas.config.RateLimiterFilter;
import com.felipe.despesas.config.SecurityConfig;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.services.DespesaService;
import com.felipe.despesas.services.JwtService;
import com.felipe.despesas.services.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DespesaController.class)
@Import({SecurityConfig.class, JwtAuthFilter.class, RateLimiterFilter.class, PasswordConfig.class})
class DespesaControllerTest {

    @MockitoBean
    private DespesaService despesaService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UsuarioService usuarioService;

    @Autowired
    private MockMvc mockMvc;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        this.usuario = new Usuario();
        this.usuario.setId(1L);
        this.usuario.setEmail("emaildeteste@gmail.com");
    }

    @Test
    void listarDespesas() throws Exception {
        when(jwtService.extrairEmail("token-de-teste")).thenReturn("emaildeteste@gmail.com");
        when(usuarioService.loadUserByUsername("emaildeteste@gmail.com")).thenReturn(usuario);
        when(despesaService.listarDespesas(any())).thenReturn(Page.empty());

        mockMvc.perform(get("/despesas")
                .header("Authorization", "Bearer token-de-teste"))
                .andExpect(status().isOk());
    }

    @Test
    void listarDespesasSemToken() throws Exception {
        mockMvc.perform(get("/despesas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void criarDespesa() throws Exception {
        when(jwtService.extrairEmail("token-de-teste")).thenReturn("emaildeteste@gmail.com");
        when(usuarioService.loadUserByUsername("emaildeteste@gmail.com")).thenReturn(usuario);

        mockMvc.perform(post("/despesas")
                        .header("Authorization", "Bearer token-de-teste")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"\", \"valor\":\"1\", \"data\": \"2026-01-01\", \"categoriaId\": \"1\"}"))
                .andExpect(status().isBadRequest());

    }
}
