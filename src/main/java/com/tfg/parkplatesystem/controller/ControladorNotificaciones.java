package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Notificacion;
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
        columnaFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));

        listaNotificaciones = FXCollections.observableArrayList();
        tablaNotificaciones.setItems(listaNotificaciones);

        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaNotificaciones.clear();
        List<Notificacion> notificaciones = Notificacion.obtenerPorUsuario(usuario.getIdUsuario());
        listaNotificaciones.addAll(notificaciones);
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
                        notificacion.getFechaHora().toLowerCase().contains(lowerCaseFilter);
            });
        });

        filtroFechaInicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(notificacion -> {
                if (newValue == null) {
                    return true;
                }
                return notificacion.getFechaHora().compareTo(newValue.toString()) >= 0;
            });
        });

        filtroFechaFin.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(notificacion -> {
                if (newValue == null) {
                    return true;
                }
                return notificacion.getFechaHora().compareTo(newValue.toString()) <= 0;
            });
        });

        SortedList<Notificacion> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaNotificaciones.comparatorProperty());

        tablaNotificaciones.setItems(sortedData);
    }

    @FXML
    private void buscar(ActionEvent event) {
        configurarBusqueda();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        filtroFechaInicio.setValue(null);
        filtroFechaFin.setValue(null);
        configurarBusqueda();
    }

    @FXML
    private void marcarComoLeida(ActionEvent event) {
        Notificacion selected = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.setLeida(true);
            selected.actualizar();
            tablaNotificaciones.refresh();
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Notificacion selected = tablaNotificaciones.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selected.eliminar();
            listaNotificaciones.remove(selected);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaNotificaciones.getScene().getWindow();
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
