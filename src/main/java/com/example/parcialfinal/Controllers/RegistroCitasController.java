package com.example.parcialfinal.Controllers;

import com.example.parcialfinal.Models.Citas;
import com.example.parcialfinal.Models.Paciente;
import com.example.parcialfinal.Models.Medico;
import com.example.parcialfinal.Repository.PacienteRepository;
import com.example.parcialfinal.Repository.MedicoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.util.StringConverter;

import java.time.LocalDate;

public class RegistroCitasController {

    @FXML private ComboBox<Medico> cmbMedico;
    @FXML private ComboBox<Paciente> cmbPaciente;
    @FXML private Spinner<Double> spinnerPrecio;
    @FXML private TextField txtHora;
    @FXML private DatePicker datePicker;

    @FXML private TableView<Citas> tablaCitas;
    @FXML private TableColumn<Citas, LocalDate> colFecha;
    @FXML private TableColumn<Citas, String> colHora;
    @FXML private TableColumn<Citas, String> colMedico;
    @FXML private TableColumn<Citas, String> colPaciente;
    @FXML private TableColumn<Citas, Double> colPrecio;

    @FXML private Label lblPrecio;

    private ObservableList<Citas> listaCitas;
    private PacienteRepository pacienteRepository;
    private MedicoRepository medicoRepository;

    @FXML
    public void initialize() {
        pacienteRepository = PacienteRepository.getInstancia();
        medicoRepository = MedicoRepository.getInstancia();
        listaCitas = FXCollections.observableArrayList();

        configurarTabla();
        cargarMedicos();
        cargarPacientes();
        configurarSpinnerPrecio();
    }

    private void configurarSpinnerPrecio() {
        SpinnerValueFactory<Double> factory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 5);
        spinnerPrecio.setValueFactory(factory);

        spinnerPrecio.valueProperty().addListener((obs, oldV, newV) -> {
            lblPrecio.setText("$ " + String.format("%.2f", newV));
        });
    }

    private void cargarMedicos() {
        cmbMedico.setItems(medicoRepository.getMedicos());

        cmbMedico.setConverter(new StringConverter<Medico>() {
            @Override
            public String toString(Medico m) {
                return (m == null) ? "" : m.getNombre();
            }
            @Override
            public Medico fromString(String s) { return null; }
        });
    }

    private void cargarPacientes() {
        cmbPaciente.setItems((ObservableList<Paciente>) pacienteRepository.getPacientes());

        cmbPaciente.setConverter(new StringConverter<Paciente>() {
            @Override
            public String toString(Paciente p) {
                return (p == null) ? "" : p.getNombre();
            }
            @Override
            public Paciente fromString(String s) { return null; }
        });
    }

    private void configurarTabla() {
        tablaCitas.setItems(listaCitas);

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medico"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
    }

    @FXML
    void onCitar(ActionEvent event) {
        Medico medico = cmbMedico.getSelectionModel().getSelectedItem();
        Paciente paciente = cmbPaciente.getSelectionModel().getSelectedItem();
        LocalDate fecha = datePicker.getValue();
        String hora = txtHora.getText();
        double precio = spinnerPrecio.getValue();

        if (medico == null) {
            mostrarAlerta("Error", "Debe seleccionar un médico.", Alert.AlertType.WARNING);
            return;
        }
        if (paciente == null) {
            mostrarAlerta("Error", "Debe seleccionar un paciente.", Alert.AlertType.WARNING);
            return;
        }
        if (fecha == null) {
            mostrarAlerta("Error", "Debe seleccionar una fecha.", Alert.AlertType.WARNING);
            return;
        }
        if (hora.isEmpty()) {
            mostrarAlerta("Error", "Debe escribir la hora.", Alert.AlertType.WARNING);
            return;
        }

        Citas cita = new Citas(
                fecha,
                hora,
                medico.getNombre(),
                paciente.getNombre(),
                precio
        );

        listaCitas.add(cita);
        limpiarCampos();

        mostrarAlerta("Éxito", "La cita fue registrada correctamente.", Alert.AlertType.INFORMATION);
    }

    private void limpiarCampos() {
        cmbMedico.getSelectionModel().clearSelection();
        cmbPaciente.getSelectionModel().clearSelection();
        txtHora.clear();
        datePicker.setValue(null);

        spinnerPrecio.getValueFactory().setValue(0.0);
        lblPrecio.setText("$ 0.00");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
