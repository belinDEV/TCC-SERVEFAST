package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.dto.ItemMaisVendidoDTO;
import com.restaurante.sistema.service.PdfService;
import com.restaurante.sistema.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @Autowired
    private PdfService pdfService; // Injetar o novo serviço

    @GetMapping
    public String paginaDeRelatorios(
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Model model) {

        LocalDateTime inicio = (dataInicio != null) ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = (dataFim != null) ? dataFim.atTime(LocalTime.MAX) : null;

        model.addAttribute("itensMaisVendidos", relatorioService.getItensMaisVendidos(inicio, fim));
        model.addAttribute("dataInicio", dataInicio);
        model.addAttribute("dataFim", dataFim);

        return "relatorios/index";
    }

    // NOVO ENDPOINT PARA GERAR O PDF
    @GetMapping("/pdf/itens-mais-vendidos")
    public ResponseEntity<InputStreamResource> exportarItensMaisVendidosPdf(
            @RequestParam(value = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(value = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        LocalDateTime inicio = (dataInicio != null) ? dataInicio.atStartOfDay() : null;
        LocalDateTime fim = (dataFim != null) ? dataFim.atTime(LocalTime.MAX) : null;

        List<ItemMaisVendidoDTO> itens = relatorioService.getItensMaisVendidos(inicio, fim);
        ByteArrayInputStream pdf = pdfService.gerarRelatorioItensMaisVendidos(itens, dataInicio, dataFim);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=relatorio_itens_mais_vendidos.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }
}
