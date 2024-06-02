package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Mantenimiento;
import com.tfg.parkplatesystem.model.Plaza;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ControladorMantenimiento {

    @FXML
    private TableView<Mantenimiento> mantenimientoTable;

    @FXML
    private TableColumn<Mantenimiento, Long> idColumn;

    @FXML
    private TableColumn<Mantenimiento, String> descripcionColumn;

    @FXML
    private TableColumn<Mantenimiento, String> fechaInicioColumn;

    @FXML
    private TableColumn<Mantenimiento, String> fechaFinColumn;

    @FXML
    private TableColumn<Mantenimiento, String> estadoColumn;

    @FXML
    private ComboBox<Plaza> plazaComboBox;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private ComboBox<String> estadoComboBox;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> filtroEstadoComboBox;

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
        fechaInicioColumn.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        fechaFinColumn.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));

        cargarMantenimientos();
        cargarPlazas();

        mantenimientoTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesMantenimiento(newValue));

        configurarBusqueda();
        configurarFiltroEstado();
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

    private void cargarPlazas() {
        try {
            List<Plaza> plazas = Plaza.obtenerTodas();
            plazaComboBox.setItems(FXCollections.observableArrayList(plazas));
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudieron cargar las plazas: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarDetallesMantenimiento(Mantenimiento mantenimiento) {
        if (mantenimiento != null) {
            Plaza plazaSeleccionada = null;
            for (Plaza plaza : plazaComboBox.getItems()) {
                if (plaza.getIdPlaza().equals(mantenimiento.getIdPlaza())) {
                    plazaSeleccionada = plaza;
                    break;
                }
            }
            plazaComboBox.setValue(plazaSeleccionada);
            descripcionTextField.setText(mantenimiento.getDescripcion());
            estadoComboBox.setValue(mantenimiento.getEstado());
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
            Plaza plazaSeleccionada = plazaComboBox.getValue();
            if (plazaSeleccionada == null) {
                mostrarAlerta("Error de validación", "Debe seleccionar una Plaza.", Alert.AlertType.ERROR);
                return;
            }
            Long idPlaza = plazaSeleccionada.getIdPlaza();
            String descripcion = descripcionTextField.getText();
            String fechaInicio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String estado = estadoComboBox.getValue();
            Mantenimiento mantenimiento = new Mantenimiento(idPlaza, descripcion, fechaInicio, null, estado); // Ajusta según los campos correctos
            mantenimiento.guardar();
            mostrarAlerta("Registro exitoso", "Mantenimiento registrado con éxito.", Alert.AlertType.INFORMATION);
            cargarMantenimientos();
            limpiarCampos();
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo registrar el mantenimiento: " + e.getMessage(), Alert.AlertType.ERROR);
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
            Plaza plazaSeleccionada = plazaComboBox.getValue();
            if (plazaSeleccionada == null) {
                mostrarAlerta("Error de validación", "Debe seleccionar una Plaza.", Alert.AlertType.ERROR);
                return;
            }
            Long idPlaza = plazaSeleccionada.getIdPlaza();
            String descripcion = descripcionTextField.getText();
            String estado = estadoComboBox.getValue();

            if (idPlaza.equals(mantenimientoSeleccionado.getIdPlaza()) &&
                    descripcion.equals(mantenimientoSeleccionado.getDescripcion()) &&
                    estado.equals(mantenimientoSeleccionado.getEstado())) {
                mostrarAlerta("Sin cambios", "No se han realizado cambios en el mantenimiento.", Alert.AlertType.INFORMATION);
                return;
            }

            mantenimientoSeleccionado.setIdPlaza(idPlaza);
            mantenimientoSeleccionado.setDescripcion(descripcion);
            mantenimientoSeleccionado.setEstado(estado);
            if (estado.equalsIgnoreCase("completado")) {
                String fechaFin = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                mantenimientoSeleccionado.setFechaFin(fechaFin);
            } else {
                mantenimientoSeleccionado.setFechaFin(null);
            }
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

            // Pasa el usuario al controlador principal
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

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
        String estado = estadoComboBox.getValue();

        if (descripcion.isEmpty() || estado == null || estado.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }

        // Validar estado
        if (!estado.equalsIgnoreCase("pendiente") && !estado.equalsIgnoreCase("completado")) {
            mostrarAlerta("Error de estado", "El estado debe ser 'pendiente' o 'completado'.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        plazaComboBox.setValue(null);
        descripcionTextField.clear();
        estadoComboBox.setValue(null);
    }

    private void configurarBusqueda() {
        if (filteredData == null) {
            filteredData = new FilteredList<>(mantenimientoObservableList, p -> true);
        }

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> actualizarFiltro());
        filtroEstadoComboBox.valueProperty().addListener((observable, oldValue, newValue) -> actualizarFiltro());

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(mantenimientoTable.comparatorProperty());

        mantenimientoTable.setItems(sortedData);
    }

    private void actualizarFiltro() {
        filteredData.setPredicate(mantenimiento -> {
            String estadoFiltro = filtroEstadoComboBox.getValue();
            String textoFiltro = txtBuscar.getText().toLowerCase();

            if (estadoFiltro == null || estadoFiltro.equals("Todos")) {
                estadoFiltro = "";
            }

            boolean coincideEstado = estadoFiltro.isEmpty() || mantenimiento.getEstado().equalsIgnoreCase(estadoFiltro);
            boolean coincideTexto = textoFiltro.isEmpty() ||
                    mantenimiento.getDescripcion().toLowerCase().contains(textoFiltro) ||
                    mantenimiento.getFechaInicio().toLowerCase().contains(textoFiltro) ||
                    mantenimiento.getFechaFin() != null && mantenimiento.getFechaFin().toLowerCase().contains(textoFiltro);

            return coincideEstado && coincideTexto;
        });
    }

    private void configurarFiltroEstado() {
        filtroEstadoComboBox.setItems(FXCollections.observableArrayList("Todos", "pendiente", "completado"));
        filtroEstadoComboBox.setValue("Todos");
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
