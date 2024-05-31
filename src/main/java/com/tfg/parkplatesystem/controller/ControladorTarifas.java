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
import com.tfg.parkplatesystem.model.Tarifa;

public class ControladorTarifas {

    @FXML
    private TableView<Tarifa> tarifasTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private TableColumn<Tarifa, Long> idColumn;

    @FXML
    private TableColumn<Tarifa, String> descripcionColumn;

    @FXML
    private TableColumn<Tarifa, Double> precioColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField precioTextField;

    @FXML
    public void handleAddTarifa(ActionEvent event) {
        // Lógica para añadir una nueva tarifa
    }

    @FXML
    public void handleDeleteTarifa(ActionEvent event) {
        // Lógica para eliminar una tarifa seleccionada
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tarifasTable.getScene().getWindow();
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
            e.printStackTrace();
        }
    }
}
