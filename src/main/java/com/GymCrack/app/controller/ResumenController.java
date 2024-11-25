package com.GymCrack.app.controller;

import com.GymCrack.app.entity.Clase;
import com.GymCrack.app.repository.ClaseRepository;
import com.GymCrack.app.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/resumen")
public class ResumenController {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    // Resumen por clase
    @GetMapping("/por-clase")
    public List<Map<String, Object>> resumenPorClase() {
        return claseRepository.findAll().stream().map(clase -> {
            long reservas = reservaRepository.countByClaseId(clase.getId());
            Map<String, Object> resumen = new HashMap<>();
            resumen.put("nombre", clase.getNombre());
            resumen.put("capacidadMaxima", clase.getCapacidadMaxima());
            resumen.put("reservas", reservas);
            return resumen;
        }).collect(Collectors.toList());
    }

    // Resumen por entrenador
    @GetMapping("/por-entrenador")
    public List<Map<String, Object>> resumenPorEntrenador() {
        return claseRepository.findAll().stream()
            .filter(clase -> clase.getEntrenadorId() != null) // Ignorar clases sin entrenador asignado
            .collect(Collectors.groupingBy(Clase::getEntrenadorId))
            .entrySet().stream().map(entry -> {
                String entrenadorId = entry.getKey();
                long totalReservas = entry.getValue().stream()
                    .mapToLong(clase -> reservaRepository.countByClaseId(clase.getId()))
                    .sum();
                Map<String, Object> resumen = new HashMap<>();
                resumen.put("entrenadorId", entrenadorId);
                resumen.put("totalReservas", totalReservas);
                return resumen;
            }).collect(Collectors.toList());
    }

}
