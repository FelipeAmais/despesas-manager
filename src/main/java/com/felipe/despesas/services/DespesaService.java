package com.felipe.despesas.services;

import java.time.LocalDate;
import java.util.List;
import com.felipe.despesas.dto.DespesaRequest;
import com.felipe.despesas.dto.DespesaResponse;
import com.felipe.despesas.exception.NotFoundException;
import com.felipe.despesas.model.Categoria;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.felipe.despesas.model.Despesa;
import com.felipe.despesas.repository.DespesaRepository;

@Service
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final CategoriaRepository categoriaRepository;

    public DespesaService(DespesaRepository despesaRepository,  CategoriaRepository categoriaRepository) {
        this.despesaRepository = despesaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    private Usuario getUsuarioAutenticado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private DespesaResponse toResponse(Despesa despesa) {
        return new DespesaResponse(despesa.getId(), despesa.getDescricao(), despesa.getValor(), despesa.getData(), despesa.getCategoria());
    }

    private Despesa toDespesa(Long id,DespesaRequest despesaRequest) {
        Categoria categoria = categoriaRepository.findById(despesaRequest.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        return new Despesa(id, despesaRequest.getDescricao(), despesaRequest.getValor(), despesaRequest.getData(), categoria);
    }

    private  Despesa buscarDespesaValidada(Long id){
        return despesaRepository.findById(id).filter(despesa -> despesa.getUsuario().getId().equals(getUsuarioAutenticado().getId()))
                .orElseThrow(() -> new NotFoundException("Despesa inexistente"));
    }

    public Page<DespesaResponse> listarDespesas(Pageable pageable) {
        Page<Despesa> despesas = despesaRepository.findByUsuario(getUsuarioAutenticado(), pageable);
        return despesas.map(this::toResponse);
    }

    public List<DespesaResponse> listaPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Despesa> despesas = despesaRepository.findByUsuarioAndDataBetween(getUsuarioAutenticado(), inicio, fim);
        return despesas.stream()
                .map(this::toResponse)
                .toList();
    }

    public DespesaResponse buscarPorId(Long id) {
        return toResponse(buscarDespesaValidada(id));
    }

    public DespesaResponse criarDespesa(DespesaRequest despesaRequest) {
        Despesa despesa = toDespesa(null, despesaRequest);
        despesa.setUsuario(getUsuarioAutenticado());
        Despesa salva = despesaRepository.save(despesa);
        return toResponse(salva);
    }

    public DespesaResponse atualizarDespesa(Long id, DespesaRequest despesaRequest) {
        Despesa despesa = buscarDespesaValidada(id);
        despesa.setDescricao(despesaRequest.getDescricao());
        despesa.setValor(despesaRequest.getValor());
        despesa.setData(despesaRequest.getData());
        Categoria categoria = categoriaRepository.findById(despesaRequest.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));
        despesa.setCategoria(categoria);

        Despesa despesaAtualizada = despesaRepository.save(despesa);
        return toResponse(despesaAtualizada);
    }

    public void excluirDespesa(Long id) {
        Despesa despesa = buscarDespesaValidada(id);
        despesaRepository.deleteById(despesa.getId());
    }
}
