package com.example.parcialfinal.Repository;

import com.example.parcialfinal.Models.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioRepository {

    private static UsuarioRepository instancia;
    private final ObservableList<Medico> medicos;

    private UsuarioRepository() {
        medicos = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    public static UsuarioRepository getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepository();
        }
        return instancia;
    }

    private void cargarDatosEjemplo() {
        medicos.add(new Medico("1056829202", "David Santos", "3104250997", "Davilss@gmail.com"));
        medicos.add(new Medico("109097508", "Simon Veraniega", "3207411485", "Simonettapatineta@gmail.com"));
    }

    public ObservableList<Medico> getUsuarios() {
        return medicos;
    }

    public void addUsuario(Medico medico) {
        medicos.add(medico);
    }

    public void removeUsuario(Medico medico) {
        medicos.remove(medico);
    }
}
