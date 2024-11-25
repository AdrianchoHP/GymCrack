package com.GymCrack.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String defaultLogin() {
        return "redirect:/login";
    }

    @GetMapping("/error")
    public String handleError() {
        return "error"; // Página de error personalizada
    }
}
