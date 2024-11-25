package com.GymCrack.app.controller;

// Importaciones básicas
import com.GymCrack.app.entity.Horario;
import com.GymCrack.app.repository.HorarioRepository;
import com.GymCrack.app.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Importaciones para logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Importaciones de utilidades
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/horarios")
@CrossOrigin(origins = "*")
public class HorarioController {
    
    // Crear el logger para esta clase
    private static final Logger logger = LoggerFactory.getLogger(HorarioController.class);

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @GetMapping
    public List<Map<String, Object>> listarHorarios() {
        try {
            List<Map<String, Object>> horarios = horarioRepository.findAll().stream().map(horario -> {
                Map<String, Object> horarioEnriquecido = new HashMap<>();
                horarioEnriquecido.put("id", horario.getId());
                horarioEnriquecido.put("dia", horario.getDia());
                horarioEnriquecido.put("apertura", horario.getApertura());
                horarioEnriquecido.put("cierre", horario.getCierre());
                horarioEnriquecido.put("entrenadorId", horario.getEntrenadorId());

                if (horario.getEntrenadorId() != null) {
                    entrenadorRepository.findById(horario.getEntrenadorId())
                        .ifPresent(entrenador -> {
                            horarioEnriquecido.put("entrenador", entrenador.getNombre());
                        });
                } else {
                    horarioEnriquecido.put("entrenador", "No asignado");
                }
                return horarioEnriquecido;
            }).collect(Collectors.toList());
            
            logger.info("Se obtuvieron {} horarios", horarios.size());
            return horarios;
            
        } catch (Exception e) {
            logger.error("Error al listar horarios: {}", e.getMessage());
            throw e;
        }
    }

    @PostMapping
    public ResponseEntity<?> crearHorario(@RequestBody Horario horario) {
        try {
            logger.info("Creando nuevo horario");
            if (horario.getEntrenadorId() != null) {
                entrenadorRepository.findById(horario.getEntrenadorId())
                    .orElseThrow(() -> new RuntimeException("Entrenador no encontrado"));
            }
            Horario nuevoHorario = horarioRepository.save(horario);
            logger.info("Horario creado con ID: {}", nuevoHorario.getId());
            return ResponseEntity.ok(nuevoHorario);
        } catch (Exception e) {
            logger.error("Error al crear horario: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al crear horario: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarHorario(@PathVariable(name = "id") String id) {
        try {
            logger.info("Intentando eliminar horario con ID: {}", id);
            
            if (!horarioRepository.existsById(id)) {
                logger.warn("Horario con ID {} no encontrado", id);
                return ResponseEntity.notFound().build();
            }
            
            horarioRepository.deleteById(id);
            logger.info("Horario con ID {} eliminado exitosamente", id);
            
            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Horario eliminado correctamente");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error al eliminar horario con ID {}: {}", id, e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al eliminar horario: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarHorario(@PathVariable(name = "id") String id, @RequestBody Horario horarioActualizado) {
        try {
            logger.info("Intentando actualizar horario con ID: {}", id);
            
            return horarioRepository.findById(id)
                .map(horario -> {
                    horario.setDia(horarioActualizado.getDia());
                    horario.setApertura(horarioActualizado.getApertura());
                    horario.setCierre(horarioActualizado.getCierre());
                    horario.setEntrenadorId(horarioActualizado.getEntrenadorId());
                    
                    Horario horarioGuardado = horarioRepository.save(horario);
                    logger.info("Horario actualizado exitosamente");
                    return ResponseEntity.ok(horarioGuardado);
                })
                .orElseGet(() -> {
                    logger.warn("Horario con ID {} no encontrado", id);
                    return ResponseEntity.notFound().build();
                });
        } catch (Exception e) {
            logger.error("Error al actualizar horario: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Error al actualizar horario: " + e.getMessage());
        }
    }
}