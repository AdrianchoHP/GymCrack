package com.GymCrack.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardWebController {

    @GetMapping("/dashboard/admin")
    public String adminDashboard() {
        return "dashboard/admin"; // Plantilla para el administrador
    }

    @GetMapping("/dashboard/entrenador")
    public String entrenadorDashboard() {
        return "dashboard/entrenador"; // Plantilla para el entrenador
    }

    @GetMapping("/dashboard/miembro")
    public String miembroDashboard() {
        return "dashboard/miembro"; // Plantilla para el miembro
    }

    @GetMapping("/calendario")
    public String mostrarCalendario() {
        return "calendario"; // Renderiza la plantilla calendario.html
    }

    @GetMapping("/calendario/volver")
    public String redirigirADashboard(HttpSession session) {
        String rol = (String) session.getAttribute("rol");
        if ("ENTRENADOR".equals(rol)) { // Corregido para coincidir con el rol asignado en LoginWebController
            return "redirect:/dashboard/entrenador";
        } else if ("Miembro".equals(rol)) {
            return "redirect:/dashboard/miembro";
        } else {
            return "redirect:/dashboard/admin"; // Página principal u otra
        }
    }


}
