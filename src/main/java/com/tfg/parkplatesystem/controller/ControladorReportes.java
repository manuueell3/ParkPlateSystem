package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Reporte;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ControladorReportes {

    @FXML
    private TableView<Reporte> reportesTable;

    @FXML
    private TableColumn<Reporte, Long> idColumn;

    @FXML
    private TableColumn<Reporte, String> descripcionColumn;

    @FXML
    private TableColumn<Reporte, String> fechaColumn;

    @FXML
    private TableColumn<Reporte, String> tipoColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private ComboBox<String> tipoComboBox;

    private Usuario usuario;
    private ObservableList<Reporte> reportesData;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descripcionColumn.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        fechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        tipoColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        reportesData = FXCollections.observableArrayList(Reporte.obtenerTodos());
        reportesTable.setItems(reportesData);

        // Inicializar ComboBox con opciones
        tipoComboBox.setItems(FXCollections.observableArrayList(
                "Incidencia", "Mantenimiento", "Pago", "Sanción", "Reserva", "Evento"
        ));
    }

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void handleAddReporte(ActionEvent event) {
        String descripcion = descripcionTextField.getText();
        String tipo = tipoComboBox.getValue(); // Obtener el valor seleccionado del ComboBox

        if (descripcion.isEmpty() || tipo == null || tipo.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.");
            return;
        }

        // Obtener la fecha y hora actuales
        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        try {
            Reporte nuevoReporte = new Reporte(null, descripcion, fecha, tipo, usuario.getIdUsuario());
            nuevoReporte.guardar();
            reportesData.add(nuevoReporte);

            descripcionTextField.clear();
            tipoComboBox.setValue(null);
        } catch (Exception e) {
            mostrarAlerta("Error al guardar", "No se pudo guardar el reporte. Inténtelo nuevamente.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleDeleteReporte(ActionEvent event) {
        Reporte selectedReporte = reportesTable.getSelectionModel().getSelectedItem();
        if (selectedReporte != null) {
            try {
                selectedReporte.eliminar();
                reportesData.remove(selectedReporte);
            } catch (Exception e) {
                mostrarAlerta("Error al eliminar", "No se pudo eliminar el reporte. Inténtelo nuevamente.");
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Selección vacía", "Por favor, seleccione un reporte para eliminar.");
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) reportesTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalAdministrador.fxml"));
            Parent root = loader.load();

            // Pasa el usuario al controlador de la vista principal
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (Exception e) {
            mostrarAlerta("Error al cargar", "No se pudo cargar la vista principal. Inténtelo nuevamente.");
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
