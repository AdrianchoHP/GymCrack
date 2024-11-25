package com.GymCrack.app.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "reservas")
public class Reserva {
    @Id
    private String id;
    private String miembroId; // ID del miembro
    private String claseId;   // ID de la clase
    private LocalDateTime fechaReserva; // Fecha en que se hizo la reserva

    @Transient
    private String nombreClase; // Nombre de la clase (no se guarda en la base de datos)
    @Transient
    private LocalDateTime fechaClase; // Fecha y hora de la clase (no se guarda en la base de datos)

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMiembroId() {
        return miembroId;
    }

    public void setMiembroId(String miembroId) {
        this.miembroId = miembroId;
    }

    public String getClaseId() {
        return claseId;
    }

    public void setClaseId(String claseId) {
        this.claseId = claseId;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    public void setFechaReserva(LocalDateTime fechaReserva) {
        this.fechaReserva = fechaReserva;
    }

    public String getNombreClase() {
        return nombreClase;
    }

    public void setNombreClase(String nombreClase) {
        this.nombreClase = nombreClase;
    }

    public LocalDateTime getFechaClase() {
        return fechaClase;
    }

    public void setFechaClase(LocalDateTime fechaClase) {
        this.fechaClase = fechaClase;
    }
}
