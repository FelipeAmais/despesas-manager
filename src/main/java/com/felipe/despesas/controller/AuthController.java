package com.felipe.despesas.controller;


import com.felipe.despesas.dto.LoginRequest;
import com.felipe.despesas.dto.LoginResponse;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Usuario register(@Valid @RequestBody LoginRequest usuario) {
        return usuarioService.criarUsuario(usuario);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return usuarioService.login(loginRequest);
    }

}
