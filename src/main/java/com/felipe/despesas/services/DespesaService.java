package com.felipe.despesas.services;

import java.util.List;

import org.hibernate.persister.collection.mutation.DeleteRowsCoordinatorTablePerSubclass;
import org.springframework.stereotype.Service;

import com.felipe.despesas.model.Despesa;
import com.felipe.despesas.repository.DespesaRepository;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;

    public DespesaService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    public List<Despesa> listarDespesas() {
        return despesaRepository.findAll();
    }

    public Despesa buscarPorId(Long id) {
        return despesaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
    }


    public Despesa criarDespesa(Despesa despesa) {
        return despesaRepository.save(despesa);
    }

    public Despesa atualizarDespesa(Despesa despesa) {
        if (despesa.getId() == null || !despesaRepository.existsById(despesa.getId())) {
            throw new IllegalArgumentException("Despesa não encontrada");
        }
        return despesaRepository.save(despesa);
    }

    public void excluirDespesa(Long id) {
        if (!despesaRepository.existsById(id)) {
            throw new IllegalArgumentException("Despesa não encontrada");
        }
        despesaRepository.deleteById(id);
    }
}
