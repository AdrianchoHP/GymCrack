package com.GymCrack.app.controller;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.GymCrack.app.entity.Entrenador;
import com.GymCrack.app.repository.EntrenadorRepository;

@Component
public class StringToEntrenadorConverter implements Converter<String, Entrenador> {

    private final EntrenadorRepository entrenadorRepository;

    public StringToEntrenadorConverter(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    @Override
    public Entrenador convert(String source) {
        return entrenadorRepository.findById(source)
                .orElseThrow(() -> new IllegalArgumentException("Entrenador no encontrado: " + source));
    }
}

