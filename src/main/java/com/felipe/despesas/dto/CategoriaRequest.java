package com.felipe.despesas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoriaRequest {

    @NotBlank
    private String nome;
}
