package com.example.parcialfinal.Models;

import java.time.LocalDate;

public class Citas {

    private LocalDate fecha;
    private String hora;
    private String medico;
    private String paciente;
    private double precio;

    public Citas(LocalDate fecha, String hora, String medico, String paciente, double precio) {
        this.fecha = fecha;
        this.hora = hora;
        this.medico = medico;
        this.paciente = paciente;
        this.precio = precio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getMedico() {
        return medico;
    }

    public String getPaciente() {
        return paciente;
    }

    public double getPrecio() {
        return precio;
    }
}
