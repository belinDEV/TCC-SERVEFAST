package com.restaurante.sistema.model;

public enum UnidadeMedida {
    UNIDADE("Unidade(s)", "un"),
    GRAMA("Grama(s)", "g"),
    QUILOGRAMA("Quilograma(s)", "kg"),
    LITRO("Litro(s)", "L"),
    MILILITRO("Mililitro(s)", "ml");

    private final String descricao;
    private final String abreviacao;

    UnidadeMedida(String descricao, String abreviacao) {
        this.descricao = descricao;
        this.abreviacao = abreviacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getAbreviacao() {
        return abreviacao;
    }
}