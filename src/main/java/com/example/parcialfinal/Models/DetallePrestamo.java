package com.example.parcialfinal.Models;

import java.time.LocalDate;

public class DetallePrestamo {
    private String fecha;
    private String usuario;
    private String libro;
    private int cantidad;
    private double subtotal;

    public DetallePrestamo(LocalDate fecha, String usuario, String libro, int cantidad, double subtotal) {
        this.fecha = fecha.toString();
        this.usuario = usuario;
        this.libro = libro;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getFecha() {
        return fecha;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getLibro() {
        return libro;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }
}
