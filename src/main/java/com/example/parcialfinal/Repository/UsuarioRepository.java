package com.example.parcialfinal.Repository;

import com.example.parcialfinal.Models.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioRepository {

    private static UsuarioRepository instancia;
    private final ObservableList<Usuario> usuarios;

    private UsuarioRepository() {
        usuarios = FXCollections.observableArrayList();
        cargarDatosEjemplo();
    }

    public static UsuarioRepository getInstancia() {
        if (instancia == null) {
            instancia = new UsuarioRepository();
        }
        return instancia;
    }

    private void cargarDatosEjemplo() {
        usuarios.add(new Usuario("1056829202", "David Santos", "3104250997", "Davilss@gmail.com"));
        usuarios.add(new Usuario("109097508", "Simon Veraniega", "3207411485", "Simonettapatineta@gmail.com"));
    }

    public ObservableList<Usuario> getUsuarios() {
        return usuarios;
    }

    public void addUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void removeUsuario(Usuario usuario) {
        usuarios.remove(usuario);
    }
}
