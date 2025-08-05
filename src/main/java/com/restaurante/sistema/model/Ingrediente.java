package com.restaurante.sistema.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;


@Data
@Entity
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    // Alterado para double para aceitar valores fracionados (ex: 0.5 kg)
    private double quantidadeEmEstoque;

    // NOVO CAMPO ADICIONADO
    @Enumerated(EnumType.STRING)
    private UnidadeMedida unidadeMedida;
}