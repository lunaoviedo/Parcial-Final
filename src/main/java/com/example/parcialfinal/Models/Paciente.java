package com.example.parcialfinal.Models;

public class Paciente extends Persona {
    private String motivoConsulta;

    public Paciente() {}

    public Paciente(String id, String nombre, String telefono, String email, String motivoConsulta) {
        super(id, nombre, telefono, email);
        this.motivoConsulta = motivoConsulta;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }
    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }
}

