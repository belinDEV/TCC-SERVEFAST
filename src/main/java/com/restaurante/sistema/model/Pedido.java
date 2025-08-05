package com.restaurante.sistema.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario garcom;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraFechamento;

    private BigDecimal valorTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private FormaPagamento formaPagamento;

    // A DECLARAÇÃO DUPLICADA FOI REMOVIDA. ESTA É A VERSÃO CORRETA.
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ItemPedido> itens = new ArrayList<>();
}
