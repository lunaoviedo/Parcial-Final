package com.example.parcialfinal.Controllers;

import com.example.parcialfinal.Models.Paciente;
import com.example.parcialfinal.Repository.PacienteRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.util.Optional;

public class PacienteController {
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Paciente, String> colId;

    @FXML
    private TableColumn<Paciente, String> colNombre;

    @FXML
    private TableColumn<Paciente, String> colTelefono;

    @FXML
    private TableColumn<Paciente, String> colCorreo;
    @FXML
    private TableColumn<Paciente, String> colMotivo;
    @FXML
    private TableView<Paciente> tablaPacientes;
    @FXML
    private TextField txtId;

    @FXML
    private javafx.scene.control.TextField txtNombre;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtMotivo;

    private ObservableList<Paciente> pacientesObservable;
    private PacienteRepository pacienteRepository;
    @FXML
    public void initialize(){
        pacienteRepository =PacienteRepository.getInstancia();
        configurarTabla();
        cargarPacientes();
        tablaPacientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarPaciente(newValue)
        );

    }
    private void configurarTabla(){
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivoConsulta"));
    }

    private void cargarPacientes() {
        pacientesObservable = FXCollections.observableArrayList(pacienteRepository.getPacientes());
        tablaPacientes.setItems(pacientesObservable);
    }

    private void limpiarCampos(){
        txtId.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtMotivo.clear();
        txtId.setDisable(false);
        txtNombre.setDisable(false);
        tablaPacientes.getSelectionModel().clearSelection();

    }

    @FXML
    void onAgregar() {
        if(!validarCampos()){
            return;
        } try{
            String id = txtId.getText();
            String nombre = txtNombre.getText();
            String telefono = txtTelefono.getText();
            String correo = txtCorreo.getText();
            String motivo = txtMotivo.getText();
            Paciente paciente = new Paciente(id, nombre, telefono, correo, motivo);
            pacienteRepository.agregarPaciente(paciente);
            mostrarAlerta("Éxito", "Paciente agregado correctamente.", Alert.AlertType.INFORMATION);
            actualizarTabla();
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el Paciente: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    private boolean validarCampos(){
        if(txtId.getText().isEmpty() || txtNombre.getText().isEmpty() || txtTelefono.getText().isEmpty()|| txtCorreo.getText().isEmpty()|| txtMotivo.getText().isEmpty()){
            mostrarAlerta("Error", "Todos los campos deben ser diligenciados obligatoriamente.", Alert.AlertType.WARNING);
            return false;
        } return  true;
    }

    @FXML
    void onEliminar() {
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if(paciente == null){
            mostrarAlerta("Error", "Seleccione un paciente para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el paciente: " + paciente.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK){
            try{
                pacienteRepository.eliminarPaciente(paciente);
                cargarPacientes();
                mostrarAlerta("Exito", "paciente eliminado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar el paciente: "+ e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }
    public void actualizarTabla(){cargarPacientes();}
    private void mostrarPaciente(Paciente paciente){
        if(paciente !=null){
            txtId.setText(paciente.getId());
            txtNombre.setText(paciente.getNombre());
            txtTelefono.setText(paciente.getTelefono());
            txtCorreo.setText(paciente.getEmail());
            txtMotivo.setText(paciente.getMotivoConsulta());
        } else{
            limpiarCampos();
        }
    }


    @FXML
    void onModificar() {
        if(!validarCampos()){
            return;
        }
        Paciente paciente = tablaPacientes.getSelectionModel().getSelectedItem();
        if(paciente == null){
            mostrarAlerta("Error", "Seleccione un paciente para modificar.", Alert.AlertType.WARNING);
            return;
        }try{
            paciente.setId(txtId.getText());
            paciente.setNombre(txtNombre.getText());
            paciente.setTelefono(txtTelefono.getText());
            paciente.setEmail(txtCorreo.getText());
            paciente.setMotivoConsulta(txtMotivo.getText());
            actualizarTabla();
            tablaPacientes.refresh();
            mostrarAlerta("Éxito", "Paciente modificado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        }catch (Exception e) {
            mostrarAlerta("Error", "Error al modificar el Paciente: " + e.getMessage(), Alert.AlertType.ERROR);
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
