package com.felipe.despesas.services;

import java.time.LocalDate;
import java.util.List;
import com.felipe.despesas.dto.DespesaRequest;
import com.felipe.despesas.dto.DespesaResponse;
import com.felipe.despesas.exception.NotFoundException;
import com.felipe.despesas.mapper.DespesaMapper;
import com.felipe.despesas.model.Categoria;
import com.felipe.despesas.model.Usuario;
import com.felipe.despesas.repository.CategoriaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.felipe.despesas.model.Despesa;
import com.felipe.despesas.repository.DespesaRepository;
import org.springframework.transaction.annotation.Transactional;

import static com.felipe.despesas.mapper.DespesaMapper.toDespesa;
import static com.felipe.despesas.mapper.DespesaMapper.toResponse;


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

    private  Despesa buscarDespesaValidada(Long id){
        return despesaRepository.findById(id).filter(despesa -> despesa.getUsuario().getId().equals(getUsuarioAutenticado().getId()))
                .orElseThrow(() -> new NotFoundException("Despesa inexistente"));
    }

    @Transactional(readOnly = true)
    public Page<DespesaResponse> listarDespesas(Pageable pageable) {
        Page<Despesa> despesas = despesaRepository.findByUsuario(getUsuarioAutenticado(), pageable);
        return despesas.map(DespesaMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DespesaResponse> listaPorPeriodo(LocalDate inicio, LocalDate fim) {
        List<Despesa> despesas = despesaRepository.findByUsuarioAndDataBetween(getUsuarioAutenticado(), inicio, fim);
        return despesas.stream()
                .map(DespesaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public DespesaResponse buscarPorId(Long id) {
        return toResponse(buscarDespesaValidada(id));
    }

    @Transactional
    public DespesaResponse criarDespesa(DespesaRequest despesaRequest) {
        Categoria categoria = categoriaRepository.findById(despesaRequest.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria inexistente"));

        Despesa despesa = toDespesa(null, despesaRequest, categoria);
        despesa.setUsuario(getUsuarioAutenticado());

        Despesa salva = despesaRepository.save(despesa);

        return toResponse(salva);
    }

    @Transactional
    public DespesaResponse atualizarDespesa(Long id, DespesaRequest despesaRequest) {
        Despesa despesa = buscarDespesaValidada(id);
        despesa.setDescricao(despesaRequest.getDescricao());
        despesa.setValor(despesaRequest.getValor());
        despesa.setData(despesaRequest.getData());
        Categoria categoria = categoriaRepository.findById(despesaRequest.getCategoriaId())
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
        despesa.setCategoria(categoria);

        Despesa despesaAtualizada = despesaRepository.save(despesa);
        return toResponse(despesaAtualizada);
    }

    @Transactional
    public void excluirDespesa(Long id) {
        Despesa despesa = buscarDespesaValidada(id);
        despesaRepository.deleteById(despesa.getId());
    }
}
