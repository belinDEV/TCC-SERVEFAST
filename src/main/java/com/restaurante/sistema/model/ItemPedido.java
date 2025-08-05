package com.restaurante.sistema.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    // MUDANÇA AQUI: Agora se refere a um ItemCardapio
    @ManyToOne
    @JoinColumn(name = "item_cardapio_id", nullable = false)
    private ItemCardapio itemCardapio;

    private int quantidade;

    private BigDecimal precoUnitario; // Preço do item do cardápio no momento da venda

    private BigDecimal subtotal;

    public void calcularSubtotal() {
        if (precoUnitario != null && quantidade > 0) {
            this.subtotal = precoUnitario.multiply(new BigDecimal(quantidade));
        }
    }
}
