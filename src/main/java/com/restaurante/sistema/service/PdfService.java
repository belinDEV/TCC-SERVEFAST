package com.restaurante.sistema.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.restaurante.sistema.model.FormaPagamento;
import com.restaurante.sistema.model.Pedido;
import com.restaurante.sistema.model.dto.ItemMaisVendidoDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PdfService {

    public ByteArrayInputStream gerarRelatorioItensMaisVendidos(List<ItemMaisVendidoDTO> itens, LocalDate dataInicio, LocalDate dataFim) {
        // ... (código existente, sem alterações)
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Relatório de Itens Mais Vendidos", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String periodoStr = "Período: ";
            if (dataInicio != null && dataFim != null) {
                periodoStr += dataInicio.format(formatter) + " a " + dataFim.format(formatter);
            } else {
                periodoStr += "Completo";
            }
            Paragraph periodo = new Paragraph(periodoStr);
            periodo.setAlignment(Element.ALIGN_CENTER);
            periodo.setSpacingAfter(20);
            document.add(periodo);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{1, 6, 3});

            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            PdfPCell headerCell;

            headerCell = new PdfPCell(new Phrase("Pos.", fontHeader));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Nome do Item", fontHeader));
            table.addCell(headerCell);

            headerCell = new PdfPCell(new Phrase("Qtd. Vendida", fontHeader));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);

            int pos = 1;
            for (ItemMaisVendidoDTO item : itens) {
                PdfPCell cell;

                cell = new PdfPCell(new Phrase(String.valueOf(pos++)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                table.addCell(item.getNomeItem());

                cell = new PdfPCell(new Phrase(String.valueOf(item.getQuantidadeVendida())));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    // NOVO MÉTODO PARA GERAR O PDF DO FECHO DE CAIXA
    public ByteArrayInputStream gerarRelatorioFechamentoCaixa(List<Pedido> pedidos, String periodo) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Relatório de Fechamento de Caixa", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            // Período
            String periodoStr = "Período: " + periodo.substring(0, 1).toUpperCase() + periodo.substring(1);
            Paragraph pPeriodo = new Paragraph(periodoStr);
            pPeriodo.setAlignment(Element.ALIGN_CENTER);
            pPeriodo.setSpacingAfter(20);
            document.add(pPeriodo);

            // Tabela de Resumo Financeiro
            document.add(new Paragraph("Resumo Financeiro", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(Chunk.NEWLINE);

            PdfPTable resumoTable = new PdfPTable(2);
            resumoTable.setWidthPercentage(50);
            resumoTable.setHorizontalAlignment(Element.ALIGN_LEFT);

            Map<FormaPagamento, BigDecimal> totaisPorForma = pedidos.stream()
                    .filter(p -> p.getFormaPagamento() != null)
                    .collect(Collectors.groupingBy(Pedido::getFormaPagamento,
                            Collectors.reducing(BigDecimal.ZERO, Pedido::getValorTotal, BigDecimal::add)));

            for (FormaPagamento fp : FormaPagamento.values()) {
                resumoTable.addCell(fp.getDescricao());
                resumoTable.addCell(String.format("R$ %.2f", totaisPorForma.getOrDefault(fp, BigDecimal.ZERO)));
            }

            BigDecimal totalGeral = totaisPorForma.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            PdfPCell totalCellLabel = new PdfPCell(new Phrase("TOTAL GERAL", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            resumoTable.addCell(totalCellLabel);
            PdfPCell totalCellValue = new PdfPCell(new Phrase(String.format("R$ %.2f", totalGeral), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            resumoTable.addCell(totalCellValue);

            document.add(resumoTable);
            document.add(Chunk.NEWLINE);

            // Tabela de Histórico de Pedidos
            document.add(new Paragraph("Histórico de Pedidos", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            document.add(Chunk.NEWLINE);

            PdfPTable pedidosTable = new PdfPTable(5);
            pedidosTable.setWidthPercentage(100);
            pedidosTable.setWidths(new float[]{1, 1, 2, 2, 2});

            // Cabeçalho
            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
            pedidosTable.addCell(new PdfPCell(new Phrase("ID", fontHeader)));
            pedidosTable.addCell(new PdfPCell(new Phrase("Mesa", fontHeader)));
            pedidosTable.addCell(new PdfPCell(new Phrase("Hora Fecho.", fontHeader)));
            pedidosTable.addCell(new PdfPCell(new Phrase("Forma Pag.", fontHeader)));
            pedidosTable.addCell(new PdfPCell(new Phrase("Valor", fontHeader)));

            // Corpo
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
            for (Pedido pedido : pedidos) {
                pedidosTable.addCell(String.valueOf(pedido.getId()));
                pedidosTable.addCell(String.valueOf(pedido.getMesa().getNumero()));
                pedidosTable.addCell(pedido.getDataHoraFechamento().format(timeFormatter));
                pedidosTable.addCell(pedido.getFormaPagamento().getDescricao());
                pedidosTable.addCell(String.format("R$ %.2f", pedido.getValorTotal()));
            }

            document.add(pedidosTable);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
