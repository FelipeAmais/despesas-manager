package com.felipe.despesas.controller;

import java.util.List;

import com.felipe.despesas.dto.CategoriaRequest;
import com.felipe.despesas.dto.CategoriaResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import com.felipe.despesas.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaResponse> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponse criarCategoria(@RequestBody CategoriaRequest categoriaRequest) {
        return categoriaService.criarCategoria(categoriaRequest);
    }

    @PutMapping("/{id}")
    public CategoriaResponse atualizarCategoria(@PathVariable Long id, @RequestBody CategoriaRequest categoriaRequest) {
        return categoriaService.atualizarCategoria(id, categoriaRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirCategoria(@PathVariable Long id) {
        categoriaService.excluirCategoria(id);
    }
}
