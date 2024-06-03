package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Sancion;
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
        columnaFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        columnaFechaMaxPago.setCellValueFactory(new PropertyValueFactory<>("fechaMaxPago"));

        listaSanciones = FXCollections.observableArrayList();
        tablaSanciones.setItems(listaSanciones);

        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaSanciones.clear();
        List<Sancion> sanciones = Sancion.obtenerTodas();
        for (Sancion sancion : sanciones) {
            if (sancion.getIdUsuario().equals(usuario.getIdUsuario())) {
                listaSanciones.add(sancion);
            }
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
                        sancion.getFechaHora().toLowerCase().contains(lowerCaseFilter) ||
                        sancion.getFechaMaxPago().toLowerCase().contains(lowerCaseFilter);
            });
        });

        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sancion -> {
                if (newValue == null) {
                    return true;
                }
                return sancion.getFechaHora().compareTo(newValue.toString()) >= 0;
            });
        });

        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(sancion -> {
                if (newValue == null) {
                    return true;
                }
                return sancion.getFechaHora().compareTo(newValue.toString()) <= 0;
            });
        });

        SortedList<Sancion> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaSanciones.comparatorProperty());

        tablaSanciones.setItems(sortedData);
    }

    @FXML
    private void buscarSancion() {
        configurarBusqueda();
    }

    @FXML
    private void limpiarFiltros() {
        txtBuscar.clear();
        filtroFechaInicio.setValue(null);
        filtroFechaFin.setValue(null);
        configurarBusqueda();
    }

    @FXML
    private void pagarSancion() {
        Sancion sancionSeleccionada = tablaSanciones.getSelectionModel().getSelectedItem();
        if (sancionSeleccionada != null) {
            sancionSeleccionada.eliminar();
            tablaSanciones.getItems().remove(sancionSeleccionada);
            labelDetalles.setText("Sanción pagada exitosamente.");
        }
    }

    @FXML
    private void disputarSancion() {
        Sancion sancionSeleccionada = tablaSanciones.getSelectionModel().getSelectedItem();
        if (sancionSeleccionada != null) {
            sancionSeleccionada.setMotivo(sancionSeleccionada.getMotivo() + " - Disputada");
            sancionSeleccionada.actualizar();
            tablaSanciones.refresh();
            labelDetalles.setText("Sanción disputada exitosamente.");
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
