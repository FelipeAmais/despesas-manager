package com.felipe.despesas.repository;

import com.felipe.despesas.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipe.despesas.model.Despesa;

import java.util.List;

@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    public List<Despesa> findByUsuario(Usuario usuario);
}
