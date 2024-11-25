package com.GymCrack.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.GymCrack.app.repository.ReservaRepository;
import com.GymCrack.app.repository.ClaseRepository;
import com.GymCrack.app.entity.Reserva;
import com.GymCrack.app.entity.Clase;
import java.time.LocalDateTime;
import java.util.List;

@Component
@EnableScheduling
public class ScheduledTasks {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ClaseRepository claseRepository;

    // Se ejecuta cada día a la medianoche
    @Scheduled(cron = "0 0 0 * * ?")
    public void eliminarReservasPasadas() {
        List<Reserva> todasLasReservas = reservaRepository.findAll();
        LocalDateTime ahora = LocalDateTime.now();

        todasLasReservas.forEach(reserva -> {
            Clase clase = claseRepository.findById(reserva.getClaseId()).orElse(null);
            if (clase != null && clase.getFechaHora().isBefore(ahora)) {
                reservaRepository.delete(reserva);
            }
        });
    }
}
