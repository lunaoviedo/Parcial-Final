package com.example.parcialfinal.Models;

public class Libro {
    private String codigo;
    private String nombre;
    private Double precio;
    private Integer stock;

    public Libro() {}

    public Libro(String codigo, String nombre, Double precio, Integer stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getId() {
        return codigo;
    }

    public void setId(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }
}

