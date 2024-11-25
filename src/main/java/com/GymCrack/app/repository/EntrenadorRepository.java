package com.GymCrack.app.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.GymCrack.app.entity.Entrenador;

public interface EntrenadorRepository extends MongoRepository<Entrenador, String> {
    List<Entrenador> findByNombre(String nombre);
    List<Entrenador> findByEspecialidad(String especialidad);
    Optional<Entrenador> findByEmailAndPassword(String email, String password);
}
