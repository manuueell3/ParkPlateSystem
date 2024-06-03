package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Incidencia;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorIncidencias {

    @FXML
    private TableView<Incidencia> tablaIncidencias;
    @FXML
    private TableColumn<Incidencia, Long> columnaIdIncidencia;
    @FXML
    private TableColumn<Incidencia, String> columnaDescripcion;
    @FXML
    private TableColumn<Incidencia, LocalDateTime> columnaFechaHora;
    @FXML
    private TableColumn<Incidencia, String> columnaEstado;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextField txtEstado;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnLimpiarFiltros;

    private ObservableList<Incidencia> listaIncidencias;
    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorIncidencias.class.getName());

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        cargarDatosUsuario();
    }

    @FXML
    public void initialize() {
        columnaIdIncidencia.setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
        columnaEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        listaIncidencias = FXCollections.observableArrayList();
        tablaIncidencias.setItems(listaIncidencias);

        configurarBusqueda();
    }

    private void cargarDatosUsuario() {
        listaIncidencias.clear();
        List<Incidencia> incidencias = Incidencia.obtenerPorUsuario(usuario.getIdUsuario());
        listaIncidencias.addAll(incidencias);
    }

    private void configurarBusqueda() {
        FilteredList<Incidencia> filteredData = new FilteredList<>(listaIncidencias, p -> true);

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(incidencia -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return incidencia.getDescripcion().toLowerCase().contains(lowerCaseFilter) ||
                        incidencia.getEstado().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Incidencia> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaIncidencias.comparatorProperty());

        tablaIncidencias.setItems(sortedData);
    }

    @FXML
    private void buscar(ActionEvent event) {
        configurarBusqueda();
    }

    @FXML
    private void limpiarFiltros(ActionEvent event) {
        txtBuscar.clear();
        configurarBusqueda();
    }

    @FXML
    private void crearIncidencia(ActionEvent event) {
        String descripcion = txtDescripcion.getText();
        String estado = txtEstado.getText();

        if (descripcion.isEmpty() || estado.isEmpty()) {
            mostrarAlerta("Campos vacíos", "Por favor, complete todos los campos.", Alert.AlertType.WARNING);
            return;
        }

        Incidencia nuevaIncidencia = new Incidencia(0L, usuario.getIdUsuario(), descripcion, LocalDateTime.now(), estado);
        nuevaIncidencia.guardar();
        listaIncidencias.add(nuevaIncidencia);
        mostrarAlerta("Éxito", "Incidencia creada exitosamente.", Alert.AlertType.INFORMATION);
        limpiarCampos();
    }

    @FXML
    private void actualizarIncidencia(ActionEvent event) {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selección vacía", "Por favor, seleccione una incidencia para actualizar.", Alert.AlertType.WARNING);
            return;
        }

        seleccionada.setDescripcion(txtDescripcion.getText());
        seleccionada.setEstado(txtEstado.getText());
        seleccionada.actualizar();
        tablaIncidencias.refresh();
        mostrarAlerta("Éxito", "Incidencia actualizada exitosamente.", Alert.AlertType.INFORMATION);
        limpiarCampos();
    }

    @FXML
    private void eliminarIncidencia(ActionEvent event) {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta("Selección vacía", "Por favor, seleccione una incidencia para eliminar.", Alert.AlertType.WARNING);
            return;
        }

        seleccionada.eliminar();
        listaIncidencias.remove(seleccionada);
        mostrarAlerta("Éxito", "Incidencia eliminada exitosamente.", Alert.AlertType.INFORMATION);
        limpiarCampos();
    }

    private void limpiarCampos() {
        txtDescripcion.clear();
        txtEstado.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaIncidencias.getScene().getWindow();
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
}
