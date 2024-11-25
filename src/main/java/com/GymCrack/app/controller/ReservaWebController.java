package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Clase;
import com.GymCrack.app.entity.Reserva;
import com.GymCrack.app.entity.Usuario;
import com.GymCrack.app.repository.ClaseRepository;
import com.GymCrack.app.repository.ReservaRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/usuario/reservas")
public class ReservaWebController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClaseRepository claseRepository;


    @GetMapping("/mis-reservas/{idUsuario}")
    public String listarReservasUsuario(@PathVariable("idUsuario") String idUsuario, 
                                      Model model, 
                                      HttpSession session) {
        // Verificar que el usuario está autenticado
        Usuario usuarioActual = (Usuario) session.getAttribute("usuario");
        if (usuarioActual == null || !usuarioActual.getId().equals(idUsuario)) {
            return "redirect:/login";
        }

        List<Reserva> reservas = reservaRepository.findByMiembroId(idUsuario);
        
        reservas.forEach(reserva -> {
            Clase clase = claseRepository.findById(reserva.getClaseId())
                    .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
            reserva.setNombreClase(clase.getNombre());
            reserva.setFechaClase(clase.getFechaHora());
        });

        model.addAttribute("reservas", reservas);
        return "reservas/usuario/mis-reservas";
    }

    @PostMapping("/cancelar/{id}")
    @ResponseBody  // Esto hace que devuelva JSON en lugar de una vista
    public ResponseEntity<?> cancelarReserva(@PathVariable("id") String reservaId, 
                                           HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                               .body(Map.of("error", "Usuario no autenticado"));
        }

        Optional<Reserva> reservaOpt = reservaRepository.findById(reservaId);
        if (reservaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reserva reserva = reservaOpt.get();
        
        // Verificar que la reserva pertenece al usuario
        if (!reserva.getMiembroId().equals(usuario.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                               .body(Map.of("error", "No autorizado"));
        }

        // Verificar que la clase no ha pasado
        Clase clase = claseRepository.findById(reserva.getClaseId())
                                   .orElseThrow(() -> new RuntimeException("Clase no encontrada"));
        
        if (clase.getFechaHora().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest()
                               .body(Map.of("error", "No se puede cancelar una clase que ya pasó"));
        }

        reservaRepository.deleteById(reservaId);
        return ResponseEntity.ok(Map.of("message", "Reserva cancelada exitosamente"));
    }

}

