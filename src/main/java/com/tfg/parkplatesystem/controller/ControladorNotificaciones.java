package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.tfg.parkplatesystem.model.Notificacion;

public class ControladorNotificaciones {

    @FXML
    private TableView<Notificacion> notificacionesTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private TableColumn<Notificacion, Long> idColumn;

    @FXML
    private TableColumn<Notificacion, String> usuarioColumn;

    @FXML
    private TableColumn<Notificacion, String> mensajeColumn;

    @FXML
    private TableColumn<Notificacion, String> fechaColumn;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField mensajeTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    public void handleAddNotificacion(ActionEvent event) {
        // Lógica para añadir una nueva notificación
    }

    @FXML
    public void handleDeleteNotificacion(ActionEvent event) {
        // Lógica para eliminar una notificación seleccionada
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) notificacionesTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/ventanaPrincipalUsuario.fxml"));
            Parent root = loader.load();

            // Pasa el usuario al controlador de la vista principal
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
