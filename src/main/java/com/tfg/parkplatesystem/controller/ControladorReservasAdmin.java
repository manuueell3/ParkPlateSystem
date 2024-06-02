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
import com.tfg.parkplatesystem.model.Reserva;

import java.io.IOException;

public class ControladorReservasAdmin {

    @FXML
    private TableView<Reserva> reservasTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private TableColumn<Reserva, Long> idColumn;

    @FXML
    private TableColumn<Reserva, String> usuarioColumn;

    @FXML
    private TableColumn<Reserva, String> plazaColumn;

    @FXML
    private TableColumn<Reserva, String> fechaColumn;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField plazaTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    public void handleAddReserva(ActionEvent event) {
        // Lógica para añadir una nueva reserva
    }

    @FXML
    public void handleDeleteReserva(ActionEvent event) {
        // Lógica para eliminar una reserva seleccionada
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
