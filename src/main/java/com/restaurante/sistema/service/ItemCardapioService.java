package com.restaurante.sistema.service;

import com.restaurante.sistema.model.ItemCardapio;
import java.util.List;

public interface ItemCardapioService {
    ItemCardapio salvar(ItemCardapio itemCardapio);
    List<ItemCardapio> listarTodos();
    ItemCardapio buscarPorId(Long id);
    void excluir(Long id);
}
    