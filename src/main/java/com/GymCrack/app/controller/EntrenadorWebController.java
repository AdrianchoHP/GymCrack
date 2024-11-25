package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Clase;
import com.GymCrack.app.entity.Entrenador;
import com.GymCrack.app.repository.ClaseRepository;
import com.GymCrack.app.repository.EntrenadorRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/entrenadores")
public class EntrenadorWebController {

	@Autowired
    private ClaseRepository claseRepository;
	
    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @GetMapping("/lista")
    public String listarEntrenadores(Model model) {
        List<Entrenador> entrenadores = entrenadorRepository.findAll();

        // Asignar clases a cada entrenador
        for (Entrenador entrenador : entrenadores) {
            List<Clase> clases = claseRepository.findByEntrenadorId(entrenador.getId());
            entrenador.setClasesAsignadas(clases);
        }

        model.addAttribute("entrenadores", entrenadores);
        return "entrenadores/lista";
    }


    // Formulario para crear un nuevo entrenador
    @GetMapping("/nuevo")
    public String nuevoEntrenadorForm(Model model) {
        model.addAttribute("entrenador", new Entrenador());
        return "entrenadores/nuevo";
    }

    // Guardar un entrenador (crear o actualizar)
    @PostMapping
    public String guardarEntrenador(@ModelAttribute("entrenador") Entrenador entrenador) {
        if (entrenador.getId() != null && !entrenador.getId().isEmpty()) {
            Optional<Entrenador> entrenadorExistente = entrenadorRepository.findById(entrenador.getId());
            if (entrenadorExistente.isPresent()) {
                Entrenador actual = entrenadorExistente.get();
                actual.setNombre(entrenador.getNombre());
                actual.setEmail(entrenador.getEmail());
                actual.setEspecialidad(entrenador.getEspecialidad());
                actual.setDisponibilidad(entrenador.getDisponibilidad());
                actual.setRol(entrenador.getRol());
                actual.setDireccion(entrenador.getDireccion());
                actual.setTelefono(entrenador.getTelefono());
                entrenadorRepository.save(actual);
            }
        } else {
            entrenadorRepository.save(entrenador);
        }
        return "redirect:/entrenadores/lista";
    }

    // Formulario para editar un entrenador
    @GetMapping("/editar/{id}")
    public String editarEntrenadorForm(@PathVariable("id") String id, Model model) {
        Optional<Entrenador> entrenador = entrenadorRepository.findById(id);
        if (entrenador.isPresent()) {
            model.addAttribute("entrenador", entrenador.get());
            return "entrenadores/nuevo"; // Reutiliza el formulario de creación
        } else {
            model.addAttribute("error", "El entrenador no existe.");
            return "redirect:/entrenadores/lista";
        }
    }



    // Eliminar un entrenador
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> eliminarEntrenador(@PathVariable("id") String id) {
        if (entrenadorRepository.existsById(id)) {
            entrenadorRepository.deleteById(id);
            return ResponseEntity.ok("Entrenador eliminado con éxito");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entrenador no encontrado");
        }
    }

    @GetMapping
    public String listarClasesDelEntrenador(HttpSession session, Model model) {
        // Obtener el ID del entrenador desde la sesión
        String entrenadorId = (String) session.getAttribute("usuarioId");

        // Consultar las clases asignadas a este entrenador
        List<Clase> clases = claseRepository.findByEntrenadorId(entrenadorId);

        // Pasar las clases al modelo
        model.addAttribute("clases", clases);
        return "clases/mis-clases"; // Vista para mostrar las clases del entrenador
    }
    
    @GetMapping("/mis-clases")
    public String verMisClases(HttpSession session, Model model) {
        // Obtener el entrenador autenticado desde la sesión
        Entrenador entrenador = (Entrenador) session.getAttribute("usuario");
        if (entrenador == null) {
            return "redirect:/login"; // Redirigir al login si no está autenticado
        }

        // Consultar las clases asignadas al entrenador
        List<Clase> clases = claseRepository.findByEntrenadorId(entrenador.getId());
        model.addAttribute("clases", clases); // Agregar las clases al modelo

        return "entrenadores/mis-clases"; // Renderizar la plantilla mis-clases.html
    }

}
