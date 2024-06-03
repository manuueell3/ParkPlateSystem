package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Pago;
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

public class ControladorPagos {

    @FXML
    private TableView<Pago> tablaPagos;
    @FXML
    private TableColumn<Pago, Long> columnaIdPago;
    @FXML
    private TableColumn<Pago, Long> columnaIdUsuario;
    @FXML
    private TableColumn<Pago, Long> columnaIdRegistro;
    @FXML
    private TableColumn<Pago, Long> columnaIdVehiculo;
    @FXML
    private TableColumn<Pago, Double> columnaMonto;
    @FXML
    private TableColumn<Pago, String> columnaFechaHoraPago;
    @FXML
    private TableColumn<Pago, String> columnaFormaPago;
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> filtroFormaPago;
    @FXML
    private DatePicker filtroFechaInicio;
    @FXML
    private DatePicker filtroFechaFin;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;

    private ObservableList<Pago> listaPagos;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorPagos.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    @FXML
    public void initialize() {
        columnaIdPago.setCellValueFactory(new PropertyValueFactory<>("idPago"));
        columnaIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnaIdRegistro.setCellValueFactory(new PropertyValueFactory<>("idRegistro"));
        columnaIdVehiculo.setCellValueFactory(new PropertyValueFactory<>("idVehiculo"));
        columnaMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        columnaFechaHoraPago.setCellValueFactory(new PropertyValueFactory<>("fechaHoraPago"));
        columnaFormaPago.setCellValueFactory(new PropertyValueFactory<>("formaPago"));

        listaPagos = FXCollections.observableArrayList();
        tablaPagos.setItems(listaPagos);

        filtroFormaPago.setItems(FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia"));
        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaPagos.clear();
        List<Pago> pagos = Pago.obtenerTodos();
        for (Pago pago : pagos) {
            if (pago.getIdUsuario().equals(usuario.getIdUsuario())) {
                listaPagos.add(pago);
            }
        }
    }

    private void configurarBusqueda() {
        FilteredList<Pago> filteredData = new FilteredList<>(listaPagos, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pago -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(pago.getIdPago()).contains(lowerCaseFilter) ||
                        String.valueOf(pago.getIdUsuario()).contains(lowerCaseFilter) ||
                        String.valueOf(pago.getIdRegistro()).contains(lowerCaseFilter) ||
                        String.valueOf(pago.getIdVehiculo()).contains(lowerCaseFilter) ||
                        String.valueOf(pago.getMonto()).contains(lowerCaseFilter) ||
                        pago.getFechaHoraPago().toLowerCase().contains(lowerCaseFilter) ||
                        pago.getFormaPago().toLowerCase().contains(lowerCaseFilter);
            });
        });

        filtroFormaPago.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pago -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return pago.getFormaPago().equalsIgnoreCase(newValue);
            });
        });

        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pago -> {
                if (newValue == null) {
                    return true;
                }
                return pago.getFechaHoraPago().compareTo(newValue.toString()) >= 0;
            });
        });

        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(pago -> {
                if (newValue == null) {
                    return true;
                }
                return pago.getFechaHoraPago().compareTo(newValue.toString()) <= 0;
            });
        });

        SortedList<Pago> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaPagos.comparatorProperty());

        tablaPagos.setItems(sortedData);
    }

    @FXML
    private void buscar(ActionEvent event) {
        configurarBusqueda();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        filtroFormaPago.getSelectionModel().clearSelection();
        filtroFechaInicio.setValue(null);
        filtroFechaFin.setValue(null);
        configurarBusqueda();
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaPagos.getScene().getWindow();
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
