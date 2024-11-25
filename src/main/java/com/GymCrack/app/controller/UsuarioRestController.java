package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Usuario;
import com.GymCrack.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
	

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Listar todos los usuarios
    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Obtener un usuario por ID
    @GetMapping("/detalle")
    public ResponseEntity<?> obtenerUsuario(@RequestParam("id") String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario con ID " + id + " no encontrado");
        }
    }

    // Crear un nuevo usuario
    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuario.getNombre() == null || usuario.getEmail() == null || usuario.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Los campos nombre, email y password son obligatorios");
        }
        usuario.setEstado("Activo"); // Valor predeterminado
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuario));
    }

    // Editar un usuario
    @PutMapping
    public ResponseEntity<?> editarUsuario(@RequestParam("id") String id, @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (!usuarioExistente.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuario = usuarioExistente.get();
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setPassword(usuarioActualizado.getPassword());
        usuario.setRol(usuarioActualizado.getRol());
        usuario.setEstado(usuarioActualizado.getEstado());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setMembresia(usuarioActualizado.getMembresia());

        return ResponseEntity.ok(usuarioRepository.save(usuario));
    }

    // Eliminar un usuario (lógico)
    @DeleteMapping
    public ResponseEntity<?> eliminarUsuario(@RequestParam("id") String id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (!usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        Usuario usuarioInactivo = usuario.get();
        usuarioInactivo.setEstado("Inactivo"); // Eliminación lógica
        usuarioRepository.save(usuarioInactivo);
        return ResponseEntity.ok("Usuario desactivado exitosamente");
    }
}
