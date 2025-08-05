package com.restaurante.sistema.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int numero;

    @Enumerated(EnumType.STRING)
    private MesaStatus status;

    // NOVO CAMPO ADICIONADO
    @Enumerated(EnumType.STRING)
    private MesaLocalizacao localizacao;
}