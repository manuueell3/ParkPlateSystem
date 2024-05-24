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
import com.tfg.parkplatesystem.model.Registro;

public class ControladorRegistro {

    @FXML
    private TableView<Registro> registroTable;

    @FXML
    private TableColumn<Registro, Long> idColumn;

    @FXML
    private TableColumn<Registro, String> usuarioColumn;

    @FXML
    private TableColumn<Registro, String> tipoColumn;

    @FXML
    private TableColumn<Registro, String> fechaColumn;

    @FXML
    private TableColumn<Registro, String> estadoColumn;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField tipoTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    private TextField estadoTextField;

    @FXML
    public void handleAddRegistro(ActionEvent event) {
        // Lógica para añadir un nuevo registro
    }

    @FXML
    public void handleDeleteRegistro(ActionEvent event) {
        // Lógica para eliminar un registro seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) registroTable.getScene().getWindow();
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