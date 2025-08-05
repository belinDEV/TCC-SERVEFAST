package com.restaurante.sistema.controller;

import com.restaurante.sistema.model.Usuario;
import com.restaurante.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "usuario/listar";
    }

    @GetMapping("/novo")
    public String novoUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/form";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute("usuario") Usuario usuario) {
        usuarioService.salvar(usuario);
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuarioForm(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", usuarioService.buscarPorId(id));
        return "usuario/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Long id) {
        // Adicionar uma verificação para não deixar o ADM se auto-excluir
        // (Isso pode ser melhorado depois)
        usuarioService.excluir(id);
        return "redirect:/usuarios";
    }
}