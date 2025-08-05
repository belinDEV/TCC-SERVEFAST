package com.restaurante.sistema.service;

import com.restaurante.sistema.model.FichaTecnicaItem;
import com.restaurante.sistema.model.Ingrediente;
import com.restaurante.sistema.model.ItemCardapio;
import com.restaurante.sistema.repository.IngredienteRepository;
import com.restaurante.sistema.repository.ItemCardapioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemCardapioServiceImpl implements ItemCardapioService {

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    // Precisamos do repositório de ingredientes para verificar o estoque
    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Override
    @Transactional
    public ItemCardapio salvar(ItemCardapio itemCardapio) {
        // Garante que cada item da ficha técnica tenha a referência correta ao item do cardápio pai
        if (itemCardapio.getFichaTecnica() != null) {
            for (FichaTecnicaItem itemReceita : itemCardapio.getFichaTecnica()) {
                // Busca o ingrediente do banco para ter a informação de estoque mais atual
                Ingrediente ingrediente = ingredienteRepository.findById(itemReceita.getIngrediente().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Ingrediente não encontrado: " + itemReceita.getIngrediente().getId()));

                // A LÓGICA DE VALIDAÇÃO ACONTECE AQUI!
                if (itemReceita.getQuantidade() > ingrediente.getQuantidadeEmEstoque()) {
                    // Lança uma exceção se a quantidade necessária for maior que o estoque
                    throw new IllegalStateException(
                            "A quantidade necessária de '" + ingrediente.getNome() +
                                    "' (" + itemReceita.getQuantidade() + " " + ingrediente.getUnidadeMedida().getAbreviacao() +
                                    ") é maior que o estoque disponível (" + ingrediente.getQuantidadeEmEstoque() + " " + ingrediente.getUnidadeMedida().getAbreviacao() + ")."
                    );
                }

                itemReceita.setItemCardapio(itemCardapio);
            }
        }
        return itemCardapioRepository.save(itemCardapio);
    }

    @Override
    public List<ItemCardapio> listarTodos() {
        return itemCardapioRepository.findAll();
    }

    @Override
    public ItemCardapio buscarPorId(Long id) {
        return itemCardapioRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        itemCardapioRepository.deleteById(id);
    }
}
