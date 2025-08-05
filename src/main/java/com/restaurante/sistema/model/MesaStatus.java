package com.restaurante.sistema.model;

public enum MesaStatus {
    LIVRE("Livre"),
    OCUPADA("Ocupada"),
    RESERVADA("Reservada");

    private final String descricao;

    MesaStatus(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}