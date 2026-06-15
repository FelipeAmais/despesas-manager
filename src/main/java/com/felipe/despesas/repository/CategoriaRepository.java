package com.felipe.despesas.repository;

import com.felipe.despesas.model.Categoria;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    public Optional<Categoria> findByNome(String nome);
} 
