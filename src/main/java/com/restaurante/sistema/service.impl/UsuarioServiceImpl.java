package com.restaurante.sistema.service.impl;

import com.restaurante.sistema.model.Usuario;
import com.restaurante.sistema.repository.UsuarioRepository;
import com.restaurante.sistema.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario salvar(Usuario usuario) {
        // Lógica importante: Só criptografa a senha se ela for nova ou alterada.
        // Se o campo senha vier vazio de um formulário de edição, mantém a senha antiga.
        if (usuario.getId() != null && (usuario.getPassword() == null || usuario.getPassword().isEmpty())) {
            usuarioRepository.findById(usuario.getId()).ifPresent(usuarioExistente -> {
                usuario.setPassword(usuarioExistente.getPassword());
            });
        } else {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public void excluir(Long id) {
        usuarioRepository.deleteById(id);
    }
    @Override
    public long contarTotalDeUsuarios() {
        return usuarioRepository.count();
    }
}