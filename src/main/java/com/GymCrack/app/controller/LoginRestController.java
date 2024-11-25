package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Usuario;
import com.GymCrack.app.entity.Entrenador;
import com.GymCrack.app.repository.UsuarioRepository;
import com.GymCrack.app.repository.EntrenadorRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginRestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @PostMapping
    public ResponseEntity<String> login(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session) {

        // Verificar si es un usuario registrado
        Optional<Usuario> usuario = usuarioRepository.findByEmailAndPassword(email, password);
        if (usuario.isPresent()) {
            String rol = usuario.get().getRol();
            session.setAttribute("rol", rol);
            session.setAttribute("usuario", usuario.get());

            // Redirigir según el rol
            return switch (rol) {
                case "Administrador" -> ResponseEntity.ok("/dashboard/admin");
                case "Miembro" -> ResponseEntity.ok("/dashboard/miembro");
                default -> ResponseEntity.badRequest().body("Rol no reconocido");
            };
        }

        // Verificar si es un entrenador registrado
        Optional<Entrenador> entrenador = entrenadorRepository.findByEmailAndPassword(email, password);
        if (entrenador.isPresent()) {
            session.setAttribute("rol", "ENTRENADOR");
            session.setAttribute("usuario", entrenador.get());

            // Redirigir al dashboard del entrenador
            return ResponseEntity.ok("/dashboard/entrenador");
        }

        // Si no se encuentra el usuario o entrenador
        return ResponseEntity.badRequest().body("Credenciales inválidas");
    }
}
