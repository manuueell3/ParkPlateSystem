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
import com.tfg.parkplatesystem.model.Pago;

import java.util.Objects;

public class ControladorPagos {

    @FXML
    private TableView<Pago> pagosTable;

    @FXML
    private TableColumn<Pago, Long> idColumn;

    @FXML
    private TableColumn<Pago, String> usuarioColumn;

    @FXML
    private TableColumn<Pago, Double> montoColumn;

    @FXML
    private TableColumn<Pago, String> fechaColumn;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField montoTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    public void handleAddPago(ActionEvent event) {
        // Lógica para añadir un nuevo pago
    }

    @FXML
    public void handleDeletePago(ActionEvent event) {
        // Lógica para eliminar un pago seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) pagosTable.getScene().getWindow();
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
