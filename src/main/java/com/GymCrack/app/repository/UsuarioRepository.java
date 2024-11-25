package com.GymCrack.app.repository;

import com.GymCrack.app.entity.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	 @Query("{ 'rol': { $in: ['Miembro', 'Administrador'] } }")
	    List<Usuario> findUsuariosByRol();

    // Verifica si un correo ya está registrado
    boolean existsByEmail(String email);

    // Autenticación del usuario por email y contraseña
    Optional<Usuario> findByEmailAndPassword(String email, String password);
}
