package com.GymCrack.app.repository;

import com.GymCrack.app.entity.Horario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HorarioRepository extends MongoRepository<Horario, String> {
}
