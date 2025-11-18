package com.example.parcialfinal.Models;

import java.time.LocalDate;

public class Citas {
    private String fecha;
    private String usuario;
    private String medico;
    private int cantidad;
    private double subtotal;

    public Citas(LocalDate fecha, String usuario, String medico, int cantidad, double subtotal) {
        this.fecha = fecha.toString();
        this.usuario = usuario;
        this.medico = medico;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getMedico() {
        return medico;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }
}
