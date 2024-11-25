package com.GymCrack.app.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "entrenadores")
public class Entrenador {

    @Id
    private String id;

    private String nombre;
    private String email;
    private String password;
    private String rol; // Miembro, Administrador, Entrenador
    private String direccion;
    private String telefono;
    private String estado; // Activo o Inactivo
    private String especialidad;
    private String disponibilidad;

    @Transient
    private List<Clase> clasesAsignadas; // Clases asignadas al entrenador

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public String getDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(String disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public List<Clase> getClasesAsignadas() {
		return clasesAsignadas;
	}

	public void setClasesAsignadas(List<Clase> clasesAsignadas) {
		this.clasesAsignadas = clasesAsignadas;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}


