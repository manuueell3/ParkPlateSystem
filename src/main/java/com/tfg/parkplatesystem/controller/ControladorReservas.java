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
import com.tfg.parkplatesystem.model.Reserva;

import java.util.Objects;

public class ControladorReservas {

    @FXML
    private TableView<Reserva> reservasTable;

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
