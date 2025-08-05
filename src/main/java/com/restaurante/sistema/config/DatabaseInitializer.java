package com.restaurante.sistema.config;

import com.restaurante.sistema.model.Usuario;
import com.restaurante.sistema.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cria o usuário Administrador
        createDefaultUser("admin", "123", "ADMIN");

        // Cria o usuário Caixa
        createDefaultUser("caixa", "123", "CAIXA");

        // Cria o usuário Cozinha
        createDefaultUser("cozinha", "123", "COZINHA");
    }

    private void createDefaultUser(String username, String password, String role) {
        // Verifica se o usuário já existe no banco de dados
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            System.out.println("Criando usuário padrão: " + username);
            Usuario newUser = new Usuario();
            newUser.setUsername(username);
            newUser.setPassword(passwordEncoder.encode(password)); // Criptografa a senha
            newUser.setRole(role);
            usuarioRepository.save(newUser);
            System.out.println("Usuário " + username + " criado com sucesso!");
        }
    }
}