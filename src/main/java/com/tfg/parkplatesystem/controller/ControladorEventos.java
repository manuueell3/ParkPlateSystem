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
import com.tfg.parkplatesystem.model.Evento;

import java.util.Objects;

public class ControladorEventos {

    @FXML
    private TableView<Evento> eventosTable;

    @FXML
    private TableColumn<Evento, Long> idColumn;

    @FXML
    private TableColumn<Evento, String> nombreColumn;

    @FXML
    private TableColumn<Evento, String> descripcionColumn;

    @FXML
    private TableColumn<Evento, String> fechaColumn;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    public void handleAddEvento(ActionEvent event) {
        // Lógica para añadir un nuevo evento
    }

    @FXML
    public void handleDeleteEvento(ActionEvent event) {
        // Lógica para eliminar un evento seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) eventosTable.getScene().getWindow();
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