package com.restaurante.sistema.service;

import com.restaurante.sistema.model.Usuario;
import java.util.List;

public interface UsuarioService {
    List<Usuario> listarTodos();
    Usuario salvar(Usuario usuario);
    Usuario buscarPorId(Long id);
    void excluir(Long id);
    long contarTotalDeUsuarios();
}