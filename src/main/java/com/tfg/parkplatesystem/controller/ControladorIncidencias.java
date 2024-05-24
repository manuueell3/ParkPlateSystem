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
import com.tfg.parkplatesystem.model.Incidencia;

import java.util.Objects;

public class ControladorIncidencias {

    @FXML
    private TableView<Incidencia> incidenciasTable;

    @FXML
    private TableColumn<Incidencia, Long> idColumn;

    @FXML
    private TableColumn<Incidencia, String> descripcionColumn;

    @FXML
    private TableColumn<Incidencia, String> estadoColumn;

    @FXML
    private TableColumn<Incidencia, String> fechaColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField estadoTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    public void handleAddIncidencia(ActionEvent event) {
        // Lógica para añadir una nueva incidencia
    }

    @FXML
    public void handleDeleteIncidencia(ActionEvent event) {
        // Lógica para eliminar una incidencia seleccionada
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) incidenciasTable.getScene().getWindow();
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