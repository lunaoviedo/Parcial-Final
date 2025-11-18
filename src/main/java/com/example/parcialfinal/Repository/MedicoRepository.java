package com.example.parcialfinal.Repository;

import com.example.parcialfinal.Models.Medico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MedicoRepository {

    private static MedicoRepository instancia;
    private final ObservableList<Medico> medicos;

    private MedicoRepository() {
        medicos = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    public static MedicoRepository getInstancia() {
        if (instancia == null) {
            instancia = new MedicoRepository();
        }
        return instancia;
    }

    private void cargarDatosEjemplo() {
        medicos.add(new Medico("1056829202", "David Santos", "3104250997", "Davilss@gmail.com" , "Pediatra"));
        medicos.add(new Medico("109097508", "Simon Veraniega", "3207411485", "Simonettapatineta@gmail.com" , "Cardiólogo"));
    }

    public ObservableList<Medico> getMedicos() {
        return medicos;
    }

    public void addMedico(Medico medico) {
        medicos.add(medico);
    }

    public void removeMedico(Medico medico) {
        medicos.remove(medico);
    }
}
