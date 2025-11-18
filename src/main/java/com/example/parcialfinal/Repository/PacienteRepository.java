package com.example.parcialfinal.Repository;

import com.example.parcialfinal.Models.Paciente;

import java.util.ArrayList;

public class PacienteRepository {

    private static PacienteRepository instancia;
    private ArrayList<Paciente> pacientes;

    private PacienteRepository() {
        pacientes = new ArrayList<>();
        cargarDatosEjemplo();
    }


    public static PacienteRepository getInstancia() {
        if (instancia == null) {
            instancia = new PacienteRepository();
        }
        return instancia;
    }


    private void cargarDatosEjemplo() {
        pacientes.add(new Paciente("123659", "Juan Carlos Gomez", "3105296752", "juancarlos@gmail.com", "Dolor Abdominal"));
        pacientes.add(new Paciente("584264", "David Simon Vega", "3111465638", "smv1222@gmail.com", "Molestia Visual"));

    }


    public ArrayList<Paciente> getPacientes() {

        return pacientes;
    }


    public void agregarPaciente(Paciente paciente) {

        pacientes.add(paciente);

    }


    public boolean eliminarPaciente(Paciente paciente) {

        return pacientes.remove(paciente);

    }
}
