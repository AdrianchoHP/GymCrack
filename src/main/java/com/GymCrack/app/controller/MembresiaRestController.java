package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Membresia;
import com.GymCrack.app.repository.MembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/membresias")
public class MembresiaRestController {

    @Autowired
    private MembresiaRepository membresiaRepository;

    // Registrar una nueva membresía
    @PostMapping
    public Membresia registrarMembresia(@RequestBody Membresia membresia) {
        return membresiaRepository.save(membresia);
    }

    // Listar todas las membresías
    @GetMapping
    public List<Membresia> listarMembresias() {
        return membresiaRepository.findAll();
    }

    // Buscar una membresía por ID
    @GetMapping("/{id}")
    public Optional<Membresia> obtenerMembresia(@PathVariable String id) {
        return membresiaRepository.findById(id);
    }

    // Actualizar una membresía
    @PutMapping("/{id}")
    public Membresia actualizarMembresia(@PathVariable String id, @RequestBody Membresia membresiaActualizada) {
        Optional<Membresia> membresiaOptional = membresiaRepository.findById(id);
        if (membresiaOptional.isPresent()) {
            Membresia membresia = membresiaOptional.get();
            membresia.setTipo(membresiaActualizada.getTipo());
            membresia.setEstado(membresiaActualizada.getEstado());
            membresia.setBeneficios(membresiaActualizada.getBeneficios());
            membresia.setPrecio(membresiaActualizada.getPrecio());
            return membresiaRepository.save(membresia);
        } else {
            throw new RuntimeException("Membresía no encontrada");
        }
    }

    // Eliminar una membresía
    @DeleteMapping("/{id}")
    public String eliminarMembresia(@PathVariable String id) {
        membresiaRepository.deleteById(id);
        return "Membresía eliminada correctamente.";
    }
}
