package com.tfg.parkplatesystem.controller;

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

public class ControladorPlazas {

    @FXML
    private TableView<Plaza> plazasTable;

    @FXML
    private TableColumn<Plaza, Long> idColumn;

    @FXML
    private TableColumn<Plaza, Integer> numeroPlazaColumn;

    @FXML
    private TableColumn<Plaza, String> estadoColumn;

    @FXML
    private TableColumn<Plaza, String> fechaBloqueoColumn;

    @FXML
    private TableColumn<Plaza, String> fechaAltaColumn;

    @FXML
    private TextField numeroPlazaTextField;

    @FXML
    private ComboBox<String> estadoComboBox;

    @FXML
    private TextField fechaBloqueoTextField;

    @FXML
    private TextField txtBuscar;

    @FXML
    private ComboBox<String> filtroEstadoComboBox;

    private ObservableList<Plaza> listaPlazas;
    private FilteredList<Plaza> filteredData;
    private SortedList<Plaza> sortedData;
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idPlaza"));
        numeroPlazaColumn.setCellValueFactory(new PropertyValueFactory<>("numeroPlaza"));
        estadoColumn.setCellValueFactory(new PropertyValueFactory<>("estado"));
        fechaBloqueoColumn.setCellValueFactory(new PropertyValueFactory<>("fechaBloqueo"));
        fechaAltaColumn.setCellValueFactory(new PropertyValueFactory<>("fechaAlta"));

        cargarPlazas();

        plazasTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesPlaza(newValue));

        configurarBusqueda();
        configurarFiltroEstado();

        estadoComboBox.setItems(FXCollections.observableArrayList("disponible", "ocupada", "reservada"));
    }

    private void cargarPlazas() {
        try {
            List<Plaza> plazas = Plaza.obtenerTodas();
            listaPlazas = FXCollections.observableArrayList(plazas);
            filteredData = new FilteredList<>(listaPlazas, p -> true);
            sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(plazasTable.comparatorProperty());
            plazasTable.setItems(sortedData);
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudieron cargar las plazas: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void mostrarDetallesPlaza(Plaza plaza) {
        if (plaza != null) {
            numeroPlazaTextField.setText(String.valueOf(plaza.getNumeroPlaza()));
            estadoComboBox.setValue(plaza.getEstado());
            fechaBloqueoTextField.setText(plaza.getFechaBloqueo());
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void handleAddPlaza(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        try {
            int numeroPlaza = Integer.parseInt(numeroPlazaTextField.getText());
            String estado = estadoComboBox.getValue();
            LocalDateTime fechaAlta = LocalDateTime.now();

            if (existePlaza(numeroPlaza)) {
                mostrarAlerta("Error de validación", "Ya existe una plaza con ese número.", Alert.AlertType.ERROR);
                return;
            }

            Plaza nuevaPlaza = new Plaza(null, numeroPlaza, estado, null, fechaAlta.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            nuevaPlaza.guardar();
            mostrarAlerta("Registro exitoso", "Plaza registrada con éxito.", Alert.AlertType.INFORMATION);
            cargarPlazas();
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El número de plaza debe ser un valor numérico.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo registrar la plaza: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdatePlaza(ActionEvent event) {
        Plaza plazaSeleccionada = plazasTable.getSelectionModel().getSelectedItem();
        if (plazaSeleccionada == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione una plaza para modificar.", Alert.AlertType.ERROR);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {
            int numeroPlaza = Integer.parseInt(numeroPlazaTextField.getText());
            String estado = estadoComboBox.getValue();
            String fechaBloqueo = fechaBloqueoTextField.getText();

            if (numeroPlaza != plazaSeleccionada.getNumeroPlaza() && existePlaza(numeroPlaza)) {
                mostrarAlerta("Error de validación", "Ya existe una plaza con ese número.", Alert.AlertType.ERROR);
                return;
            }

            // Verificar si no se han realizado cambios
            if (numeroPlaza == plazaSeleccionada.getNumeroPlaza() &&
                    estado.equals(plazaSeleccionada.getEstado()) &&
                    ((fechaBloqueo == null && plazaSeleccionada.getFechaBloqueo() == null) ||
                            (fechaBloqueo != null && fechaBloqueo.equals(plazaSeleccionada.getFechaBloqueo())))) {
                mostrarAlerta("Sin cambios", "No se han realizado cambios en la plaza.", Alert.AlertType.INFORMATION);
                return;
            }

            plazaSeleccionada.setNumeroPlaza(numeroPlaza);
            plazaSeleccionada.setEstado(estado);
            if ("ocupada".equals(estado)) {
                plazaSeleccionada.setFechaBloqueo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            } else {
                plazaSeleccionada.setFechaBloqueo(fechaBloqueo.isEmpty() ? null : fechaBloqueo);
            }
            plazaSeleccionada.actualizar();
            mostrarAlerta("Actualización exitosa", "Plaza actualizada con éxito.", Alert.AlertType.INFORMATION);
            cargarPlazas();
            limpiarCampos();
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El número de plaza debe ser un valor numérico.", Alert.AlertType.ERROR);
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo actualizar la plaza: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePlaza(ActionEvent event) {
        Plaza plazaSeleccionada = plazasTable.getSelectionModel().getSelectedItem();
        if (plazaSeleccionada == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione una plaza para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar la plaza seleccionada?");

            if (alert.showAndWait().orElseThrow() == ButtonType.OK) {
                plazaSeleccionada.eliminar();
                mostrarAlerta("Eliminación exitosa", "Plaza eliminada con éxito.", Alert.AlertType.INFORMATION);
                cargarPlazas();
                limpiarCampos();
            }
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo eliminar la plaza: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) plazasTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalAdministrador.fxml"));
            Parent root = loader.load();

            // Pasar el usuario al controlador de la nueva escena
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
        String numeroPlaza = numeroPlazaTextField.getText();
        String estado = estadoComboBox.getValue();
        String fechaBloqueo = fechaBloqueoTextField.getText();

        if (numeroPlaza.isEmpty() || estado == null || estado.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean existePlaza(int numeroPlaza) throws SQLException {
        for (Plaza plaza : listaPlazas) {
            if (plaza.getNumeroPlaza() == numeroPlaza) {
                return true;
            }
        }
        return false;
    }

    private void limpiarCampos() {
        numeroPlazaTextField.clear();
        estadoComboBox.setValue(null);
        fechaBloqueoTextField.clear();
    }

    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> actualizarFiltro());
        filtroEstadoComboBox.valueProperty().addListener((observable, oldValue, newValue) -> actualizarFiltro());

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(plazasTable.comparatorProperty());

        plazasTable.setItems(sortedData);
    }

    private void actualizarFiltro() {
        filteredData.setPredicate(plaza -> {
            String estadoFiltro = filtroEstadoComboBox.getValue();
            String textoFiltro = txtBuscar.getText().toLowerCase();

            if (estadoFiltro == null || estadoFiltro.equals("Todos")) {
                estadoFiltro = "";
            }

            boolean coincideEstado = estadoFiltro.isEmpty() || plaza.getEstado().equalsIgnoreCase(estadoFiltro);
            boolean coincideTexto = textoFiltro.isEmpty() ||
                    plaza.getNumeroPlaza().toString().toLowerCase().contains(textoFiltro) ||
                    plaza.getEstado().toLowerCase().contains(textoFiltro);

            return coincideEstado && coincideTexto;
        });
    }

    private void configurarFiltroEstado() {
        filtroEstadoComboBox.setItems(FXCollections.observableArrayList("Todos", "disponible", "ocupada", "reservada"));
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
