package com.example.parcialfinal.Controllers;


import com.example.parcialfinal.Models.Paciente;
import com.example.parcialfinal.Models.Usuario;
import com.example.parcialfinal.Models.Citas;
import com.example.parcialfinal.Repository.LibroRepository;
import com.example.parcialfinal.Repository.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import javafx.fxml.FXML;
import javafx.scene.control.*;
        import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PrestamosController {
    @FXML
    private Button btnVender;
    @FXML
    private ComboBox<Usuario> cmbUsuario;
    @FXML
    private ComboBox<Paciente> cmbLibro;
    @FXML
    private TableColumn<Citas, Integer> colCantidad;
    @FXML
    private TableColumn<Citas, String> colUsuario;
    @FXML
    private TableColumn<Citas, String> colLibro;
    @FXML
    private TableColumn<Citas, LocalDate> colFecha;
    @FXML
    private TableColumn<Citas, Double> colSubtotal;
    @FXML
    private Spinner<Integer> spinnerCantidad;
    @FXML
    private Label lblHora;
    @FXML
    private Label lblPrecio;
    @FXML
    private Label lblSubtotal;

    @FXML
    private TableView<Citas> tablaPrestamos;
    private ObservableList<Citas> listaPrestamos;
    private LibroRepository libroRepository;
    private UsuarioRepository usuarioRepository;
    private double precioUnitario = 0.0;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() {
        libroRepository = LibroRepository.getInstancia();
        usuarioRepository = UsuarioRepository.getInstancia();
        listaPrestamos = FXCollections.observableArrayList();
        configurarTabla();
        inicializarComponentes();
        configListeners();
        actualizarHora();
    }
    @FXML
    public void inicializarComponentes() {
        cargarUsuarios();
        cargarLibros();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1, 1);
        spinnerCantidad.setValueFactory(valueFactory);
        lblPrecio.setText("$ 0.00");
    }
    private void configListeners() {
        cmbLibro.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                precioUnitario = newValue.getPrecio();
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
        spinnerCantidad.valueProperty().addListener((observable, oldValue, newValue) -> {
            calcularSubtotal();
        });

    }
    private void actualizarHora(){
        lblHora.setText(LocalDateTime.now().format(TIME_FORMATTER));
    }

    private void actualizarSpinner(int stock){
        SpinnerValueFactory.IntegerSpinnerValueFactory factory= (SpinnerValueFactory.IntegerSpinnerValueFactory) spinnerCantidad.getValueFactory();
        factory.setMax(Math.max(1, stock));
        if(spinnerCantidad.getValue()>stock){
            spinnerCantidad.getValueFactory().setValue(stock);
        }
    }
    private void calcularSubtotal() {
        if (precioUnitario > 0 && spinnerCantidad.getValue() != null) {
            int cantidad = spinnerCantidad.getValue();
            double subtotal = precioUnitario * cantidad;
            lblSubtotal.setText("$ " + String.format("%.2f", subtotal));
        } else {
            lblSubtotal.setText("$ 0.00");
        }
    }

    private void cargarUsuarios() {
        cmbUsuario.setItems(FXCollections.observableArrayList(usuarioRepository.getUsuarios()));

        cmbUsuario.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) {
                return (usuario != null) ? usuario.getNombre() : "";
            }
            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });
    }
    private void cargarLibros() {
        cmbLibro.setItems(FXCollections.observableArrayList(libroRepository.getLibros()));
        cmbLibro.setConverter(new StringConverter<Paciente>() {
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
        Usuario usuario = cmbUsuario.getSelectionModel().getSelectedItem();
        if(usuario == null){
            mostrarAlerta("Error de Prestamo", "Debe seleccionar un usuario.", Alert.AlertType.WARNING);
            return;
        }
        Paciente paciente = cmbLibro.getSelectionModel().getSelectedItem();
        if (paciente == null) {
            mostrarAlerta("Error de Prestamo", "Debe seleccionar un libro.", Alert.AlertType.WARNING);
            return;
        }
        int cantidad = spinnerCantidad.getValue();
        if (cantidad > paciente.getStock()) {
            mostrarAlerta("Error de Stock", "No hay suficiente stock. Disponible: " + paciente.getStock(), Alert.AlertType.ERROR);
            return;
        } try{
            int nuevoStock = paciente.getStock()-cantidad;
            paciente.setStock(nuevoStock);
            double subtotal = precioUnitario*cantidad;
            Citas detalle = new Citas(
                    LocalDate.now(),
                    usuario.getNombre(),
                    paciente.getNombre(),
                    cantidad,
                    subtotal
            );
            listaPrestamos.add(detalle);
            limpiarCampos();
            mostrarAlerta("Prestamo Exitosa", "prestamo registrado en la tabla.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "Error al procesar el prestamo: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    private void limpiarCampos() {
        cmbLibro.getSelectionModel().clearSelection();
        lblPrecio.setText("$ 0.00");
        lblSubtotal.setText("$ 0.00");
        SpinnerValueFactory.IntegerSpinnerValueFactory factory =
                (SpinnerValueFactory.IntegerSpinnerValueFactory) spinnerCantidad.getValueFactory();
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
        tablaPrestamos.setItems(listaPrestamos);
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colLibro.setCellValueFactory(new PropertyValueFactory<>("libro"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        actualizarHora();
        cmbUsuario.getSelectionModel().clearSelection();

    }
}
