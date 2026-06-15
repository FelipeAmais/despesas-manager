package com.felipe.despesas.controller;


import java.util.List;
import com.felipe.despesas.dto.DespesaRequest;
import com.felipe.despesas.dto.DespesaResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.felipe.despesas.services.DespesaService;


@RestController
@RequestMapping("/despesas")
public class DespesaController {

    private final DespesaService despesaService;

    public DespesaController(DespesaService despesaService) {
        this.despesaService = despesaService;
    }

    @GetMapping
    public List<DespesaResponse> listarDespesas() {
        return despesaService.listarDespesas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaResponse> buscarPorId(@PathVariable Long id) {
        return despesaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
