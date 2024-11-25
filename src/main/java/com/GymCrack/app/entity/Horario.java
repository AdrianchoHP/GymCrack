package com.GymCrack.app.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "horarios")
public class Horario {
    @Id
    private String id;
    private String dia; // Lunes, Martes, etc.
    private String apertura; // Hora de apertura (HH:mm)
    private String cierre;   // Hora de cierre (HH:mm)
    private String entrenadorId; // ID del entrenador asignado

    // Getters y Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDia() {
        return dia;
    }
    public void setDia(String dia) {
        this.dia = dia;
    }
    public String getApertura() {
        return apertura;
    }
    public void setApertura(String apertura) {
        this.apertura = apertura;
    }
    public String getCierre() {
        return cierre;
    }
    public void setCierre(String cierre) {
        this.cierre = cierre;
    }
    public String getEntrenadorId() {
        return entrenadorId;
    }
    public void setEntrenadorId(String entrenadorId) {
        this.entrenadorId = entrenadorId;
    }
}
