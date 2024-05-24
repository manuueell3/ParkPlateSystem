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
import com.tfg.parkplatesystem.model.Tarjeta;

import java.util.Objects;

public class ControladorTarjetas {

    @FXML
    private TableView<Tarjeta> tarjetasTable;

    @FXML
    private TableColumn<Tarjeta, Long> idColumn;

    @FXML
    private TableColumn<Tarjeta, String> usuarioColumn;

    @FXML
    private TableColumn<Tarjeta, String> numeroColumn;

    @FXML
    private TableColumn<Tarjeta, String> fechaExpiracionColumn;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField numeroTextField;

    @FXML
    private TextField fechaExpiracionTextField;

    @FXML
    public void handleAddTarjeta(ActionEvent event) {
        // Lógica para añadir una nueva tarjeta
    }

    @FXML
    public void handleDeleteTarjeta(ActionEvent event) {
        // Lógica para eliminar una tarjeta seleccionada
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tarjetasTable.getScene().getWindow();
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