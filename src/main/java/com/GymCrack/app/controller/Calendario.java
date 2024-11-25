package com.GymCrack.app.controller;

import org.springframework.web.bind.annotation.GetMapping;

public class Calendario {

	@GetMapping("/calendario")
	public String mostrarCalendario() {
	    return "calendario"; // Nombre del archivo HTML (sin extensión)
	}
}
