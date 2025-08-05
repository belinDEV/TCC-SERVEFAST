package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.ItemCardapio;
import com.restaurante.sistema.service.IngredienteService;
import com.restaurante.sistema.service.ItemCardapioService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cardapio")
public class ItemCardapioController {

    @Autowired
    private ItemCardapioService itemCardapioService;

    @Autowired
    private IngredienteService ingredienteService;

    // ... métodos listar, novo, editar, excluir ...

    @PostMapping("/salvar")
    public String salvarItem(@ModelAttribute("itemCardapio") ItemCardapio itemCardapio, Model model) {
        try {
            itemCardapioService.salvar(itemCardapio);
            return "redirect:/cardapio";
        } catch (IllegalStateException | EntityNotFoundException e) {
            // Se ocorrer um erro de validação (ou outro erro)
            // Adiciona a mensagem de erro ao modelo
            model.addAttribute("errorMessage", e.getMessage());
            // Adiciona novamente os ingredientes para o dropdown funcionar
            model.addAttribute("ingredientes", ingredienteService.listarTodos());
            // Retorna para a mesma página do formulário, mantendo os dados que o usuário digitou
            return "cardapio/form";
        }
    }

    // O restante dos seus métodos (listar, novo, editar, excluir) continua aqui...
    @GetMapping
    public String listarItensCardapio(Model model) {
        model.addAttribute("itensCardapio", itemCardapioService.listarTodos());
        return "cardapio/listar";
    }

    @GetMapping("/novo")
    public String novoItemForm(Model model) {
        model.addAttribute("itemCardapio", new ItemCardapio());
        model.addAttribute("ingredientes", ingredienteService.listarTodos());
        return "cardapio/form";
    }

    @GetMapping("/editar/{id}")
    public String editarItemForm(@PathVariable Long id, Model model) {
        model.addAttribute("itemCardapio", itemCardapioService.buscarPorId(id));
        model.addAttribute("ingredientes", ingredienteService.listarTodos());
        return "cardapio/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirItem(@PathVariable Long id) {
        itemCardapioService.excluir(id);
        return "redirect:/cardapio";
    }
}
