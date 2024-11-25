package com.GymCrack.app.entity;

public class ReservaResponse {
    private String message;
    private String claseId;
    private String miembroId;

    // Constructor
    public ReservaResponse(String message, String claseId, String miembroId) {
        this.message = message;
        this.claseId = claseId;
        this.miembroId = miembroId;
    }

    // Getters y setters
    public String getMessage() {
        return message;
    }

    public String getClaseId() {
        return claseId;
    }

    public String getMiembroId() {
        return miembroId;
    }
}

