package com.GymCrack.app.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "clases")
public class Clase {
    @Id
    private String id;
    private String nombre;
    private String categoria; // Yoga, Pesas, Cardio, etc.
    private int capacidadMaxima;
    private LocalDateTime fechaHora;
    private String colorIdentificador; // Para distinguir visualmente
    private String entrenadorId; // ID del entrenador seleccionado
    
    // Getters y Setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public int getCapacidadMaxima() {
		return capacidadMaxima;
	}
	public void setCapacidadMaxima(int capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}
	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}
	public String getColorIdentificador() {
		return colorIdentificador;
	}
	public void setColorIdentificador(String colorIdentificador) {
		this.colorIdentificador = colorIdentificador;
	}
	public String getEntrenadorId() {
		return entrenadorId;
	}
	public void setEntrenadorId(String entrenadorId) {
		this.entrenadorId = entrenadorId;
	}
	
	@Transient
	private String nombreEntrenador;

	public String getNombreEntrenador() {
	    return nombreEntrenador;
	}

	public void setNombreEntrenador(String nombreEntrenador) {
	    this.nombreEntrenador = nombreEntrenador;
	}

}

