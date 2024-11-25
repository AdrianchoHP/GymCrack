package com.GymCrack.app.controller;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.GymCrack.app.entity.Entrenador;
import com.GymCrack.app.entity.Usuario;
import com.GymCrack.app.repository.EntrenadorRepository;
import com.GymCrack.app.repository.UsuarioRepository;

@Controller
public class LoginWebController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;
    
    // Maneja solicitudes GET para mostrar la página de inicio de sesión
    @GetMapping("/login")
    public String mostrarFormularioLogin() {
        return "login"; // Renderiza la plantilla login.html
    }

    // Maneja solicitudes POST para procesar el inicio de sesión
    @PostMapping("/login")
    public String login(HttpSession session, String username, String password) {
        // Busca en la colección de usuarios
        Optional<Usuario> usuario = usuarioRepository.findByEmailAndPassword(username, password);
        if (usuario.isPresent()) {
            String rol = usuario.get().getRol();
            session.setAttribute("rol", rol);
            session.setAttribute("usuario", usuario.get());
            return switch (rol) {
                case "Administrador" -> "redirect:/dashboard/admin";
                case "Miembro" -> "redirect:/dashboard/miembro";
                default -> "redirect:/login?error=unknownRole";
            };
        }

        // Busca en la colección de entrenadores
        Optional<Entrenador> entrenador = entrenadorRepository.findByEmailAndPassword(username, password);
        if (entrenador.isPresent()) {
            session.setAttribute("rol", "ENTRENADOR");
            session.setAttribute("usuario", entrenador.get());
            return "redirect:/dashboard/entrenador";
        }

        // Si no encuentra credenciales válidas
        return "redirect:/login?error=true";
    }
}
