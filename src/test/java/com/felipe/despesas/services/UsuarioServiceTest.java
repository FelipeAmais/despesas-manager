package com.felipe.despesas.services;

import com.felipe.despesas.dto.LoginRequest;
import com.felipe.despesas.dto.LoginResponse;
import com.felipe.despesas.dto.UsuarioResponse;
import com.felipe.despesas.exception.InvalidCredentialsException;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.repository.UsuarioRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private UsuarioResponse usuarioResponse;

    @BeforeEach
    void setUp() {
        this.loginRequest = new LoginRequest();
        loginRequest.setEmail("emaildeteste1@gmail.com");
        loginRequest.setSenha("senhadeteste1");

        this.usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("emaildeteste1@gmail.com");
        usuario.setSenha("$2a$10$hasheado");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void criarUsuario() {
        when(passwordEncoder.encode("senhadeteste1")).thenReturn("$2a$10$hasheado");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioResponse usuarioResponse = usuarioService.criarUsuario(loginRequest);
        assertEquals("emaildeteste1@gmail.com", usuarioResponse.getEmail());
    }

    @Test
    void login() {
        when(jwtService.gerarToken("emaildeteste1@gmail.com")).thenReturn("token-de-teste");
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhadeteste1", "$2a$10$hasheado")).thenReturn(true);

        LoginResponse loginResponse = usuarioService.login(loginRequest);
        assertEquals("token-de-teste", loginResponse.getToken());
    }

    @Test
    void loginEmailNaoEncontrado() {
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> {
            usuarioService.login(loginRequest);
        });

    }

    @Test
    void loginSenhaIncorreta(){
        when(usuarioRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhadeteste1", "$2a$10$hasheado")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> {
            usuarioService.login(loginRequest);
        });
    }

    @Test
    void loadUserByUsername() {
    }
}