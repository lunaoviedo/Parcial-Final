package com.example.parcialfinal.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
                getClass().getResource("/com/example/parcialfinal/RegistroUsuarios.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarUsuarios);
    }
    @FXML
    void onLibros() throws IOException {
        AnchorPane panelConsultarLibros = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroLibros.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarLibros);

    }

    @FXML
    void onPrestamos() throws IOException {
        AnchorPane panelConsultarPrestamos = FXMLLoader.load(
                getClass().getResource("/com/example/parcialfinal/RegistroPrestamos.fxml"));
        panelContenido.getChildren().clear();
        panelContenido.getChildren().add(panelConsultarPrestamos);

    }

}
