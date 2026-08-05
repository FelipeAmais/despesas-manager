package com.felipe.despesas.mapper;

import com.felipe.despesas.dto.UsuarioResponse;
import com.felipe.despesas.model.Usuario;

public class UsuarioMapper {

    public static UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getEmail());
    }
}
