package com.example.parcialfinal.Controllers;


import com.example.parcialfinal.Models.Citas;
import com.example.parcialfinal.Models.Paciente;
import com.example.parcialfinal.Models.Medico;
import com.example.parcialfinal.Repository.PacienteRepository;
import com.example.parcialfinal.Repository.MedicoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RegistroCitasController {
    @FXML
    private Button btnCitar;
    @FXML
    private ComboBox<Medico> cmbMedico;
    @FXML
    private ComboBox<Paciente> cmbPaciente;
    @FXML
    private TableColumn<Citas, Integer> colPrecio;
    @FXML
    private TableColumn<Citas, String> colMedico;
    @FXML
    private TableColumn<Citas, String> colPaciente;
    @FXML
    private TableColumn<Citas, Date> colFecha;
    @FXML
    private TableColumn<Citas, String> colHora;
    @FXML
    private Spinner<Double> spinnerPrecio;
    @FXML
    private TextField txtHora;
    @FXML
    private Label lblPrecio;


    @FXML
    private TableView<Citas> tablaCitas;
    private ObservableList<Citas> listaCitas;
    private PacienteRepository PacienteRepository;
    private MedicoRepository medicoRepository;
    private double precioUnitario = 0.0;

    @FXML
    public void initialize() {
        pacienteRepository = PacienteRepository.getInstancia();
        medicoRepository = MedicoRepository.getInstancia();
        listaCitas = FXCollections.observableArrayList();
        configurarTabla();
        inicializarComponentes();
        configListeners();
    }

    @FXML
    public void inicializarComponentes() {
        cargarMedicos();
        cargarPacientes();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1);
        spinnerPrecio.setValueFactory(valueFactory);
        lblPrecio.setText("$ 0.00");
    }
    private void configListeners() {
        cmbPacientre.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                precioUnitario = newValue.getPaciente();
                int stock = newValue.getStock();
                lblPrecio.setText("$ "+ String.format("%.2f", precioUnitario));
                actualizarSpinner(stock);
                calcularSubtotal();

            } else{
                lblPrecio.setText("$ 0.00");
                precioUnitario = 0.0;
                actualizarSpinner(1);
            }
        });
        spinnerPrecio.valueProperty().addListener((observable, oldValue, newValue) -> {
            calcularSubtotal();
        });

    }

    private void actualizarSpinner(int stock){
        SpinnerValueFactory.IntegerSpinnerValueFactory factory= (SpinnerValueFactory.IntegerSpinnerValueFactory) spinnerCantidad.getValueFactory();
        factory.setMax(Math.max(1, stock));
        if(spinnerPrecio.getValue()>stock){
            spinnerPrecio.getValueFactory().setValue(stock);
        }
    }
    private void calcularSubtotal() {
        if (precioUnitario > 0 && spinnerPrecio.getValue() != null) {
            int cantidad = spinnerPrecio.getValue();
            double subtotal = precioUnitario * cantidad;
            lblSubtotal.setText("$ " + String.format("%.2f", subtotal));
        } else {
            lblSubtotal.setText("$ 0.00");
        }
    }

    private void cargarMedicos() {
        cmbMedico.setItems(FXCollections.observableArrayList(medicoRepository.getMedicos()));

        cmbMedico.setConverter(new StringConverter<Medico>() {
            @Override
            public String toString(Medico medico) {
                return (medico != null) ? medico.getNombre() : "";
            }
            @Override
            public Medico fromString(String string) {
                return null;
            }
        });
    }
    private void cargarPacientes() {
        cmbPaciente.setItems(FXCollections.observableArrayList(pacienteRepository.getPacientes()));
        cmbPaciente.setConverter(new StringConverter<Paciente>() {
            @Override
            public String toString(Paciente paciente) {
                return (paciente != null) ? paciente.getNombre(): "";
            }

            @Override
            public Paciente fromString(String s) {
                return null;
            }
        });
    }

    @FXML
    void onPrestar(ActionEvent event) {
        Medico medico = cmbMedico.getSelectionModel().getSelectedItem();
        if(medico == null){
            mostrarAlerta("Error de citación", "Debe seleccionar un medico.", Alert.AlertType.WARNING);
            return;
        }
        Paciente paciente = cmbPaciente.getSelectionModel().getSelectedItem();
        if (paciente == null) {
            mostrarAlerta("Error de citación", "Debe seleccionar un paciente.", Alert.AlertType.WARNING);
            return;
        }
        int cantidad = spinnerCantidad.getValue();
        if (cantidad > libro.getStock()) {
            mostrarAlerta("Error de Stock", "No hay suficiente stock. Disponible: " + paciente.getStock(), Alert.AlertType.ERROR);
            return;
        } try{
            int nuevoStock = paciente.getStock()-cantidad;
            paciente.setStock(nuevoStock);
            double subtotal = precioUnitario*cantidad;
            Citas cita = new Citas(
                    LocalDate.now(),
                    medico.getNombre(),
                    paciente.getNombre(),
                    cantidad,
                    subtotal
            );
            listaCitas.add(cita);
            limpiarCampos();
            mostrarAlerta("Citación Exitosa", "cita registrada en la tabla.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al procesar la cita: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    private void limpiarCampos() {
        cmbPaciene.getSelectionModel().clearSelection();
        lblPrecio.setText("$ 0.00");
        SpinnerValueFactory.IntegerSpinnerValueFactory factory =
                (SpinnerValueFactory.DoubleSpinnerValueFactory) spinnerPrecio.getValueFactory();
        factory.setMax(1);
        factory.setValue(1);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void configurarTabla() {
        tablaCitas.setItems(listaCitas);
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medico"));
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("paciente"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        cmbMedico.getSelectionModel().clearSelection();

    }
}
