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
    void onMedicos() throws IOException {
        AnchorPane panelConsultarMedicos = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroMedico.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarMedicos);
    }
    @FXML
    void onPacientes() throws IOException {
        AnchorPane panelConsultarPacientes = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroPacientes.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarPacientes);

    }

    @FXML
    void onCitas() throws IOException {
        AnchorPane panelConsultarCitas = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroCitas.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarCitas);

    }

}
