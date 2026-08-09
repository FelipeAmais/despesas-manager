package com.felipe.despesas.services;

import com.felipe.despesas.dto.CategoriaRequest;
import com.felipe.despesas.dto.CategoriaResponse;
import com.felipe.despesas.exception.NotFoundException;
import com.felipe.despesas.model.Categoria;
import com.felipe.despesas.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    private Categoria categoria;

    @InjectMocks
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        this.categoria = new Categoria();
        categoria.setId(1L);
        categoria.setNome("Categoria 1");
    }

    @Test
    void listarCategorias() {
        List<Categoria> categorias =  List.of(categoria);

        when(categoriaRepository.findAll()).thenReturn(categorias);
        List<CategoriaResponse> response = categoriaService.listarCategorias();

        assertEquals(1, response.size());
    }

    @Test
    void criarCategoria() {
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        CategoriaRequest request = new CategoriaRequest("Categoria 1");
        CategoriaResponse response = categoriaService.criarCategoria(request);

        assertEquals("Categoria 1", response.getNome());
    }

    @Test
    void atualizarCategoria() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);
        CategoriaRequest request = new CategoriaRequest("Categoria 1");

        CategoriaResponse response = categoriaService.atualizarCategoria(1L, request);

        assertEquals("Categoria 1", response.getNome());
    }

    @Test
    void atualizarCategoriaIdNaoEncontrado() {
        when(categoriaRepository.existsById(1L)).thenReturn(false);
        CategoriaRequest request = new CategoriaRequest("Categoria 1");

        assertThrows(NotFoundException.class, () ->
                categoriaService.atualizarCategoria(1L, request));
    }

    @Test
    void excluirCategoria() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);
        categoriaService.excluirCategoria(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void excluirCategoriaIdNaoEncontrado() {
        when(categoriaRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () ->
                categoriaService.excluirCategoria(1L));
    }
}
