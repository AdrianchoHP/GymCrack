package com.GymCrack.app.repository;

import com.GymCrack.app.entity.Clase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ClaseRepository extends MongoRepository<Clase, String> {

    @Query("{ 'fechaHora': { $gte: ?0, $lt: ?1 } }")
    List<Clase> findClasesBySemana(LocalDateTime inicioSemana, LocalDateTime finSemana);
    List<Clase> findByEntrenadorId(String entrenadorId);
    
    @Query("{ 'categoria': ?0 }")
    List<Clase> findByCategoria(String categoria);

}
