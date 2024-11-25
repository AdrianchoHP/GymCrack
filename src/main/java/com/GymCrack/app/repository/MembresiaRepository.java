package com.GymCrack.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.GymCrack.app.entity.Membresia;

public interface MembresiaRepository extends MongoRepository<Membresia, String> {
}

