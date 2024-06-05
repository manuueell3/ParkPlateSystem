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
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;

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
    private Button btnLimpiarFiltros;

    private ObservableList<Pago> listaPagos;
    private Usuario usuario;


    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        try {
            cargarDatosUsuario();
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos del usuario: " + e.getMessage(), Alert.AlertType.ERROR);
        }
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

        filtroFormaPago.setItems(FXCollections.observableArrayList("Todos", "Efectivo", "Tarjeta", "Transferencia"));
        filtroFormaPago.setValue("Todos");
        configurarBusqueda();
    }

    private void cargarDatosUsuario() throws SQLException {
        listaPagos.clear();
        List<Pago> pagos = Pago.obtenerTodos();
        if (pagos.isEmpty()) {
            mostrarAlerta("Información", "No hay pagos registrados.", Alert.AlertType.INFORMATION);
        }
        for (Pago pago : pagos) {
            if (pago.getIdUsuario().equals(usuario.getIdUsuario())) {
                listaPagos.add(pago);
            }
        }
        configurarBusqueda();
    }

    private void configurarBusqueda() {
        FilteredList<Pago> filteredData = new FilteredList<>(listaPagos, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros(filteredData));
        filtroFormaPago.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros(filteredData));
        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros(filteredData));
        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros(filteredData));

        SortedList<Pago> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaPagos.comparatorProperty());

        tablaPagos.setItems(sortedData);
    }

    private void aplicarFiltros(FilteredList<Pago> filteredData) {
        filteredData.setPredicate(pago -> {
            // Filtro de texto
            String filterText = txtBuscar.getText();
            if (filterText != null && !filterText.isEmpty()) {
                String lowerCaseFilter = filterText.toLowerCase();
                if (!String.valueOf(pago.getIdPago()).contains(lowerCaseFilter) &&
                        !String.valueOf(pago.getIdUsuario()).contains(lowerCaseFilter) &&
                        !String.valueOf(pago.getIdRegistro()).contains(lowerCaseFilter) &&
                        !String.valueOf(pago.getIdVehiculo()).contains(lowerCaseFilter) &&
                        !String.valueOf(pago.getMonto()).contains(lowerCaseFilter) &&
                        !pago.getFechaHoraPago().toLowerCase().contains(lowerCaseFilter) &&
                        !pago.getFormaPago().toLowerCase().contains(lowerCaseFilter)) {
                    return false;
                }
            }

            // Filtro de forma de pago
            String formaPago = filtroFormaPago.getValue();
            if (formaPago != null && !formaPago.equalsIgnoreCase("Todos") && !formaPago.isEmpty()) {
                if (!pago.getFormaPago().equalsIgnoreCase(formaPago)) {
                    return false;
                }
            }

            // Filtro de fechas
            LocalDate fechaInicio = filtroFechaInicio.getValue();
            LocalDate fechaFin = filtroFechaFin.getValue();
            LocalDate fechaPago;
            try {
                fechaPago = LocalDate.parse(pago.getFechaHoraPago().split(" ")[0]);
            } catch (Exception e) {
                return false;
            }
            if (fechaInicio != null && fechaPago.isBefore(fechaInicio)) {
                return false;
            }
            if (fechaFin != null && fechaPago.isAfter(fechaFin)) {
                return false;
            }

            return true;
        });
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        filtroFormaPago.setValue("Todos");
        filtroFechaInicio.setValue(null);
        filtroFechaFin.setValue(null);
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
