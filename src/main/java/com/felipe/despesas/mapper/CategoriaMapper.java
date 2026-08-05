package com.felipe.despesas.mapper;

import com.felipe.despesas.dto.CategoriaResponse;
import com.felipe.despesas.model.Categoria;

public class CategoriaMapper {

    public static CategoriaResponse toResponse(Categoria categoria){
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }

}
