package com.GymCrack.app.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Invalida la sesión actual
        session.invalidate();
        // Redirige al usuario a la página de inicio de sesión
        return "redirect:/login";
    }
}
