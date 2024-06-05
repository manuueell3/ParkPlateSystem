package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Notificacion;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorNotificaciones {

    @FXML
    private TableView<Notificacion> tablaNotificaciones;
    @FXML
    private TableColumn<Notificacion, Long> columnaIdNotificacion;
    @FXML
    private TableColumn<Notificacion, Long> columnaIdUsuario;
    @FXML
    private TableColumn<Notificacion, String> columnaMensaje;
    @FXML
    private TableColumn<Notificacion, Boolean> columnaLeida;
    @FXML
    private TableColumn<Notificacion, String> columnaFechaHora;
    @FXML
    private TextField txtBuscar;
    @FXML
    private DatePicker filtroFechaInicio;
    @FXML
    private DatePicker filtroFechaFin;
    @FXML
    private Button btnMarcarComoLeida;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;

    private ObservableList<Notificacion> listaNotificaciones;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorNotificaciones.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    @FXML
    public void initialize() {
        columnaIdNotificacion.setCellValueFactory(new PropertyValueFactory<>("idNotificacion"));
        columnaIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnaMensaje.setCellValueFactory(new PropertyValueFactory<>("mensaje"));
        columnaLeida.setCellValueFactory(new PropertyValueFactory<>("leida"));
        columnaFechaHora.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHoraFormateada()));

        listaNotificaciones = FXCollections.observableArrayList();
        tablaNotificaciones.setItems(listaNotificaciones);

        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaNotificaciones.clear();
        try {
            List<Notificacion> notificaciones = Notificacion.obtenerPorUsuario(usuario.getIdUsuario());
            listaNotificaciones.addAll(notificaciones);
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al cargar las notificaciones del usuario.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al cargar las notificaciones del usuario.", e);
        }
    }

    private void configurarBusqueda() {
        FilteredList<Notificacion> filteredData = new FilteredList<>(listaNotificaciones, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(notificacion -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return String.valueOf(notificacion.getIdNotificacion()).contains(lowerCaseFilter) ||
                        notificacion.getMensaje().toLowerCase().contains(lowerCaseFilter) ||
                        notificacion.getFechaHoraFormateada().toLowerCase().contains(lowerCaseFilter);
            });
        });

        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(notificacion -> {
                if (newValue == null) {
                    return true;
                }
                return notificacion.getFechaHora().toLocalDate().compareTo(newValue) >= 0;
            });
        });

        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(notificacion -> {
                if (newValue == null) {
                    return true;
                }
                return notificacion.getFechaHora().toLocalDate().compareTo(newValue) <= 0;
            });
        });

        SortedList<Notificacion> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaNotificaciones.comparatorProperty());

        tablaNotificaciones.setItems(sortedData);
    }

    @FXML
    private void buscar(ActionEvent event) {
        try {
            configurarBusqueda();
        } catch (Exception e) {
            mostrarAlerta("Error", "Error al realizar la búsqueda de notificaciones.", Alert.AlertType.ERROR);
            LOGGER.log(Level.SEVERE, "Error al realizar la búsqueda de notificaciones.", e);
        }
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
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
    private void marcarComoLeida(ActionEvent event) {
        Notificacion selected = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setLeida(true);
                selected.actualizar();
                tablaNotificaciones.refresh();
                mostrarAlerta("Éxito", "Notificación marcada como leída.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al marcar la notificación como leída.", Alert.AlertType.ERROR);
                LOGGER.log(Level.SEVERE, "Error al marcar la notificación como leída.", e);
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, seleccione una notificación para marcar como leída.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Notificacion selected = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.eliminar();
                listaNotificaciones.remove(selected);
                mostrarAlerta("Éxito", "Notificación eliminada.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error", "Error al eliminar la notificación.", Alert.AlertType.ERROR);
                LOGGER.log(Level.SEVERE, "Error al eliminar la notificación.", e);
            }
        } else {
            mostrarAlerta("Advertencia", "Por favor, seleccione una notificación para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaNotificaciones.getScene().getWindow();
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
