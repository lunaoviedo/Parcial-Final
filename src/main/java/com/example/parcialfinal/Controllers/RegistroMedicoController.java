package com.example.parcialfinal.Controllers;

import com.example.parcialfinal.Models.Medico;
import com.example.parcialfinal.Repository.MedicoRepository;
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
    private TableColumn<Medico,String> colCorreo;

    @FXML
    private TableColumn<Medico, String> colId;

    @FXML
    private TableColumn<Medico, String> colNombre;

    @FXML
    private TableColumn<Medico, String> colEspecialidad
            ;
    @FXML
    private TableView<Medico> tablaMedicos;
    @FXML
    private TextField txtCorreo;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTel;

    @FXML
    private TextField txtEspecialidad;


    @FXML
    private TableColumn<Medico, String> colTel;
    private AnchorPane panelContenido;
    private MedicoRepository medicoRepository;
    private ObservableList<Medico> medicosObservable;
    @FXML
    public void initialize() {
        medicoRepository = MedicoRepository.getInstancia();
        configurarTabla();
        cargarMedicos();
        btnEliminar.setDisable(true);
        btnModificar.setDisable(true);
        tablaMedicos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean isSelected = newValue != null;
                    btnEliminar.setDisable(!isSelected);
                    btnModificar.setDisable(!isSelected);
                    mostrarMedico(newValue);
                    txtId.setDisable(isSelected);
                }
        );

    }
    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
    }
    private void cargarMedicos() {
        medicosObservable = medicoRepository.getMedicos();
        tablaMedicos.setItems(medicosObservable);
    }

    private void limpiarCampos() {
        txtId.clear();
        txtNombre.clear();
        txtTel.clear();
        txtCorreo.clear();
        txtId.setDisable(false);
        tablaMedicos.getSelectionModel().clearSelection();
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
            String especialidad = txtEspecialidad.getText();
            Medico medico = new Medico(id, name, phone, email , especialidad);
            medicoRepository.addMedico(medico);
            tablaMedicos.refresh();
            mostrarAlerta("Éxito", "Medico agregado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el medico: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private boolean validarCampos() {
        if(txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTel.getText().isEmpty()||
                txtCorreo.getText().isEmpty() || txtEspecialidad.getText().isEmpty()) {
            mostrarAlerta("Error", "Todos los campos deben ser diligenciados obligatoriamente.", Alert.AlertType.WARNING);
            return false;
        } return true;
    }

    @FXML
    void onEliminar() {
        Medico medico = tablaMedicos.getSelectionModel().getSelectedItem();
        if(medico == null){
            mostrarAlerta("Error", "Seleccione un medico para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el medico: " + medico.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK){
            try{
                medicoRepository.removeMedico(medico);
                medicosObservable.remove(medico);
                tablaMedicos.refresh();
                mostrarAlerta("Exito", "medico eliminado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Exito", "Error al eliminar el medico: "+ e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    public void actualizarTabla() {
        cargarMedicos();
    }

    private void mostrarMedico(Medico medico) {
        if(medico != null){
            txtId.setText(medico.getId());
            txtNombre.setText(medico.getNombre());
            txtTel.setText(medico.getTelefono());
            txtCorreo.setText(medico.getEmail());
            txtEspecialidad.setText(medico.getEspecialidad());
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
        Medico medico = tablaMedicos.getSelectionModel().getSelectedItem();
        if(medico == null){
            mostrarAlerta("Error", "Seleccione un medico para modificar.", Alert.AlertType.WARNING);
            return;
        }
        try{
            medico.setNombre(txtNombre.getText());
            medico.setTelefono(txtTel.getText());
            medico.setEmail(txtCorreo.getText());
            actualizarTabla();
            tablaMedicos.refresh();
            mostrarAlerta("Éxito", "medico modificado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al modificar el medico: " + e.getMessage(), Alert.AlertType.ERROR);
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

