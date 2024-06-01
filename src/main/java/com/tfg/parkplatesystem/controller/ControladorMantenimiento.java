package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Mantenimiento;
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
import java.util.List;

public class ControladorMantenimiento {

    @FXML
    private TableView<Mantenimiento> mantenimientoTable;

    @FXML
    private TableColumn<Mantenimiento, Long> idColumn;

    @FXML
    private TableColumn<Mantenimiento, String> descripcionColumn;

    @FXML
    private TableColumn<Mantenimiento, String> fechaColumn;

    @FXML
    private TableColumn<Mantenimiento, String> estadoColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    private TextField estadoTextField;

    @FXML
    private TextField txtBuscar;

    private ObservableList<Mantenimiento> mantenimientoObservableList;
    private FilteredList<Mantenimiento> filteredData;
    private SortedList<Mantenimiento> sortedData;
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idMantenimiento"));
        descripcionColumn.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        fechaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaInicio")); // Suponiendo que fechaInicio es la fecha principal
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarMantenimientos();

        mantenimientoTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesMantenimiento(newValue));

        configurarBusqueda();
    }

    private void cargarMantenimientos() {
        try {
            List<Mantenimiento> mantenimientos = Mantenimiento.obtenerTodos();
            mantenimientoObservableList = FXCollections.observableArrayList(mantenimientos);
            filteredData = new FilteredList<>(mantenimientoObservableList, p -> true);
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(mantenimientoTable.comparatorProperty());
            mantenimientoTable.setItems(sortedData);
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudieron cargar los mantenimientos: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarDetallesMantenimiento(Mantenimiento mantenimiento) {
        if (mantenimiento != null) {
            descripcionTextField.setText(mantenimiento.getDescripcion());
            fechaTextField.setText(mantenimiento.getFechaInicio()); // Ajustar según los campos correctos
            estadoTextField.setText(mantenimiento.getEstado());
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void handleAddMantenimiento(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        try {
            Long idPlaza = Long.parseLong(descripcionTextField.getText()); // Suponiendo que idPlaza es el primer campo
            String descripcion = descripcionTextField.getText();
            String fecha = fechaTextField.getText();
            String estado = estadoTextField.getText();
            Mantenimiento mantenimiento = new Mantenimiento(idPlaza, descripcion, fecha, fecha, estado); // Ajusta según los campos correctos
            mantenimiento.guardar();
            mostrarAlerta("Registro exitoso", "Mantenimiento registrado con éxito.", Alert.AlertType.INFORMATION);
            cargarMantenimientos();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo registrar el mantenimiento: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El ID de Plaza debe ser un número: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateMantenimiento(ActionEvent event) {
        Mantenimiento mantenimientoSeleccionado = mantenimientoTable.getSelectionModel().getSelectedItem();
        if (mantenimientoSeleccionado == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione un mantenimiento para modificar.", Alert.AlertType.ERROR);
            return;
        }
        if (!validarCampos()) {
            return;
        }
        try {
            String descripcion = descripcionTextField.getText();
            String fecha = fechaTextField.getText();
            String estado = estadoTextField.getText();

            mantenimientoSeleccionado.setDescripcion(descripcion);
            mantenimientoSeleccionado.setFechaInicio(fecha); // Ajustar según los campos correctos
            mantenimientoSeleccionado.setEstado(estado);
            mantenimientoSeleccionado.actualizar();

            mostrarAlerta("Actualización exitosa", "Mantenimiento actualizado con éxito.", Alert.AlertType.INFORMATION);
            cargarMantenimientos();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo actualizar el mantenimiento: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteMantenimiento(ActionEvent event) {
        Mantenimiento mantenimientoSeleccionado = mantenimientoTable.getSelectionModel().getSelectedItem();
        if (mantenimientoSeleccionado == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione un mantenimiento para eliminar.", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar el mantenimiento seleccionado?");

            if (alert.showAndWait().orElseThrow() == ButtonType.OK) {
                mantenimientoSeleccionado.eliminar();
                mostrarAlerta("Eliminación exitosa", "Mantenimiento eliminado con éxito.", Alert.AlertType.INFORMATION);
                cargarMantenimientos();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo eliminar el mantenimiento: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) mantenimientoTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalAdministrador.fxml"));
            Parent root = loader.load();

            // Asumiendo que el controlador principal necesita un usuario
            ControladorPrincipal controladorPrincipal = loader.getController();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (IOException e) {
            mostrarAlerta("Error de navegación", "No se pudo volver al menú principal: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        String descripcion = descripcionTextField.getText();
        String fecha = fechaTextField.getText();
        String estado = estadoTextField.getText();

        if (descripcion.isEmpty() || fecha.isEmpty() || estado.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        descripcionTextField.clear();
        fechaTextField.clear();
        estadoTextField.clear();
    }

    private void configurarBusqueda() {
        if (filteredData == null) {
            filteredData = new FilteredList<>(mantenimientoObservableList, p -> true);
        }

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(mantenimiento -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (mantenimiento.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (mantenimiento.getFechaInicio().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (mantenimiento.getEstado().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(mantenimientoTable.comparatorProperty());

        mantenimientoTable.setItems(sortedData);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
