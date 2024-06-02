package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ControladorReservasUsuario {
    @FXML
    private TableView reservasTable;
    @FXML
    private TableColumn idColumn;
    @FXML
    private TableColumn usuarioColumn;
    @FXML
    private TableColumn plazaColumn;
    @FXML
    private TableColumn fechaColumn;
    @FXML
    private TextField plazaTextField;
    @FXML
    private TextField fechaTextField;
    @FXML
    private Button handleAddReserva;
    @FXML
    private Button handleDeleteReserva;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        // Cargar reservas del usuario
    }

    @FXML
    public void initialize() {
        // Inicialización específica para el usuario
    }

    @FXML
    private void handleAddReserva() {
        // Lógica para añadir una reserva
    }

    @FXML
    private void handleDeleteReserva() {
        // Lógica para eliminar una reserva
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) reservasTable.getScene().getWindow();
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
}
