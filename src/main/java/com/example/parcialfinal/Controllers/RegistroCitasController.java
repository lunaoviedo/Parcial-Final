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

        txtHora.textProperty().addListener((obs, oldValue, newValue) -> {

            newValue = newValue.replaceAll("[^0-9]", "");

            if (newValue.length() > 4) {
                newValue = newValue.substring(0, 4);
            }
            if (newValue.length() >= 3) {
                newValue = newValue.substring(0, 2) + ":" + newValue.substring(2);
            }
            if (!txtHora.getText().equals(newValue)) {
                txtHora.setText(newValue);
            }
        });
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc;");
                }
            }
        });

        configurarTabla();
        cargarMedicos();
        cargarPacientes();
        configurarSpinnerPrecio();
    }

    private void configurarSpinnerPrecio() {
        SpinnerValueFactory<Double> factory =
                new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 500);
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
        ObservableList<Paciente> listaPacientes =
                FXCollections.observableArrayList(pacienteRepository.getPacientes());

        cmbPaciente.setItems(listaPacientes);

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

    private boolean existeConflictoMedico(String nombreMedico, LocalDate fecha, String hora) {
        for (Citas c : listaCitas) {
            if (c.getMedico().equals(nombreMedico)
                    && c.getFecha().equals(fecha)
                    && c.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
    }

    private boolean existeConflictoPaciente(String nombrePaciente, LocalDate fecha, String hora) {
        for (Citas c : listaCitas) {
            if (c.getPaciente().equals(nombrePaciente)
                    && c.getFecha().equals(fecha)
                    && c.getHora().equals(hora)) {
                return true;
            }
        }
        return false;
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
        if (existeConflictoMedico(medico.getNombre(), fecha, hora)) {
            mostrarAlerta("Conflicto de horario",
                    "El médico ya tiene una cita a esa fecha y hora.",
                    Alert.AlertType.ERROR);
            return;
        }

        if (existeConflictoPaciente(paciente.getNombre(), fecha, hora)) {
            mostrarAlerta("Conflicto de horario",
                    "El paciente ya tiene una cita a esa fecha y hora.",
                    Alert.AlertType.ERROR);
            return;
        }

        if (!hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            mostrarAlerta(
                    "Formato incorrecto",
                    "La hora debe estar en formato HH:MM (por ejemplo: 08:30, 14:00)",
                    Alert.AlertType.WARNING
            );
            return;
        }
        if (!hora.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            mostrarAlerta(
                    "Formato incorrecto",
                    "La hora debe ser válida en formato HH:MM (ej: 08:30, 14:00)",
                    Alert.AlertType.WARNING
            );
            return;
        }
        if (fecha.isBefore(LocalDate.now())) {
            mostrarAlerta("Fecha inválida", "No puede registrar una cita con una fecha pasada.", Alert.AlertType.WARNING);
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
