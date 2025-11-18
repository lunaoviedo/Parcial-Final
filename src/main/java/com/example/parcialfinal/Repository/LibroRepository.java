package com.example.parcialfinal.Repository;

import com.example.parcialfinal.Models.Libro;

import java.util.ArrayList;

public class LibroRepository {

    private static LibroRepository instancia;
    private ArrayList<Libro> libros;

    private LibroRepository() {
        libros = new ArrayList<>();
        cargarDatosEjemplo();
    }


    public static LibroRepository getInstancia() {
        if (instancia == null) {
            instancia = new LibroRepository();
        }
        return instancia;
    }


    private void cargarDatosEjemplo() {
        libros.add(new Libro("123659", "Barra de Chocolate", 5000.0, 23));
        libros.add(new Libro("155441", "Bocadillo", 2000.0, 30));

    }


    public ArrayList<Libro> getLibros() {

        return libros;
    }


    public void agregarLibro(Libro libro) {

        libros.add(libro);

    }


    public boolean eliminarLibro(Libro libro) {

        return libros.remove(libro);

    }


    public int getCantidadLibros() {
        return libros.size();
    }
}
