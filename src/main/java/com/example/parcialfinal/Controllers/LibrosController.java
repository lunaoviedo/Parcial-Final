package com.example.parcialfinal.Controllers;

import com.example.parcialfinal.Models.Libro;
import com.example.parcialfinal.Repository.LibroRepository;
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

public class LibrosController {
    @FXML
    private Button btnAgregar;

    @FXML
    private Button btnEliminar;

    @FXML
    private Button btnModificar;

    @FXML
    private TableColumn<Libro, String> colCodigo;

    @FXML
    private TableColumn<Libro, String> colNombre;

    @FXML
    private TableColumn<Libro, Double> colPrecio;

    @FXML
    private TableColumn<Libro, Integer> colStock;
    @FXML
    private TableView<Libro> tablaLibros;
    @FXML
    private TextField txtCodigo;

    @FXML
    private javafx.scene.control.TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtStock;
    private ObservableList<Libro> librosObservable;
    private LibroRepository libroRepository;
    @FXML
    public void initialize(){
        libroRepository =LibroRepository.getInstancia();
        configurarTabla();
        cargarLibros();
        tablaLibros.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> mostrarLibro(newValue)
        );

    }
    private void configurarTabla(){
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
    }

    private void cargarLibros() {
        librosObservable = FXCollections.observableArrayList(libroRepository.getLibros());
        tablaLibros.setItems(librosObservable);
    }

    private void limpiarCampos(){
        txtCodigo.clear();
        txtNombre.clear();
        txtPrecio.clear();
        txtStock.clear();
        txtCodigo.setDisable(false);
        txtNombre.setDisable(false);
        tablaLibros.getSelectionModel().clearSelection();

    }

    @FXML
    void onAgregar() {
        if(!validarCampos()){
            return;
        } try{
            String codigo = txtCodigo.getText();
            String name = txtNombre.getText();
            Double precio;
            int stock;
            try{
                precio = Double.parseDouble(txtPrecio.getText());
                stock = Integer.parseInt(txtStock.getText());
            }catch (NumberFormatException e) {
                mostrarAlerta("Error de Formato", "El precio y el stock deben ser números válidos.", Alert.AlertType.ERROR);
                return;
            }
            Libro libro = new Libro(codigo, name, precio, stock);
            libroRepository.agregarLibro(libro);
            mostrarAlerta("Éxito", "Libro agregado correctamente.", Alert.AlertType.INFORMATION);
            actualizarTabla();
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar el Libro: " + e.getMessage(), Alert.AlertType.ERROR);
        }

    }
    private boolean validarCampos(){
        if(txtCodigo.getText().isEmpty() || txtNombre.getText().isEmpty() || txtPrecio.getText().isEmpty()|| txtStock.getText().isEmpty()){
            mostrarAlerta("Error", "Todos los campos deben ser diligenciados obligatoriamente.", Alert.AlertType.WARNING);
            return false;
        } return  true;
    }

    @FXML
    void onEliminar() {
        Libro libro = tablaLibros.getSelectionModel().getSelectedItem();
        if(libro == null){
            mostrarAlerta("Error", "Seleccione un libro para eliminar.", Alert.AlertType.WARNING);
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText("¿Estás seguro?");
        confirmacion.setContentText("¿Deseas eliminar el libro: " + libro.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if(resultado.isPresent() && resultado.get() == ButtonType.OK){
            try{
                libroRepository.eliminarLibro(libro);
                cargarLibros();
                mostrarAlerta("Exito", "libro eliminado correctamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar el libro: "+ e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }
    public void actualizarTabla(){cargarLibros();}
    private void mostrarLibro(Libro libro){
        if(libro !=null){
            txtCodigo.setText(libro.getId());
            txtNombre.setText(libro.getNombre());
            txtPrecio.setText(String.valueOf(libro.getPrecio()));
            txtStock.setText(String.valueOf(libro.getStock()));
        } else{
            limpiarCampos();
        }
    }


    @FXML
    void onModificar() {
        if(!validarCampos()){
            return;
        }
        Libro libro = tablaLibros.getSelectionModel().getSelectedItem();
        if(libro == null){
            mostrarAlerta("Error", "Seleccione un libro para modificar.", Alert.AlertType.WARNING);
            return;
        }try{
            libro.setId(txtCodigo.getText());
            libro.setNombre(txtNombre.getText());
            libro.setPrecio(Double.parseDouble(txtPrecio.getText()));
            libro.setStock(Integer.parseInt(txtStock.getText()));
            actualizarTabla();
            tablaLibros.refresh();
            mostrarAlerta("Éxito", "Libro modificado correctamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        }catch (Exception e) {
            mostrarAlerta("Error", "Error al modificar el Libro: " + e.getMessage(), Alert.AlertType.ERROR);
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
