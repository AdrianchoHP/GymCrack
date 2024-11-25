package com.GymCrack.app.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/horarios")
public class HorarioWebController {

    @GetMapping("/gestionar")
    public String gestionarHorarios() {
        return "horarios"; // Nombre del archivo HTML en templates/horarios.html
    }
    
    @GetMapping("/ver")
    public String verHorarios() {
        return "HorarioMiembro"; // Nombre del archivo HTML en templates/HorarioMiembro.html
    }
}

