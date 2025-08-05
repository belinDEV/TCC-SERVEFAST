package com.restaurante.sistema.controller;

import com.restaurante.sistema.service.IngredienteService;
import com.restaurante.sistema.service.PedidoService; // Importar o PedidoService
import com.restaurante.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private UsuarioService usuarioService;

    // Adicionar a injeção do PedidoService
    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/")
    public String home(Model model) {
        // --- Lógica de Estoque e Usuários ---
        // Assumindo que você renomeou os métodos no seu IngredienteService
        long totalIngredientes = ingredienteService.contarTotalDeIngredientes();
        long estoqueBaixo = ingredienteService.contarIngredientesComEstoqueBaixo(10);
        long totalUsuarios = usuarioService.contarTotalDeUsuarios();

        // Envia os dados para a view
        model.addAttribute("totalProdutos", totalIngredientes); // O nome da variável no frontend continua "totalProdutos" por consistência
        model.addAttribute("totalEstoqueBaixo", estoqueBaixo);
        model.addAttribute("totalUsuarios", totalUsuarios);

        // --- NOVA LÓGICA PARA DADOS FINANCEIROS ---
        // Busca o total de vendas do dia para o card
        BigDecimal vendasDoDia = pedidoService.calcularVendasDoDia();
        model.addAttribute("vendasDoDia", vendasDoDia);

        // Busca os dados de vendas dos últimos 7 dias para o gráfico
        Map<String, BigDecimal> vendasSemana = pedidoService.calcularVendasUltimosSeteDias();

        // Separa os dados em duas listas (labels e valores) para o JavaScript do gráfico
        List<String> chartLabels = new ArrayList<>(vendasSemana.keySet());
        List<BigDecimal> chartData = new ArrayList<>(vendasSemana.values());

        model.addAttribute("chartLabels", chartLabels);
        model.addAttribute("chartData", chartData);

        return "index";
    }
}
