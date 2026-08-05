package com.felipe.despesas.mapper;

import com.felipe.despesas.dto.DespesaRequest;
import com.felipe.despesas.dto.DespesaResponse;
import com.felipe.despesas.model.Categoria;
import com.felipe.despesas.model.Despesa;

public class DespesaMapper {

    public static DespesaResponse toResponse(Despesa despesa){
        return new DespesaResponse(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getData(), despesa.getCategoria());
    }

    public static Despesa toDespesa(Long id, DespesaRequest despesaRequest, Categoria categoria){
        return new Despesa(id, despesaRequest.getDescricao(), despesaRequest.getValor(), despesaRequest.getData(), categoria);
    }
}
