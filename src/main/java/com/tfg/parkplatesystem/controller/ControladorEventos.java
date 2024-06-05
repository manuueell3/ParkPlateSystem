package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Evento;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorEventos {

    @FXML
    private TableView<Evento> tablaEventos;
    @FXML
    private TableColumn<Evento, Long> columnaId;
    @FXML
    private TableColumn<Evento, String> columnaTipo;
    @FXML
    private TableColumn<Evento, String> columnaDescripcion;
    @FXML
    private TableColumn<Evento, LocalDateTime> columnaFechaHora;
    @FXML
    private TextField campoTipo;
    @FXML
    private TextArea campoDescripcion;
    @FXML
    private DatePicker campoFecha;
    @FXML
    private Button botonAgregar;
    @FXML
    private Button botonActualizar;
    @FXML
    private Button botonEliminar;

    private ObservableList<Evento> listaEventos;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorEventos.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarEventosDesdeBD();
    }

    @FXML
    public void initialize() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idEvento"));
        columnaTipo.setCellValueFactory(new PropertyValueFactory<>("tipoEvento"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));

        listaEventos = FXCollections.observableArrayList();
        tablaEventos.setItems(listaEventos);
    }

    private void cargarEventosDesdeBD() {
        listaEventos.clear();
        try {
            listaEventos.addAll(Evento.obtenerTodos(usuario.getIdUsuario()));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al cargar eventos desde la base de datos", e);
            mostrarAlerta("Error", "No se pudieron cargar los eventos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void agregarEvento() {
        if (validarCampos()) {
            String tipoEvento = campoTipo.getText();
            String descripcion = campoDescripcion.getText();
            LocalDateTime fechaHora = campoFecha.getValue().atStartOfDay();

            Evento nuevoEvento = new Evento(null, usuario.getIdUsuario(), tipoEvento, descripcion, fechaHora);
            try {
                nuevoEvento.guardar();
                cargarEventosDesdeBD();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al guardar el evento", e);
                mostrarAlerta("Error", "No se pudo guardar el evento.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void actualizarEvento() {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null && validarCampos()) {
            String tipoEvento = campoTipo.getText();
            String descripcion = campoDescripcion.getText();
            LocalDateTime fechaHora = campoFecha.getValue().atStartOfDay();

            eventoSeleccionado.setTipoEvento(tipoEvento);
            eventoSeleccionado.setDescripcion(descripcion);
            eventoSeleccionado.setFechaHora(fechaHora);
            try {
                eventoSeleccionado.actualizar();
                cargarEventosDesdeBD();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al actualizar el evento", e);
                mostrarAlerta("Error", "No se pudo actualizar el evento.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un evento para actualizar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void eliminarEvento() {
        Evento eventoSeleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (eventoSeleccionado != null) {
            try {
                eventoSeleccionado.eliminar();
                cargarEventosDesdeBD();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al eliminar el evento", e);
                mostrarAlerta("Error", "No se pudo eliminar el evento.", Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Advertencia", "Seleccione un evento para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaEventos.getScene().getWindow();
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

    private boolean validarCampos() {
        String tipoEvento = campoTipo.getText();
        String descripcion = campoDescripcion.getText();
        LocalDate fecha = campoFecha.getValue();

        if (tipoEvento == null || tipoEvento.trim().isEmpty()) {
            mostrarAlerta("Validación", "El tipo de evento es obligatorio.", Alert.AlertType.WARNING);
            return false;
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            mostrarAlerta("Validación", "La descripción es obligatoria.", Alert.AlertType.WARNING);
            return false;
        }
        if (fecha == null) {
            mostrarAlerta("Validación", "La fecha es obligatoria.", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
