package com.felipe.despesas.repository;

import com.felipe.despesas.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.felipe.despesas.model.Despesa;
import java.time.LocalDate;
import java.util.List;


@Repository
public interface DespesaRepository extends JpaRepository<Despesa, Long> {

    public Page<Despesa> findByUsuario(Usuario usuario, Pageable pageable);

    List<Despesa> findByUsuarioAndDataBetween(Usuario usuario, LocalDate inicio, LocalDate fim);
}
