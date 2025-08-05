package com.restaurante.sistema.service.impl;

import com.restaurante.sistema.model.*;
import com.restaurante.sistema.repository.*;
import com.restaurante.sistema.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ItemPedidoRepository itemPedidoRepository;
    @Autowired
    private ItemCardapioRepository itemCardapioRepository;
    @Autowired
    private IngredienteRepository ingredienteRepository;


    // MÉTODO ATUALIZADO: Lógica para buscar por período
    @Override
    public List<Pedido> buscarPedidosFechadosPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        // Garante que as datas não sejam nulas para evitar erros
        if (inicio == null || fim == null) {
            return new ArrayList<>(); // Retorna lista vazia se não houver período
        }

        return pedidoRepository.findAll().stream()
                .filter(p -> p.getStatus() == StatusPedido.FECHADO && p.getDataHoraFechamento() != null)
                .filter(p -> !p.getDataHoraFechamento().isBefore(inicio) && !p.getDataHoraFechamento().isAfter(fim))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Pedido adicionarItem(Long pedidoId, Long itemCardapioId, int quantidade) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + pedidoId));

        ItemCardapio itemCardapio = itemCardapioRepository.findById(itemCardapioId)
                .orElseThrow(() -> new EntityNotFoundException("Item do cardápio não encontrado com id: " + itemCardapioId));

        if (itemCardapio.getFichaTecnica() != null && !itemCardapio.getFichaTecnica().isEmpty()) {
            for (FichaTecnicaItem itemReceita : itemCardapio.getFichaTecnica()) {
                Ingrediente ingredienteDoEstoque = ingredienteRepository.findById(itemReceita.getIngrediente().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingrediente da receita não encontrado no estoque."));
                double quantidadeNecessaria = itemReceita.getQuantidade() * quantidade;
                if (ingredienteDoEstoque.getQuantidadeEmEstoque() < quantidadeNecessaria) {
                    throw new IllegalStateException("Estoque insuficiente para '" + ingredienteDoEstoque.getNome() + "'.");
                }
            }
            for (FichaTecnicaItem itemReceita : itemCardapio.getFichaTecnica()) {
                Ingrediente ingredienteParaAtualizar = ingredienteRepository.findById(itemReceita.getIngrediente().getId()).get();
                double quantidadeADeduzir = itemReceita.getQuantidade() * quantidade;
                double novoEstoque = ingredienteParaAtualizar.getQuantidadeEmEstoque() - quantidadeADeduzir;
                ingredienteParaAtualizar.setQuantidadeEmEstoque(novoEstoque);
                ingredienteRepository.save(ingredienteParaAtualizar);
            }
        }

        ItemPedido item = new ItemPedido();
        item.setPedido(pedido);
        item.setItemCardapio(itemCardapio);
        item.setQuantidade(quantidade);
        item.setPrecoUnitario(itemCardapio.getPrecoVenda());
        item.calcularSubtotal();

        itemPedidoRepository.save(item);
        pedido.getItens().add(item);
        recalcularValorTotal(pedido);

        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void removerItem(Long itemPedidoId) {
        ItemPedido itemParaRemover = itemPedidoRepository.findById(itemPedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Item de pedido não encontrado com id: " + itemPedidoId));

        Pedido pedido = itemParaRemover.getPedido();
        ItemCardapio itemCardapio = itemParaRemover.getItemCardapio();
        int quantidadeRemovida = itemParaRemover.getQuantidade();

        if (itemCardapio.getFichaTecnica() != null && !itemCardapio.getFichaTecnica().isEmpty()) {
            for (FichaTecnicaItem itemReceita : itemCardapio.getFichaTecnica()) {
                Ingrediente ingrediente = ingredienteRepository.findById(itemReceita.getIngrediente().getId()).get();
                double quantidadeADevolver = itemReceita.getQuantidade() * quantidadeRemovida;
                double novoEstoque = ingrediente.getQuantidadeEmEstoque() + quantidadeADevolver;
                ingrediente.setQuantidadeEmEstoque(novoEstoque);
                ingredienteRepository.save(ingrediente);
            }
        }

        pedido.getItens().remove(itemParaRemover);
        itemPedidoRepository.delete(itemParaRemover);

        if (pedido.getItens().isEmpty()) {
            Mesa mesa = pedido.getMesa();
            mesa.setStatus(MesaStatus.LIVRE);
            mesaRepository.save(mesa);
            pedidoRepository.delete(pedido);
        } else {
            recalcularValorTotal(pedido);
            pedidoRepository.save(pedido);
        }
    }

    @Override
    public Pedido abrirNovoPedido(Long mesaId, Long garcomId) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new EntityNotFoundException("Mesa não encontrada com id: " + mesaId));

        if (mesa.getStatus() == MesaStatus.OCUPADA) {
            throw new IllegalStateException("Mesa já está ocupada.");
        }

        Usuario garcom = usuarioRepository.findById(garcomId)
                .orElseThrow(() -> new EntityNotFoundException("Garçom não encontrado com id: " + garcomId));

        Pedido pedido = new Pedido();
        pedido.setMesa(mesa);
        pedido.setGarcom(garcom);
        pedido.setStatus(StatusPedido.ABERTO);
        pedido.setDataHoraAbertura(LocalDateTime.now());

        mesa.setStatus(MesaStatus.OCUPADA);
        mesaRepository.save(mesa);

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido buscarPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + pedidoId));
    }

    @Override
    public Pedido buscarPedidoAbertoPorMesa(Long mesaId) {
        return pedidoRepository.findByMesaIdAndStatusNot(mesaId, StatusPedido.FECHADO)
                .orElse(null);
    }

    @Override
    @Transactional
    public Pedido fecharPedido(Long pedidoId, FormaPagamento formaPagamento) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + pedidoId));

        if (pedido.getStatus() == StatusPedido.FECHADO) {
            throw new IllegalStateException("Este pedido já foi fechado.");
        }

        pedido.setFormaPagamento(formaPagamento);
        pedido.setStatus(StatusPedido.FECHADO);
        pedido.setDataHoraFechamento(LocalDateTime.now());

        Mesa mesa = pedido.getMesa();
        mesa.setStatus(MesaStatus.LIVRE);
        mesaRepository.save(mesa);

        return pedidoRepository.save(pedido);
    }

    @Override
    public BigDecimal calcularVendasDoDia() {
        LocalDateTime inicioDoDia = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime fimDoDia = LocalDateTime.now().with(LocalTime.MAX);

        BigDecimal total = pedidoRepository.sumValorTotalByStatusAndDataHoraFechamentoBetween(
                StatusPedido.FECHADO, inicioDoDia, fimDoDia);

        return Optional.ofNullable(total).orElse(BigDecimal.ZERO);
    }

    private void recalcularValorTotal(Pedido pedido) {
        BigDecimal total = pedido.getItens().stream()
                .map(ItemPedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        pedido.setValorTotal(total);
    }

    @Override
    public List<Pedido> buscarPedidosPorStatus(StatusPedido status) {
        return pedidoRepository.findAllByStatus(status);
    }

    @Override
    @Transactional
    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado com id: " + pedidoId));

        pedido.setStatus(novoStatus);
        return pedidoRepository.save(pedido);
    }

    @Override
    public Map<String, BigDecimal> calcularVendasUltimosSeteDias() {
        Map<String, BigDecimal> vendasPorDia = new LinkedHashMap<>();
        Locale localeBrasil = new Locale("pt", "BR");

        for (int i = 6; i >= 0; i--) {
            LocalDateTime dia = LocalDateTime.now().minusDays(i);
            LocalDateTime inicioDoDia = dia.with(LocalTime.MIN);
            LocalDateTime fimDoDia = dia.with(LocalTime.MAX);

            BigDecimal totalDoDia = pedidoRepository.sumValorTotalByStatusAndDataHoraFechamentoBetween(
                    StatusPedido.FECHADO, inicioDoDia, fimDoDia);

            String nomeDia = dia.getDayOfWeek().getDisplayName(TextStyle.SHORT, localeBrasil);

            vendasPorDia.put(nomeDia, Optional.ofNullable(totalDoDia).orElse(BigDecimal.ZERO));
        }
        return vendasPorDia;
    }
    // NOVA IMPLEMENTAÇÃO PARA O FECHAMENTO DE CAIXA
    @Override
    public List<Pedido> buscarPedidosFechadosHoje() {
        LocalDateTime inicioDoDia = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime fimDoDia = LocalDateTime.now().with(LocalTime.MAX);

        // Vamos buscar todos os pedidos e filtrar no Java para simplificar
        return pedidoRepository.findAll().stream()
                .filter(p -> p.getStatus() == StatusPedido.FECHADO)
                .filter(p -> p.getDataHoraFechamento() != null &&
                        p.getDataHoraFechamento().isAfter(inicioDoDia) &&
                        p.getDataHoraFechamento().isBefore(fimDoDia))
                .collect(Collectors.toList());
    }
}
