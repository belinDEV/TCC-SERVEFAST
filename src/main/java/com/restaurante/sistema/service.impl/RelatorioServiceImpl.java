package com.restaurante.sistema.service.impl;

import com.restaurante.sistema.model.dto.ItemMaisVendidoDTO;
import com.restaurante.sistema.repository.ItemPedidoRepository;
import com.restaurante.sistema.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RelatorioServiceImpl implements RelatorioService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public List<ItemMaisVendidoDTO> getItensMaisVendidos(LocalDateTime dataInicio, LocalDateTime dataFim) {
        return itemPedidoRepository.findItensMaisVendidosPorPeriodo(dataInicio, dataFim);
    }
}
    