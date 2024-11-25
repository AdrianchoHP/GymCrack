package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Clase;
import com.GymCrack.app.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clases")
public class ClaseRestController {

    @Autowired
    private ClaseRepository claseRepository;

    // Registrar una nueva clase
    @PostMapping
    public Clase registrarClase(@RequestBody Clase clase) {
        return claseRepository.save(clase);
    }

    // Listar todas las clases
    @GetMapping
    public List<Clase> listarClases() {
        return claseRepository.findAll();
    }

    // Buscar una clase por ID
    @GetMapping("/{id}")
    public Optional<Clase> obtenerClase(@PathVariable String id) {
        return claseRepository.findById(id);
    }

    // Asignar un entrenador a una clase
    @PutMapping("/{id}/entrenador")
    public Clase asignarEntrenador(@PathVariable String id, @RequestParam String entrenadorId) {
        return claseRepository.findById(id).map(clase -> {
            clase.setEntrenadorId(entrenadorId);
            return claseRepository.save(clase);
        }).orElseThrow(() -> new RuntimeException("Clase no encontrada"));
    }

    // Actualizar una clase
    @PutMapping("/{id}")
    public Clase actualizarClase(@PathVariable String id, @RequestBody Clase claseActualizada) {
        Optional<Clase> claseOptional = claseRepository.findById(id);
        if (claseOptional.isPresent()) {
            Clase clase = claseOptional.get();
            clase.setNombre(claseActualizada.getNombre());
            clase.setCategoria(claseActualizada.getCategoria());
            clase.setCapacidadMaxima(claseActualizada.getCapacidadMaxima());
            clase.setFechaHora(claseActualizada.getFechaHora());
            clase.setColorIdentificador(claseActualizada.getColorIdentificador());
            return claseRepository.save(clase);
        } else {
            throw new RuntimeException("Clase no encontrada");
        }
    }

    // Eliminar una clase
    @DeleteMapping("/{id}")
    public String eliminarClase(@PathVariable String id) {
        claseRepository.deleteById(id);
        return "Clase eliminada correctamente.";
    }
    
    @GetMapping("/calendar")
    public List<Map<String, Object>> obtenerDatosCalendario() {
        return claseRepository.findAll().stream().map(clase -> {
            Map<String, Object> evento = new HashMap<>();
            evento.put("id", clase.getId());
            evento.put("title", clase.getNombre() + " - " + clase.getCategoria());
            evento.put("start", clase.getFechaHora().toString()); // Formato ISO 8601
            evento.put("backgroundColor", clase.getColorIdentificador()); // Color distintivo
            return evento;
        }).collect(Collectors.toList());
    }

}
