package com.restaurante.sistema.service.impl;

import com.restaurante.sistema.model.Mesa;
import com.restaurante.sistema.repository.MesaRepository;
import com.restaurante.sistema.service.MesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaServiceImpl implements MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    @Override
    public List<Mesa> listarTodas() {
        return mesaRepository.findAll();
    }

    @Override
    public Mesa salvar(Mesa mesa) {
        return mesaRepository.save(mesa);
    }

    @Override
    public Mesa buscarPorId(Long id) {
        return mesaRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        mesaRepository.deleteById(id);
    }
}