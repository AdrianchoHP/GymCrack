package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Usuario;
import com.GymCrack.app.entity.Membresia;
import com.GymCrack.app.repository.UsuarioRepository;
import com.GymCrack.app.repository.MembresiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuarioWebController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MembresiaRepository membresiaRepository;

    // Listar todos los usuarios
    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioRepository.findAll());
        return "usuarios/lista"; // Plantilla para listar usuarios
    }

    // Formulario para crear un nuevo usuario
    @GetMapping("/nuevo")
    public String nuevoUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("membresias", obtenerMembresiasActivas()); // Lista de membresías activas
        return "usuarios/nuevo"; // Plantilla para formulario de creación
    }

    // Guardar un nuevo usuario
    @PostMapping("/nuevo")
    public String guardarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
        if (usuario.getNombre() == null || usuario.getEmail() == null || usuario.getPassword() == null) {
            model.addAttribute("error", "Nombre, email y contraseña son obligatorios");
            model.addAttribute("membresias", obtenerMembresiasActivas());
            return "usuarios/nuevo";
        }

        // Si no se seleccionó un estado, se asigna "Activo" como predeterminado
        if (usuario.getEstado() == null || usuario.getEstado().isEmpty()) {
            usuario.setEstado("Activo");
        }

        usuarioRepository.save(usuario);
        return "redirect:/usuarios";
    }


    // Formulario para editar un usuario existente
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable("id") String id, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            model.addAttribute("usuario", usuario.get());
            model.addAttribute("membresias", obtenerMembresiasActivas()); // Lista de membresías activas
            return "usuarios/editar"; // Nombre de la vista Thymeleaf
        } else {
            return "error"; // Vista de error si no se encuentra el usuario
        }
    }

    // Guardar cambios de un usuario editado
    @PostMapping("/editar")
    public String actualizarUsuario(@ModelAttribute Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
        if (usuarioExistente.isPresent()) {
            Usuario usuarioActualizado = usuarioExistente.get();
            usuarioActualizado.setNombre(usuario.getNombre());
            usuarioActualizado.setEmail(usuario.getEmail());
            usuarioActualizado.setPassword(usuario.getPassword());
            usuarioActualizado.setRol(usuario.getRol());
            usuarioActualizado.setEstado(usuario.getEstado()); // Actualizar estado
            usuarioActualizado.setDireccion(usuario.getDireccion());
            usuarioActualizado.setTelefono(usuario.getTelefono());
            usuarioActualizado.setMembresia(usuario.getMembresia());

            usuarioRepository.save(usuarioActualizado);
            return "redirect:/usuarios";
        } else {
            return "error"; // Redirige a una página de error si el usuario no existe
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") String id) { // Especifica el nombre
        usuarioRepository.deleteById(id);
        return "redirect:/usuarios";
    }
    @GetMapping("/desactivar/{id}")
    public String desactivarUsuario(@PathVariable("id") String id, Model model) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            Usuario usuarioExistente = usuario.get();
            usuarioExistente.setEstado("Inactivo"); // Cambia el estado a "Inactivo"
            usuarioRepository.save(usuarioExistente);
        }
        return "redirect:/usuarios"; // Redirige a la lista de usuarios
    }

    // Método auxiliar para obtener solo las membresías activas
    private List<Membresia> obtenerMembresiasActivas() {
        return membresiaRepository.findAll().stream()
                .filter(membresia -> "Activa".equalsIgnoreCase(membresia.getEstado()))
                .toList();
    }
}
