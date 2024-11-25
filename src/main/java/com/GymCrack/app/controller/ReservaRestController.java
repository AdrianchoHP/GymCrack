package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Clase;
import com.GymCrack.app.entity.Reserva;
import com.GymCrack.app.repository.ClaseRepository;
import com.GymCrack.app.repository.ReservaRepository;
import com.GymCrack.app.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservas")
public class ReservaRestController {

    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ClaseRepository claseRepository;

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelarReserva(@PathVariable("id") String id) {
        try {
            Optional<Reserva> reserva = reservaRepository.findById(id);
            if (!reserva.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reserva no encontrada");
            }
            
            reservaRepository.deleteById(id);
            return ResponseEntity.ok()
                .body("Reserva cancelada exitosamente");
                
        } catch (Exception e) {
            e.printStackTrace(); // Para ver el error en los logs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al cancelar la reserva: " + e.getMessage());
        }
    }
    
    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody Reserva reserva) {
        try {
            // Verificar si el usuario ya tiene una reserva para esta clase
            boolean yaReservada = reservaRepository.existsByMiembroIdAndClaseId(reserva.getMiembroId(), reserva.getClaseId());
            if (yaReservada) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "El miembro ya tiene una reserva para esta clase"));
            }

            // Verificar si el usuario existe
            if (!usuarioRepository.existsById(reserva.getMiembroId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Usuario no encontrado"));
            }

            // Verificar si la clase existe
            Clase clase = claseRepository.findById(reserva.getClaseId()).orElse(null);
            if (clase == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Clase no encontrada"));
            }

            // Verificar capacidad de la clase
            long reservasExistentes = reservaRepository.countByClaseId(reserva.getClaseId());
            if (reservasExistentes >= clase.getCapacidadMaxima()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Clase sin cupo disponible"));
            }

            // Crear la reserva
            reserva.setFechaReserva(LocalDateTime.now());
            reservaRepository.save(reserva);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "message", "Reserva creada exitosamente",
                "claseId", reserva.getClaseId(),
                "miembroId", reserva.getMiembroId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error interno del servidor"));
        }
    }

}
