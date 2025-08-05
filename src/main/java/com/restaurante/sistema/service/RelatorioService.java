package com.restaurante.sistema.service;

import com.restaurante.sistema.model.dto.ItemMaisVendidoDTO;
import java.time.LocalDateTime;
import java.util.List;

public interface RelatorioService {
    List<ItemMaisVendidoDTO> getItensMaisVendidos(LocalDateTime dataInicio, LocalDateTime dataFim);
}
    