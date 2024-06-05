package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Tarjeta;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorTarjetas {

    @FXML
    private TableView<Tarjeta> tablaTarjetas;
    @FXML
    private TableColumn<Tarjeta, Long> columnaIdTarjeta;
    @FXML
    private TableColumn<Tarjeta, Long> columnaIdUsuario;
    @FXML
    private TableColumn<Tarjeta, String> columnaNumeroTarjeta;
    @FXML
    private TableColumn<Tarjeta, String> columnaFechaExpiracion;
    @FXML
    private TableColumn<Tarjeta, String> columnaCvv;
    @FXML
    private TextField txtBuscar;
    @FXML
    private TextField txtNumeroTarjeta;
    @FXML
    private DatePicker txtFechaExpiracion;
    @FXML
    private TextField txtCvv;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;

    private ObservableList<Tarjeta> listaTarjetas;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorTarjetas.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    @FXML
    public void initialize() {
        columnaIdTarjeta.setCellValueFactory(new PropertyValueFactory<>("idTarjeta"));
        columnaIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnaNumeroTarjeta.setCellValueFactory(new PropertyValueFactory<>("numeroTarjeta"));
        columnaFechaExpiracion.setCellValueFactory(new PropertyValueFactory<>("fechaExpiracion"));
        columnaCvv.setCellValueFactory(new PropertyValueFactory<>("cvv"));

        listaTarjetas = FXCollections.observableArrayList();
        tablaTarjetas.setItems(listaTarjetas);

        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaTarjetas.clear();
        try {
            List<Tarjeta> tarjetas = Tarjeta.obtenerTodas();
            for (Tarjeta tarjeta : tarjetas) {
                if (tarjeta.getIdUsuario().equals(usuario.getIdUsuario())) {
                    listaTarjetas.add(tarjeta);
                }
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar las tarjetas del usuario.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al cargar las tarjetas del usuario.", e);
        }
    }

    private void configurarBusqueda() {
        FilteredList<Tarjeta> filteredData = new FilteredList<>(listaTarjetas, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tarjeta -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return tarjeta.getNumeroTarjeta().toLowerCase().contains(lowerCaseFilter) ||
                        tarjeta.getFechaExpiracion().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Tarjeta> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaTarjetas.comparatorProperty());

        tablaTarjetas.setItems(sortedData);
    }

    @FXML
    private void buscar(ActionEvent event) {
        try {
            configurarBusqueda();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al realizar la búsqueda de tarjetas.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al realizar la búsqueda de tarjetas.", e);
        }
    }

    @FXML
    private void agregarTarjeta(ActionEvent event) {
        try {
            String numeroTarjeta = txtNumeroTarjeta.getText();
            String fechaExpiracion = txtFechaExpiracion.getValue().toString();
            String cvv = txtCvv.getText();

            if (numeroTarjeta.isEmpty() || fechaExpiracion.isEmpty() || cvv.isEmpty()) {
                mostrarAlerta("Campos vacíos", "Por favor, complete todos los campos.", Alert.AlertType.WARNING);
                return;
            }

            Tarjeta nuevaTarjeta = new Tarjeta(null, usuario.getIdUsuario(), numeroTarjeta, fechaExpiracion, cvv);
            nuevaTarjeta.guardar();
            cargarDatosUsuario();
            mostrarAlerta("Éxito", "Tarjeta agregada exitosamente.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al agregar la tarjeta.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al agregar la tarjeta.", e);
        }
    }

    @FXML
    private void actualizarTarjeta(ActionEvent event) {
        Tarjeta tarjetaSeleccionada = tablaTarjetas.getSelectionModel().getSelectedItem();
        if (tarjetaSeleccionada != null) {
            try {
                String numeroTarjeta = txtNumeroTarjeta.getText();
                String fechaExpiracion = txtFechaExpiracion.getValue().toString();
                String cvv = txtCvv.getText();

                if (numeroTarjeta.isEmpty() || fechaExpiracion.isEmpty() || cvv.isEmpty()) {
                    mostrarAlerta("Campos vacíos", "Por favor, complete todos los campos.", Alert.AlertType.WARNING);
                    return;
                }

                tarjetaSeleccionada.setNumeroTarjeta(numeroTarjeta);
                tarjetaSeleccionada.setFechaExpiracion(fechaExpiracion);
                tarjetaSeleccionada.setCvv(cvv);
                tarjetaSeleccionada.actualizar();
                cargarDatosUsuario();
                mostrarAlerta("Éxito", "Tarjeta actualizada exitosamente.", Alert.AlertType.INFORMATION);
                limpiarCampos();
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al actualizar la tarjeta.", Alert.AlertType.ERROR);
                LOGGER.log(Level.SEVERE, "Error al actualizar la tarjeta.", e);
            }
        } else {
            mostrarAlerta("Selección vacía", "Por favor, seleccione una tarjeta para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarTarjeta(ActionEvent event) {
        Tarjeta tarjetaSeleccionada = tablaTarjetas.getSelectionModel().getSelectedItem();
        if (tarjetaSeleccionada != null) {
            try {
                tarjetaSeleccionada.eliminar();
                cargarDatosUsuario();
                mostrarAlerta("Éxito", "Tarjeta eliminada exitosamente.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar la tarjeta.", Alert.AlertType.ERROR);
                LOGGER.log(Level.SEVERE, "Error al eliminar la tarjeta.", e);
            }
        } else {
            mostrarAlerta("Selección vacía", "Por favor, seleccione una tarjeta para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        txtNumeroTarjeta.clear();
        txtFechaExpiracion.setValue(null);
        txtCvv.clear();
        try {
            configurarBusqueda();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al limpiar los filtros de búsqueda.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al limpiar los filtros de búsqueda.", e);
        }
    }

    private void limpiarCampos() {
        txtNumeroTarjeta.clear();
        txtFechaExpiracion.setValue(null);
        txtCvv.clear();
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaTarjetas.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalUsuario.fxml"));
            Parent root = loader.load();

            // Pasa el usuario al controlador de la vista principal
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Menú Principal");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo cargar el menú principal.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al cargar la vista principal", e);
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
