package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Membresia;
import com.GymCrack.app.repository.MembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/membresias")
public class MembresiaWebController {

    @Autowired
    private MembresiaRepository membresiaRepository;

    // Listar todas las membresías
    @GetMapping
    public String listarMembresias(Model model) {
        List<Membresia> membresias = membresiaRepository.findAll();
        model.addAttribute("membresias", membresias);
        return "membresias/lista";
    }

    // Formulario para crear una nueva membresía
    @GetMapping("/nueva")
    public String formularioNuevaMembresia(Model model) {
        Membresia nuevaMembresia = new Membresia();
        System.out.println("Cargando formulario con membresía vacía: " + nuevaMembresia);
        model.addAttribute("membresia", nuevaMembresia);
        return "membresias/nueva";
    }

    // Guardar nueva membresía
    @PostMapping
    public String registrarMembresia(@ModelAttribute Membresia membresia) {
        membresiaRepository.save(membresia);
        return "redirect:/membresias";
    }
    
    @GetMapping("/editar/{id}")
    public String formularioEditarMembresia(@PathVariable("id") String id, Model model) {
        Optional<Membresia> membresiaOptional = membresiaRepository.findById(id);
        if (membresiaOptional.isPresent()) {
            model.addAttribute("membresia", membresiaOptional.get());
            return "membresias/editar"; // Vista para editar membresías
        } else {
            return "redirect:/membresias?error=Membresía no encontrada";
        }
    }

    @PostMapping("/actualizar")
    public String actualizarMembresia(@ModelAttribute Membresia membresia) {
        membresiaRepository.save(membresia);
        return "redirect:/membresias?success=Membresía actualizada con éxito";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarMembresia(@PathVariable("id") String id) {
        if (membresiaRepository.existsById(id)) {
            membresiaRepository.deleteById(id);
            return "redirect:/membresias?success=Membresía eliminada con éxito";
        } else {
            return "redirect:/membresias?error=Membresía no encontrada";
        }
    }

}
