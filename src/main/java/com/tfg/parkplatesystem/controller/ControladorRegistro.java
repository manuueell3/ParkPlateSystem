package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Registro;
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

public class ControladorRegistro {

    @FXML
    private TableView<Registro> tablaRegistros;
    @FXML
    private TableColumn<Registro, Long> columnaIdRegistro;
    @FXML
    private TableColumn<Registro, Long> columnaIdUsuario;
    @FXML
    private TableColumn<Registro, String> columnaTipo;
    @FXML
    private TableColumn<Registro, String> columnaFecha;
    @FXML
    private TableColumn<Registro, String> columnaEstado;
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> filtroEstado;
    @FXML
    private DatePicker filtroFechaInicio;
    @FXML
    private DatePicker filtroFechaFin;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;

    private ObservableList<Registro> listaRegistros;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorRegistro.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    @FXML
    public void initialize() {
        columnaIdRegistro.setCellValueFactory(new PropertyValueFactory<>("idRegistro"));
        columnaIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        listaRegistros = FXCollections.observableArrayList();
        tablaRegistros.setItems(listaRegistros);

        filtroEstado.setItems(FXCollections.observableArrayList("pendiente", "completado"));
        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaRegistros.clear();
        List<Registro> registros = Registro.obtenerTodos();
        for (Registro registro : registros) {
            if (registro.getIdUsuario().equals(usuario.getIdUsuario())) {
                listaRegistros.add(registro);
            }
        }
    }

    private void configurarBusqueda() {
        FilteredList<Registro> filteredData = new FilteredList<>(listaRegistros, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(registro -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(registro.getIdRegistro()).contains(lowerCaseFilter) ||
                        String.valueOf(registro.getIdUsuario()).contains(lowerCaseFilter) ||
                        registro.getTipo().toLowerCase().contains(lowerCaseFilter) ||
                        registro.getFecha().toLowerCase().contains(lowerCaseFilter) ||
                        registro.getEstado().toLowerCase().contains(lowerCaseFilter);
            });
        });

        filtroEstado.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(registro -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return registro.getEstado().equalsIgnoreCase(newValue);
            });
        });

        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(registro -> {
                if (newValue == null) {
                    return true;
                }
                return registro.getFecha().compareTo(newValue.toString()) >= 0;
            });
        });

        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(registro -> {
                if (newValue == null) {
                    return true;
                }
                return registro.getFecha().compareTo(newValue.toString()) <= 0;
            });
        });

        SortedList<Registro> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaRegistros.comparatorProperty());

        tablaRegistros.setItems(sortedData);
    }

    @FXML
    private void buscar(ActionEvent event) {
        configurarBusqueda();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        filtroEstado.getSelectionModel().clearSelection();
        filtroFechaInicio.setValue(null);
        filtroFechaFin.setValue(null);
        configurarBusqueda();
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaRegistros.getScene().getWindow();
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
            LOGGER.log(Level.SEVERE, "Error al cargar la vista principal", e);
            mostrarAlerta("Error", "No se pudo cargar el menú principal.", Alert.AlertType.ERROR);
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
