package com.example.parcialfinal.Controllers;

import com.example.parcialfinal.Models.Usuario;
import com.example.parcialfinal.Repository.UsuarioRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.util.Optional;

public class RegistroMedicoController {
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Usuario,String> colCorreo;

    @FXML
    private TableColumn<Usuario, String> colId;

    @FXML
    private TableColumn<Usuario, String> colNombre;
    @FXML
    private TableView<Usuario> tablaMedicos;
    @FXML
    private TableColumn<Usuario,String> colEspcialidad;
    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtEspcialidad;

    @FXML
    private TableColumn<Usuario, String> colTel;


    private AnchorPane panelContenido;
    private MedicoRepository medicoRepository;
    private ObservableList<Medico> MedicosObservable;
    @FXML
    public void initialize() {
        medicoRepository = MedicoRepository.getInstancia();
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
        colEspcialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
    }
    private void cargarUsuarios() {
        medicoObservable = medicoRepository.getUsuarios();
        tablaMedicos.setItems(medicoObservable);
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
            Usuario usuario = new Usuario(id, name, phone, email);
            usuarioRepository.addUsuario(usuario);
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
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if(usuario == null){
            mostrarAlerta("Error", "Seleccione un usuario para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el usuario: " + usuario.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK){
            try{
                usuarioRepository.removeUsuario(usuario);
                usuariosObservable.remove(usuario);
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

    private void mostrarUsuario(Usuario usuario) {
        if(usuario != null){
            txtId.setText(usuario.getId());
            txtNombre.setText(usuario.getNombre());
            txtTel.setText(usuario.getTelefono());
            txtCorreo.setText(usuario.getEmail());
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
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if(usuario == null){
            mostrarAlerta("Error", "Seleccione un usuario para modificar.", Alert.AlertType.WARNING);
            return;
        }
        try{
            usuario.setNombre(txtNombre.getText());
            usuario.setTelefono(txtTel.getText());
            usuario.setEmail(txtCorreo.getText());
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

