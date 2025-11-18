package com.example.parcialfinal.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class GeneralController{

    @FXML
    private Button btnUsuarios;

    @FXML
    private Button btnLibros;

    @FXML
    private Button btnPrestamos;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private AnchorPane panelContenido;

    @FXML
    void onUsuarios() throws IOException {
        AnchorPane panelConsultarUsuarios = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroMedico.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarUsuarios);
    }
    @FXML
    void onLibros() throws IOException {
        AnchorPane panelConsultarLibros = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroClientes.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarLibros);

    }

    @FXML
    void onPrestamos() throws IOException {
        AnchorPane panelConsultarPrestamos = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroCitas.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarPrestamos);

    }

}
