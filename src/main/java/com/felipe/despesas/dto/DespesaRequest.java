package com.felipe.despesas.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DespesaRequest {

    private String descricao;

    private BigDecimal valor;

    private LocalDate data;

    private Long categoriaId;
}
