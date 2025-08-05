package com.restaurante.sistema.service;

import com.restaurante.sistema.model.Mesa;
import java.util.List;

public interface MesaService {
    List<Mesa> listarTodas();
    Mesa salvar(Mesa mesa);
    Mesa buscarPorId(Long id);
    void excluir(Long id);
}