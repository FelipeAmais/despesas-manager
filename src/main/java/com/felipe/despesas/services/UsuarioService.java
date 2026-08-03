package com.felipe.despesas.services;

import com.felipe.despesas.dto.LoginRequest;
import com.felipe.despesas.dto.LoginResponse;
import com.felipe.despesas.dto.UsuarioResponse;
import com.felipe.despesas.exception.InvalidCredentialsException;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getEmail());
    }

    @Transactional
    public UsuarioResponse criarUsuario(LoginRequest loginRequest) {
        Usuario usuario = new Usuario();
        usuario.setEmail(loginRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(loginRequest.getSenha()));
        Usuario salva =  usuarioRepository.save(usuario);
        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest loginRequest) {
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Email ou senha Inválidos"));

        if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
            throw new InvalidCredentialsException("Email ou senha Inválidos");
        }

        String token = jwtService.gerarToken(loginRequest.getEmail());
        return new LoginResponse(token);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));
    }
}
