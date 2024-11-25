package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Clase;
import com.GymCrack.app.entity.Entrenador;
import com.GymCrack.app.entity.Reserva;
import com.GymCrack.app.entity.Usuario;
import com.GymCrack.app.repository.ClaseRepository;
import com.GymCrack.app.repository.EntrenadorRepository;
import com.GymCrack.app.repository.ReservaRepository;
import com.GymCrack.app.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/clases")
public class ClaseWebController {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;
    
    @Autowired
    private ReservaRepository reservaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public String listarClases(Model model) {
        List<Clase> clases = claseRepository.findAll();
        
        // Mapear entrenadorId al nombre del entrenador
        clases.forEach(clase -> {
            if (clase.getEntrenadorId() != null) {
                entrenadorRepository.findById(clase.getEntrenadorId())
                    .ifPresent(entrenador -> clase.setNombreEntrenador(entrenador.getNombre()));
            } else {
                clase.setNombreEntrenador("Sin asignar");
            }
        });

        model.addAttribute("clases", clases);
        return "clases/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioCrearClase(Model model) {
        Clase clase = new Clase();
        List<Entrenador> entrenadores = entrenadorRepository.findAll(); // Cargar entrenadores disponibles
        model.addAttribute("clase", clase);
        model.addAttribute("entrenadores", entrenadores);
        return "reservas/admin/nueva"; // Template para crear una nueva clase
    }

    @PostMapping
    public String guardarClase(@ModelAttribute Clase clase) {
        claseRepository.save(clase); // Guardar la clase con el entrenador asignado
        return "redirect:/admin/clases";
    }
    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarClase(@PathVariable("id") String id, Model model) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));

        // Cargar entrenadores para el selector
        List<Entrenador> entrenadores = entrenadorRepository.findAll();

        // Agregar los datos al modelo
        model.addAttribute("clase", clase);
        model.addAttribute("entrenadores", entrenadores);

        return "clases/editar";
    }

    @PostMapping("/editar")
    public String actualizarClase(
            @RequestParam("id") String id,
            @RequestParam("nombre") String nombre,
            @RequestParam("categoria") String categoria,
            @RequestParam("capacidadMaxima") int capacidadMaxima,
            @RequestParam("fechaHora") String fechaHora,
            @RequestParam("entrenadorId") String entrenadorId) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + id));
        
        // Asegúrate de que el formato de la fecha coincida con el que viene del formulario
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        clase.setFechaHora(LocalDateTime.parse(fechaHora, formatter));
        
        clase.setNombre(nombre);
        clase.setCategoria(categoria);
        clase.setCapacidadMaxima(capacidadMaxima);
        clase.setEntrenadorId(entrenadorId);
        claseRepository.save(clase);
        return "redirect:/admin/clases";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarClase(@PathVariable("id") String id) {
        if (claseRepository.existsById(id)) {
            claseRepository.deleteById(id);
            return "redirect:/admin/clases?success=Clase eliminada con éxito";
        } else {
            return "redirect:/admin/clases?error=Clase no encontrada";
        }
    }

    @GetMapping("/disponibles")
    public String listarClasesDisponibles(
            @RequestParam(name = "categoria", required = false) String categoria,
            Model model) {
        List<Clase> clases;

        // Filtra por categoría si está presente
        if (categoria != null && !categoria.isEmpty()) {
            clases = claseRepository.findByCategoria(categoria);
        } else {
            clases = claseRepository.findAll();
        }

        // Asigna el nombre del entrenador a cada clase
        clases.forEach(clase -> {
            if (clase.getEntrenadorId() != null) {
                entrenadorRepository.findById(clase.getEntrenadorId())
                        .ifPresent(entrenador -> clase.setNombreEntrenador(entrenador.getNombre()));
            } else {
                clase.setNombreEntrenador("Sin asignar");
            }
        });

        // Recopila categorías únicas
        Set<String> categorias = claseRepository.findAll().stream()
                .map(Clase::getCategoria) // Obtiene la categoría de cada clase
                .collect(Collectors.toSet()); // Elimina duplicados usando Set

        model.addAttribute("clases", clases);
        model.addAttribute("categorias", categorias);

        return "clases/disponibles";
    }
    
    @GetMapping("/participantes/{idClase}")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> obtenerParticipantes(@PathVariable("idClase") String idClase) {
        List<Reserva> reservas = reservaRepository.findByClaseId(idClase);

        // Extraer los IDs de los usuarios de las reservas
        List<String> idsUsuarios = reservas.stream()
                .map(Reserva::getMiembroId)
                .toList();

        // Obtener los usuarios con los IDs
        List<Usuario> participantes = usuarioRepository.findAllById(idsUsuarios);

        // Transformar la lista de usuarios en una respuesta simple con nombres
        List<Map<String, String>> response = participantes.stream()
                .map(usuario -> Map.of("nombre", usuario.getNombre()))
                .toList();

        if (response.isEmpty()) {
            return ResponseEntity.ok(List.of(Map.of("nombre", "No hay participantes")));
        }

        return ResponseEntity.ok(response);
    }

}
