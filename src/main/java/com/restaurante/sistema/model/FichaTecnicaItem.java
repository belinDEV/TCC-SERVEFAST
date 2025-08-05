package com.restaurante.sistema.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class FichaTecnicaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_cardapio_id")
    private ItemCardapio itemCardapio;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente;

    // Quantidade do ingrediente necessária para fazer 1 unidade do item do cardápio
    // Ex: 0.15 para 150g de queijo
    private double quantidade;
}
