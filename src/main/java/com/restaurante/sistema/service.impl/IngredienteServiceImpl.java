package com.restaurante.sistema.service.impl;

import com.restaurante.sistema.model.Ingrediente;
import com.restaurante.sistema.repository.IngredienteRepository;
import com.restaurante.sistema.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IngredienteServiceImpl implements IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Override
    public List<Ingrediente> listarTodos() {
        return ingredienteRepository.findAll();
    }

    @Override
    public Ingrediente salvar(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    @Override
    public Ingrediente buscarPorId(Long id) {
        return ingredienteRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        ingredienteRepository.deleteById(id);
    }

    // ======================================================
    //      MÉTODOS FALTANTES ADICIONADOS AQUI
    // ======================================================
    @Override
    public long contarTotalDeIngredientes() {
        return ingredienteRepository.count();
    }

    @Override
    public long contarIngredientesComEstoqueBaixo(int limite) {
        return ingredienteRepository.contarEstoqueBaixo(limite);
    }
}
