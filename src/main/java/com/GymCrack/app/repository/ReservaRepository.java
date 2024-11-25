package com.GymCrack.app.repository;

import com.GymCrack.app.entity.Reserva;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends MongoRepository<Reserva, String> {
    // Contar reservas por clase
    long countByClaseId(String claseId);

    // Verificar si un usuario ya reservó una clase
    boolean existsByMiembroIdAndClaseId(String miembroId, String claseId);

    // Obtener reservas por usuario
    List<Reserva> findByMiembroId(String miembroId);
    
    // Nuevo método para encontrar reservas pasadas
    @Query("{'claseId': ?0, 'fechaClase': {$lt: ?1}}")
    List<Reserva> findByClaseIdAndFechaClaseBefore(String claseId, LocalDateTime fecha);
    
    // Obtener todas las reservas de una clase específica
    List<Reserva> findByClaseId(String claseId);

}
