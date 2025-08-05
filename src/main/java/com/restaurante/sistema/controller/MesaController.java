package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.Mesa;
import com.restaurante.sistema.model.MesaLocalizacao;
import com.restaurante.sistema.model.MesaStatus;
import com.restaurante.sistema.model.Pedido;
import com.restaurante.sistema.service.MesaService;
import com.restaurante.sistema.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MesaController {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private PedidoService pedidoService;

    // ROTA PARA O PAINEL VISUAL DE ATENDIMENTO
    @GetMapping("/mesas")
    public String painelDeMesas(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        // CORREÇÃO AQUI: Deve retornar a página de atendimento visual
        return "mesa/listar";
    }

    // ROTA PARA A LISTA DE GERENCIAMENTO DO ADMIN
    @GetMapping("/admin/mesas")
    public String listarMesasParaAdmin(Model model) {
        model.addAttribute("mesas", mesaService.listarTodas());
        // Esta rota está correta, retorna a página de admin
        return "admin/mesa/listar";
    }

    @GetMapping("/mesas/novo")
    public String novaMesaForm(Model model) {
        model.addAttribute("mesa", new Mesa());
        model.addAttribute("statuses", MesaStatus.values());
        model.addAttribute("localizacoes", MesaLocalizacao.values());
        return "mesa/form";
    }

    @PostMapping("/mesas/salvar")
    public String salvarMesa(@ModelAttribute("mesa") Mesa mesa) {
        mesaService.salvar(mesa);
        return "redirect:/admin/mesas"; // Redireciona para a lista do admin
    }

    @GetMapping("/mesas/editar/{id}")
    public String editarMesaForm(@PathVariable Long id, Model model) {
        model.addAttribute("mesa", mesaService.buscarPorId(id));
        model.addAttribute("statuses", MesaStatus.values());
        model.addAttribute("localizacoes", MesaLocalizacao.values());
        return "mesa/form";
    }

    @GetMapping("/mesas/excluir/{id}")
    public String excluirMesa(@PathVariable Long id) {
        mesaService.excluir(id);
        return "redirect:/admin/mesas"; // Redireciona para a lista do admin
    }

    @GetMapping("/mesas/atender/{id}")
    public String atenderMesa(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Pedido pedido = pedidoService.buscarPedidoAbertoPorMesa(id);

        if (pedido == null) {
            // TODO: A lógica para pegar o ID do garçom logado será melhorada aqui.
            Long garcomId = 1L; // Usando o admin (ID=1) como padrão por enquanto
            pedido = pedidoService.abrirNovoPedido(id, garcomId);
        }

        return "redirect:/pedidos/" + pedido.getId();
    }
}
