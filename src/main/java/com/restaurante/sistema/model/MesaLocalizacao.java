package com.restaurante.sistema.model;

public enum MesaLocalizacao {
    INTERNO("Ambiente Interno"),
    EXTERNO("Área Externa");

    private final String descricao;

    MesaLocalizacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}