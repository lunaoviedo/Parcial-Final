package com.example.parcialfinal.Controllers;

import com.example.parcialfinal.Models.Medico;
import com.example.parcialfinal.Repository.UsuarioRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class UsuariosController {
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Medico,String> colCorreo;

    @FXML
    private TableColumn<Medico, String> colId;

    @FXML
    private TableColumn<Medico, String> colNombre;
    @FXML
    private TableView<Medico> tablaUsuarios;
    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;


    @FXML
    private TableColumn<Medico, String> colTel;
    private AnchorPane panelContenido;
    private UsuarioRepository usuarioRepository;
    private ObservableList<Medico> usuariosObservable;
    @FXML
    public void initialize() {
        usuarioRepository = UsuarioRepository.getInstancia();
        configurarTabla();
        cargarUsuarios();
        btnEliminar.setDisable(true);
        btnModificar.setDisable(true);
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean isSelected = newValue != null;
                    btnEliminar.setDisable(!isSelected);
                    btnModificar.setDisable(!isSelected);
                    mostrarUsuario(newValue);
                    txtId.setDisable(isSelected);
                }
        );

    }
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
    }
    private void cargarUsuarios() {
        usuariosObservable = usuarioRepository.getUsuarios();
        tablaUsuarios.setItems(usuariosObservable);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtTel.clear();
        txtCorreo.clear();
        txtId.setDisable(false);
        tablaUsuarios.getSelectionModel().clearSelection();
    }

    @FXML
    void onAgregar() {
        if (!validarCampos()) {
            return;
        }

        try {
            String id = txtId.getText();
            String name = txtNombre.getText();
            String phone = txtTel.getText();
            String email = txtCorreo.getText();
            Medico medico = new Medico(id, name, phone, email);
            usuarioRepository.addUsuario(medico);
            tablaUsuarios.refresh();
            mostrarAlerta("Éxito", "Usuario agregado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private boolean validarCampos() {
        if(txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTel.getText().isEmpty()||
                txtCorreo.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben ser diligenciados obligatoriamente.", Alert.AlertType.WARNING);
            return false;
        } return true;
    }

    @FXML
    void onEliminar() {
        Medico medico = tablaUsuarios.getSelectionModel().getSelectedItem();
        if(medico == null){
            mostrarAlerta("Error", "Seleccione un usuario para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el usuario: " + medico.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK){
            try{
                usuarioRepository.removeUsuario(medico);
                usuariosObservable.remove(medico);
                tablaUsuarios.refresh();
                mostrarAlerta("Exito", "usuario eliminado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Exito", "Error al eliminar el usuario: "+ e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    public void actualizarTabla() {
        cargarUsuarios();
    }

    private void mostrarUsuario(Medico medico) {
        if(medico != null){
            txtId.setText(medico.getId());
            txtNombre.setText(medico.getNombre());
            txtTel.setText(medico.getTelefono());
            txtCorreo.setText(medico.getEmail());
            txtId.setDisable(true);
        } else {
            limpiarCampos();
            txtId.setDisable(false);
        }
    }

    @FXML
    void onModificar() {
        if(!validarCampos()){
            return;
        }
        Medico medico = tablaUsuarios.getSelectionModel().getSelectedItem();
        if(medico == null){
            mostrarAlerta("Error", "Seleccione un usuario para modificar.", Alert.AlertType.WARNING);
            return;
        }
        try{
            medico.setNombre(txtNombre.getText());
            medico.setTelefono(txtTel.getText());
            medico.setEmail(txtCorreo.getText());
            actualizarTabla();
            tablaUsuarios.refresh();
            mostrarAlerta("Éxito", "usuario modificado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al modificar el usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }


    }
    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}

