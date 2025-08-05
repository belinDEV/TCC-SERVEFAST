package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.StatusPedido;
import com.restaurante.sistema.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cozinha")
public class CozinhaController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/painel")
    public String painelCozinha(Model model) {
        // Busca os pedidos e os separa por status para enviar à view
        model.addAttribute("pedidosAFazer", pedidoService.buscarPedidosPorStatus(StatusPedido.ABERTO));
        model.addAttribute("pedidosEmPreparo", pedidoService.buscarPedidosPorStatus(StatusPedido.EM_PREPARACAO));
        model.addAttribute("pedidosProntos", pedidoService.buscarPedidosPorStatus(StatusPedido.ENTREGUE));
        return "cozinha/painel";
    }

    // Endpoint para o botão "Iniciar Preparo"
    @PostMapping("/pedido/{id}/iniciar-preparo")
    public String iniciarPreparo(@PathVariable("id") Long pedidoId) {
        pedidoService.atualizarStatus(pedidoId, StatusPedido.EM_PREPARACAO);
        return "redirect:/cozinha/painel";
    }

    // Endpoint para o botão "Marcar como Pronto"
    @PostMapping("/pedido/{id}/marcar-pronto")
    public String marcarPronto(@PathVariable("id") Long pedidoId) {
        pedidoService.atualizarStatus(pedidoId, StatusPedido.ENTREGUE);
        return "redirect:/cozinha/painel";
    }
}
