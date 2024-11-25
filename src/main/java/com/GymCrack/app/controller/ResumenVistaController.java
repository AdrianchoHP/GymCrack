package com.GymCrack.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resumen")
public class ResumenVistaController {

    @GetMapping("/resumen-reservas")
    public String mostrarResumenReservas() {
        return "resumen_reservas"; // Renderiza el archivo HTML ubicado en src/main/resources/templates
    }
}
