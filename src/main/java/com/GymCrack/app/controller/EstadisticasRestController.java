package com.GymCrack.app.controller;

import com.GymCrack.app.repository.ReservaRepository;
import com.GymCrack.app.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/estadisticas")
public class EstadisticasRestController {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClaseRepository claseRepository;

    // Obtener estadísticas de reservas por clase
    @GetMapping("/reservas-por-clase")
    public List<Map<String, Object>> obtenerReservasPorClase() {
        return claseRepository.findAll().stream().map(clase -> {
            long reservas = reservaRepository.countByClaseId(clase.getId());
            Map<String, Object> estadisticas = new HashMap<>();
            estadisticas.put("claseId", clase.getId());
            estadisticas.put("nombreClase", clase.getNombre());
            estadisticas.put("reservas", reservas);
            return estadisticas;
        }).collect(Collectors.toList());
    }
}

