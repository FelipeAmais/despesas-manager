package com.felipe.despesas.controller;


import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import com.felipe.despesas.model.Despesa;
import com.felipe.despesas.services.DespesaService;


@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public List<Despesa> listarDespesas() {
        return despesaService.listarDespesas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Despesa> buscarPorId(@PathVariable Long id) {
        return despesaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Despesa criarDespesa(@RequestBody Despesa despesa) {
        return despesaService.criarDespesa(despesa);
    }

    @PutMapping
    public Despesa atualizarDespesa(@RequestBody Despesa despesa) {
        return despesaService.atualizarDespesa(despesa);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirDespesa(@PathVariable Long id) {
        despesaService.excluirDespesa(id);
    }
}
