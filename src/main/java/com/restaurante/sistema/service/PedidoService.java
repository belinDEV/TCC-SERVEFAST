package com.restaurante.sistema.service;

import com.restaurante.sistema.model.FormaPagamento;
import com.restaurante.sistema.model.Pedido;
import com.restaurante.sistema.model.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface PedidoService {

    Pedido abrirNovoPedido(Long mesaId, Long garcomId);

    Pedido adicionarItem(Long pedidoId, Long itemCardapioId, int quantidade);

    void removerItem(Long itemPedidoId);

    Pedido buscarPorId(Long pedidoId);

    Pedido buscarPedidoAbertoPorMesa(Long mesaId);

    // Assinatura correta para fechar o pedido com pagamento
    Pedido fecharPedido(Long pedidoId, FormaPagamento formaPagamento);

    BigDecimal calcularVendasDoDia();

    Map<String, BigDecimal> calcularVendasUltimosSeteDias();

    // Métodos para a cozinha
    List<Pedido> buscarPedidosPorStatus(StatusPedido status);
    Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus);
    List<Pedido> buscarPedidosFechadosHoje();
    List<Pedido> buscarPedidosFechadosPorPeriodo(LocalDateTime inicio, LocalDateTime fim);
}
