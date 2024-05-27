package com.tfg.parkplatesystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.tfg.parkplatesystem.model.PlazaAparcamiento;
import com.tfg.parkplatesystem.model.Usuario;

import java.io.IOException;
import java.util.Objects;

public class ControladorPlazasAparcamiento {

    @FXML
    private TableView<PlazaAparcamiento> plazasTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private TableColumn<PlazaAparcamiento, Long> idColumn;

    @FXML
    private TableColumn<PlazaAparcamiento, String> ubicacionColumn;

    @FXML
    private TableColumn<PlazaAparcamiento, String> estadoColumn;

    @FXML
    private TextField ubicacionTextField;

    @FXML
    public void handleAddPlaza(ActionEvent event) {
        // Lógica para añadir una nueva plaza de aparcamiento
    }

    @FXML
    public void handleDeletePlaza(ActionEvent event) {
        // Lógica para eliminar una plaza de aparcamiento seleccionada
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) plazasTable.getScene().getWindow();
            FXMLLoader loader;
            if (usuario.esAdministrador()) {
                loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/ventanaPrincipalAdministrador.fxml"));
            } else {
                loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/ventanaPrincipalUsuario.fxml"));
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
