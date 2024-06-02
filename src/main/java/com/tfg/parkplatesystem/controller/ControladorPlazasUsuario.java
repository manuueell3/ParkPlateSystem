package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorPlazasUsuario {
    @FXML
    private TableView plazasTable;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn numeroPlazaColumn;
    @FXML
    private TableColumn estadoColumn;
    @FXML
    private TableColumn fechaBloqueoColumn;
    @FXML
    private TableColumn fechaAltaColumn;
    @FXML
    private TextField numeroPlazaTextField;
    @FXML
    private ComboBox estadoComboBox;
    @FXML
    private TextField fechaBloqueoTextField;
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox filtroEstadoComboBox;
    @FXML
    private Button handleAddPlaza;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        // Cargar plazas del usuario
    }

    @FXML
    public void initialize() {
        // Inicialización específica para el usuario
    }

    @FXML
    private void handleAddPlaza() {
        // Lógica para añadir una plaza
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) plazasTable.getScene().getWindow();
            FXMLLoader loader;
            if (usuario.esAdministrador()) {
                loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalAdministrador.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalUsuario.fxml"));
            }
            Parent root = loader.load();

            // Pasar el usuario al controlador de la nueva escena
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
