package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.FormaPagamento;
import com.restaurante.sistema.model.Pedido;
import com.restaurante.sistema.service.PdfService;
import com.restaurante.sistema.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/caixa")
public class CaixaController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PdfService pdfService;

    @GetMapping("/fechamento")
    public String fechamentoDeCaixa(
            @RequestParam(value = "periodo", defaultValue = "hoje") String periodo,
            Model model) {

        LocalDateTime inicio;
        LocalDateTime fim = LocalDateTime.now();

        switch (periodo) {
            case "semana":
                inicio = fim.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
                break;
            case "mes":
                inicio = fim.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                break;
            default: // "hoje"
                inicio = fim.with(LocalTime.MIN);
                break;
        }

        List<Pedido> pedidosFechados = pedidoService.buscarPedidosFechadosPorPeriodo(inicio, fim);

        BigDecimal totalGeral = pedidosFechados.stream().map(Pedido::getValorTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        Map<FormaPagamento, BigDecimal> totaisPorFormaPagamento = pedidosFechados.stream()
                .filter(p -> p.getFormaPagamento() != null)
                .collect(Collectors.groupingBy(Pedido::getFormaPagamento, Collectors.reducing(BigDecimal.ZERO, Pedido::getValorTotal, BigDecimal::add)));

        model.addAttribute("pedidos", pedidosFechados);
        model.addAttribute("totalGeral", totalGeral);
        model.addAttribute("totaisPorFormaPagamento", totaisPorFormaPagamento);
        model.addAttribute("formasPagamento", FormaPagamento.values());
        model.addAttribute("periodoSelecionado", periodo);

        return "caixa/fechamento";
    }

    @GetMapping("/fechamento/pdf")
    public ResponseEntity<InputStreamResource> exportarFechamentoPdf(@RequestParam(value = "periodo", defaultValue = "hoje") String periodo) {
        LocalDateTime inicio;
        LocalDateTime fim = LocalDateTime.now();
        switch (periodo) {
            case "semana":
                inicio = fim.with(DayOfWeek.MONDAY).with(LocalTime.MIN);
                break;
            case "mes":
                inicio = fim.with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                break;
            default:
                inicio = fim.with(LocalTime.MIN);
                break;
        }

        List<Pedido> pedidos = pedidoService.buscarPedidosFechadosPorPeriodo(inicio, fim);

        // CHAMA O NOVO MÉTODO PARA GERAR O PDF CORRETO
        ByteArrayInputStream pdf = pdfService.gerarRelatorioFechamentoCaixa(pedidos, periodo);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=fechamento_caixa_" + periodo + ".pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(pdf));
    }
}
