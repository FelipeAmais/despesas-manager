package com.felipe.despesas.services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.felipe.despesas.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.felipe.despesas.model.Despesa;
import com.felipe.despesas.repository.DespesaRepository;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;

    public DespesaService(DespesaRepository despesaRepository) {
        this.despesaRepository = despesaRepository;
    }

    private Usuario getUsuarioAutenticado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public List<Despesa> listarDespesas() {
        return despesaRepository.findByUsuario(getUsuarioAutenticado());
    }

    public Optional<Despesa> buscarPorId(Long id) {
        return despesaRepository.findById(id);
    }

    public Despesa criarDespesa(Despesa despesa) {
        despesa.setUsuario(getUsuarioAutenticado());
        validarDespesa(despesa);
        return despesaRepository.save(despesa);
    }

    /* Validacoes para a criacao da despesa */
    public void validarDespesa(Despesa despesa) {
        if (despesa.getValor() == null || despesa.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
        }

        if (despesa.getData() == null || despesa.getData().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data não pode ser futura.");
        }

        if (despesa.getDescricao() == null || despesa.getDescricao().isBlank()) {
            throw new IllegalArgumentException("A descrição não pode ser vazia.");
        }
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
