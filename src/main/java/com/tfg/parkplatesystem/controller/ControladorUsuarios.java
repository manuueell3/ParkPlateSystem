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
import com.tfg.parkplatesystem.model.Usuario;

public class ControladorUsuarios {

    @FXML
    private TableView<Usuario> usuariosTable;

    @FXML
    private TableColumn<Usuario, Long> idColumn;

    @FXML
    private TableColumn<Usuario, String> nombreColumn;

    @FXML
    private TableColumn<Usuario, String> correoColumn;

    @FXML
    private TableColumn<Usuario, String> rolColumn;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField correoTextField;

    @FXML
    private TextField rolTextField;

    @FXML
    public void handleAddUsuario(ActionEvent event) {
        // Lógica para añadir un nuevo usuario
    }

    @FXML
    public void handleDeleteUsuario(ActionEvent event) {
        // Lógica para eliminar un usuario seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) usuariosTable.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/tfg/parkplatesystem/fxml/principal.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
