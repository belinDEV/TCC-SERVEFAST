package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.Ingrediente;
import com.restaurante.sistema.model.UnidadeMedida;
import com.restaurante.sistema.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ingredientes") // A rota base também deve ser atualizada
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @GetMapping
    public String listarIngredientes(Model model) {
        model.addAttribute("ingredientes", ingredienteService.listarTodos());
        // CORREÇÃO AQUI
        return "ingrediente/listar";
    }

    @GetMapping("/novo")
    public String novoIngredienteForm(Model model) {
        model.addAttribute("ingrediente", new Ingrediente());
        model.addAttribute("unidades", UnidadeMedida.values());
        // CORREÇÃO AQUI
        return "ingrediente/form";
    }

    @PostMapping("/salvar")
    public String salvarIngrediente(@ModelAttribute("ingrediente") Ingrediente ingrediente) {
        ingredienteService.salvar(ingrediente);
        return "redirect:/ingredientes";
    }

    @GetMapping("/editar/{id}")
    public String editarIngredienteForm(@PathVariable Long id, Model model) {
        model.addAttribute("ingrediente", ingredienteService.buscarPorId(id));
        model.addAttribute("unidades", UnidadeMedida.values());
        // CORREÇÃO AQUI
        return "ingrediente/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirIngrediente(@PathVariable Long id) {
        ingredienteService.excluir(id);
        return "redirect:/ingredientes";
    }
}
