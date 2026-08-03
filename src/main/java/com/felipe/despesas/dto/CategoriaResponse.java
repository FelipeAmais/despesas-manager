package com.felipe.despesas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaResponse {

    private Long id;
    private String nome;
}
