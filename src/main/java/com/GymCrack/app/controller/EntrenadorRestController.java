package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Entrenador;
import com.GymCrack.app.repository.EntrenadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorRestController {

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    // Listar todos los entrenadores
    @GetMapping
    public List<Entrenador> listarEntrenadores() {
        return entrenadorRepository.findAll();
    }

    // Obtener un entrenador por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEntrenador(@PathVariable String id) {
        Optional<Entrenador> entrenador = entrenadorRepository.findById(id);

        // Verifica si el entrenador está presente
        if (entrenador.isPresent()) {
            return ResponseEntity.ok(entrenador.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Entrenador con ID " + id + " no encontrado");
        }
    }

    // Crear un nuevo entrenador
    @PostMapping
    public ResponseEntity<?> registrarEntrenador(@Valid @RequestBody Entrenador entrenador) {
        // No establezcas manualmente el ID
        Entrenador savedEntrenador = entrenadorRepository.save(entrenador);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEntrenador);
    }


    // Editar un entrenador existente
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEntrenador(@PathVariable String id, @Valid @RequestBody Entrenador entrenadorActualizado) {
        Optional<Entrenador> entrenadorOptional = entrenadorRepository.findById(id);

        if (entrenadorOptional.isPresent()) {
            Entrenador entrenador = entrenadorOptional.get();
            entrenador.setNombre(entrenadorActualizado.getNombre());
            entrenador.setEspecialidad(entrenadorActualizado.getEspecialidad());
            entrenador.setDisponibilidad(entrenadorActualizado.getDisponibilidad());
            entrenador.setEmail(entrenadorActualizado.getEmail());
            entrenador.setRol(entrenadorActualizado.getRol());
            entrenador.setDireccion(entrenadorActualizado.getDireccion());
            entrenador.setTelefono(entrenadorActualizado.getTelefono());
            Entrenador updatedEntrenador = entrenadorRepository.save(entrenador);
            return ResponseEntity.ok(updatedEntrenador);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Entrenador con ID " + id + " no encontrado");
        }
    }

    // Eliminar un entrenador por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEntrenador(@PathVariable String id) {
        if (entrenadorRepository.existsById(id)) {
            entrenadorRepository.deleteById(id);
            return ResponseEntity.ok().body("Entrenador eliminado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Entrenador con ID " + id + " no encontrado");
        }
    }
}
