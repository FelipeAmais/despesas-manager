package com.felipe.despesas.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "categorias")
@Data
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String nome;

}
