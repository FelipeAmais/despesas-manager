package com.felipe.despesas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DespesaResponse {

    private Long id;
    private String descricao;
    private BigDecimal valor;
    private LocalDate data;
    private CategoriaResponse categoria;
}
