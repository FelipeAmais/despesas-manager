package com.felipe.despesas.services;

import com.felipe.despesas.exception.NotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import com.felipe.despesas.repository.CategoriaRepository;
import com.felipe.despesas.model.Categoria;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Transactional
    public Categoria criarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria atualizarCategoria(Long id, Categoria categoria) {
        if (!categoriaRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada");
        }
        categoria.setId(id);
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluirCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
