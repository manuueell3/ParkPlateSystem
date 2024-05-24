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
import com.tfg.parkplatesystem.model.Rol;

import java.util.Objects;

public class ControladorRoles {

    @FXML
    private TableView<Rol> rolesTable;

    @FXML
    private TableColumn<Rol, Long> idColumn;

    @FXML
    private TableColumn<Rol, String> nombreColumn;

    @FXML
    private TableColumn<Rol, String> descripcionColumn;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField descripcionTextField;

    @FXML
    public void handleAddRol(ActionEvent event) {
        // Lógica para añadir un nuevo rol
    }

    @FXML
    public void handleDeleteRol(ActionEvent event) {
        // Lógica para eliminar un rol seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) rolesTable.getScene().getWindow();
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/tfg/parkplatesystem/fxml/principal.fxml")));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
