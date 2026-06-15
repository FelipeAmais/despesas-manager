package com.felipe.despesas.controller;

import com.felipe.despesas.dto.DespesaRequest;
import com.felipe.despesas.dto.DespesaResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.felipe.despesas.services.DespesaService;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public Page<DespesaResponse> listarDespesas(Pageable pageable) {
        return despesaService.listarDespesas(pageable);
    }

    @GetMapping("/relatorio")
    public List<DespesaResponse> buscarPorPeriodo(@RequestParam LocalDate inicio, @RequestParam LocalDate fim) {
        return despesaService.listaPorPeriodo(inicio, fim);
    }

    @GetMapping("/{id}")
    public DespesaResponse buscarPorId(@PathVariable Long id) {
        return despesaService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaResponse criarDespesa(@Valid @RequestBody DespesaRequest despesaRequest) {
        return despesaService.criarDespesa(despesaRequest);
    }

    @PutMapping("/{id}")
    public DespesaResponse atualizarDespesa(@PathVariable Long id, @Valid @RequestBody DespesaRequest despesaRequest) {
        return despesaService.atualizarDespesa(id, despesaRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirDespesa(@PathVariable Long id) {
        despesaService.excluirDespesa(id);
    }
}
