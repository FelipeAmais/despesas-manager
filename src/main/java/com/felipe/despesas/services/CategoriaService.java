package com.felipe.despesas.services;

import com.felipe.despesas.dto.CategoriaRequest;
import com.felipe.despesas.dto.CategoriaResponse;
import com.felipe.despesas.exception.NotFoundException;
import com.felipe.despesas.mapper.CategoriaMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import com.felipe.despesas.repository.CategoriaRepository;
import com.felipe.despesas.model.Categoria;
import org.springframework.transaction.annotation.Transactional;

import static com.felipe.despesas.mapper.CategoriaMapper.toResponse;


@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        return categorias.stream()
                .map(CategoriaMapper::toResponse)
                .toList();
    }

    @Transactional
    public CategoriaResponse criarCategoria(CategoriaRequest categoriaRequest) {
        Categoria categoria = new Categoria();
        categoria.setNome(categoriaRequest.getNome());
        Categoria salva = categoriaRepository.save(categoria);
        return toResponse(salva);
    }

    @Transactional
    public CategoriaResponse atualizarCategoria(Long id, CategoriaRequest categoriaRequest) {
        if (!categoriaRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada");
        }

        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(categoriaRequest.getNome());
        Categoria salva = categoriaRepository.save(categoria);
        return toResponse(salva);
    }

    @Transactional
    public void excluirCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new NotFoundException("Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
