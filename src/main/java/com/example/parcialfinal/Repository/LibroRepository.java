package com.example.parcialfinal.Repository;

import com.example.parcialfinal.Models.Paciente;

import java.util.ArrayList;

public class LibroRepository {

    private static LibroRepository instancia;
    private ArrayList<Paciente> pacientes;

    private LibroRepository() {
        pacientes = new ArrayList<>();
        cargarDatosEjemplo();
    }


    public static LibroRepository getInstancia() {
        if (instancia == null) {
            instancia = new LibroRepository();
        }
        return instancia;
    }


    private void cargarDatosEjemplo() {
        pacientes.add(new Paciente("123659", "Barra de Chocolate", 5000.0, 23));
        pacientes.add(new Paciente("155441", "Bocadillo", 2000.0, 30));

    }


    public ArrayList<Paciente> getLibros() {

        return pacientes;
    }


    public void agregarLibro(Paciente paciente) {

        pacientes.add(paciente);

    }


    public boolean eliminarLibro(Paciente paciente) {

        return pacientes.remove(paciente);

    }


    public int getCantidadLibros() {
        return pacientes.size();
    }
}
