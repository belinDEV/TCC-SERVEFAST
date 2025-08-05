package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.FormaPagamento;
import com.restaurante.sistema.service.ItemCardapioService;
import com.restaurante.sistema.service.PedidoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Importar

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ItemCardapioService itemCardapioService;

    @GetMapping("/{id}")
    public String detalhesPedido(@PathVariable Long id, Model model) {
        model.addAttribute("pedido", pedidoService.buscarPorId(id));
        model.addAttribute("itensCardapio", itemCardapioService.listarTodos());
        // Envia a lista de formas de pagamento para o modal
        model.addAttribute("formasPagamento", FormaPagamento.values());
        return "pedido/detalhes";
    }

    @PostMapping("/{pedidoId}/adicionar-item")
    public String adicionarItem(@PathVariable Long pedidoId,
                                @RequestParam Long itemCardapioId,
                                @RequestParam int quantidade,
                                RedirectAttributes redirectAttributes) { // Adicionar RedirectAttributes
        try {
            pedidoService.adicionarItem(pedidoId, itemCardapioId, quantidade);
        } catch (IllegalStateException | EntityNotFoundException e) {
            // Se ocorrer um erro (como estoque insuficiente), envia a mensagem de erro para a página
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/pedidos/" + pedidoId;
    }

    // O restante dos seus métodos (removerItem, fechar) continua aqui...
    @GetMapping("/remover-item/{itemId}")
    public String removerItem(@PathVariable Long itemId) {
        // FIXME: Melhorar este redirecionamento
        pedidoService.removerItem(itemId);
        return "redirect:/mesas";
    }

    // MÉTODO "FECHAR" ATUALIZADO PARA RECEBER O POST DO FORMULÁRIO
    @PostMapping("/{pedidoId}/fechar")
    public String fecharPedido(@PathVariable Long pedidoId, @RequestParam FormaPagamento formaPagamento) {
        pedidoService.fecharPedido(pedidoId, formaPagamento); // O service também será atualizado
        return "redirect:/mesas";
    }
}
