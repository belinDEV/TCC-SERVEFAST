package com.restaurante.sistema.model.dto;

public class ItemMaisVendidoDTO {
    private String nomeItem;
    private Long quantidadeVendida;

    public ItemMaisVendidoDTO(String nomeItem, Long quantidadeVendida) {
        this.nomeItem = nomeItem;
        this.quantidadeVendida = quantidadeVendida;
    }

    // Getters e Setters
    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public Long getQuantidadeVendida() {
        return quantidadeVendida;
    }

    public void setQuantidadeVendida(Long quantidadeVendida) {
        this.quantidadeVendida = quantidadeVendida;
    }
}
