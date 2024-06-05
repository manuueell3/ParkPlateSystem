package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Sancion;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.beans.property.SimpleStringProperty;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorSanciones {

    @FXML
    private TableView<Sancion> tablaSanciones;
    @FXML
    private TableColumn<Sancion, Long> columnaIdSancion;
    @FXML
    private TableColumn<Sancion, Long> columnaIdVehiculo;
    @FXML
    private TableColumn<Sancion, Long> columnaIdUsuario;
    @FXML
    private TableColumn<Sancion, String> columnaMotivo;
    @FXML
    private TableColumn<Sancion, Double> columnaMonto;
    @FXML
    private TableColumn<Sancion, String> columnaFechaHora;
    @FXML
    private TableColumn<Sancion, String> columnaFechaMaxPago;
    @FXML
    private TextField txtBuscar;
    @FXML
    private DatePicker filtroFechaInicio;
    @FXML
    private DatePicker filtroFechaFin;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;
    @FXML
    private Button btnPagar;
    @FXML
    private Button btnDisputar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label labelDetalles;

    private ObservableList<Sancion> listaSanciones;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorSanciones.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    @FXML
    public void initialize() {
        columnaIdSancion.setCellValueFactory(new PropertyValueFactory<>("idSancion"));
        columnaIdVehiculo.setCellValueFactory(new PropertyValueFactory<>("idVehiculo"));
        columnaIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnaMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        columnaMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        columnaFechaHora.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHoraFormateada()));
        columnaFechaMaxPago.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaMaxPagoFormateada()));

        listaSanciones = FXCollections.observableArrayList();
        tablaSanciones.setItems(listaSanciones);

        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaSanciones.clear();
        try {
            List<Sancion> sanciones = Sancion.obtenerTodas();
            for (Sancion sancion : sanciones) {
                if (sancion.getIdUsuario().equals(usuario.getIdUsuario())) {
                    listaSanciones.add(sancion);
                }
            }
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar las sanciones del usuario.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al cargar las sanciones del usuario.", e);
        }
    }

    private void configurarBusqueda() {
        FilteredList<Sancion> filteredData = new FilteredList<>(listaSanciones, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sancion -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return sancion.getMotivo().toLowerCase().contains(lowerCaseFilter) ||
                        String.valueOf(sancion.getMonto()).contains(lowerCaseFilter) ||
                        sancion.getFechaHoraFormateada().toLowerCase().contains(lowerCaseFilter) ||
                        sancion.getFechaMaxPagoFormateada().toLowerCase().contains(lowerCaseFilter);
            });
        });

        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sancion -> {
                if (newValue == null) {
                    return true;
                }
                return sancion.getFechaHora().toLocalDate().compareTo(newValue) >= 0;
            });
        });

        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sancion -> {
                if (newValue == null) {
                    return true;
                }
                return sancion.getFechaHora().toLocalDate().compareTo(newValue) <= 0;
            });
        });

        SortedList<Sancion> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaSanciones.comparatorProperty());

        tablaSanciones.setItems(sortedData);
    }

    @FXML
    private void buscarSancion() {
        try {
            configurarBusqueda();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al realizar la búsqueda de sanciones.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al realizar la búsqueda de sanciones.", e);
        }
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        filtroFechaInicio.setValue(null);
        filtroFechaFin.setValue(null);
        try {
            configurarBusqueda();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al limpiar los filtros de búsqueda.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al limpiar los filtros de búsqueda.", e);
        }
    }

    @FXML
    private void pagarSancion() {
        Sancion sancionSeleccionada = tablaSanciones.getSelectionModel().getSelectedItem();
        if (sancionSeleccionada != null) {
            try {
                sancionSeleccionada.eliminar();
                tablaSanciones.getItems().remove(sancionSeleccionada);
                labelDetalles.setText("Sanción pagada exitosamente.");
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al pagar la sanción.", Alert.AlertType.ERROR);
                LOGGER.log(Level.SEVERE, "Error al pagar la sanción.", e);
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, seleccione una sanción para pagar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void disputarSancion() {
        Sancion sancionSeleccionada = tablaSanciones.getSelectionModel().getSelectedItem();
        if (sancionSeleccionada != null) {
            try {
                sancionSeleccionada.setMotivo(sancionSeleccionada.getMotivo() + " - Disputada");
                sancionSeleccionada.actualizar();
                tablaSanciones.refresh();
                labelDetalles.setText("Sanción disputada exitosamente.");
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al disputar la sanción.", Alert.AlertType.ERROR);
                LOGGER.log(Level.SEVERE, "Error al disputar la sanción.", e);
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, seleccione una sanción para disputar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaSanciones.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalUsuario.fxml"));
            Parent root = loader.load();

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
