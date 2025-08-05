package com.restaurante.sistema.service;

import com.restaurante.sistema.model.Ingrediente;
import java.util.List;

public interface IngredienteService {
    List<Ingrediente> listarTodos();
    Ingrediente salvar(Ingrediente ingrediente);
    Ingrediente buscarPorId(Long id);
    void excluir(Long id);

    // MÉTODOS RENOMEADOS
    long contarTotalDeIngredientes();
    long contarIngredientesComEstoqueBaixo(int limite);
}
