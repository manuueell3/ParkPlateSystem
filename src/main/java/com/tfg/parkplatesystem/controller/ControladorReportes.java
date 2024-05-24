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
import com.tfg.parkplatesystem.model.Reporte;

public class ControladorReportes {

    @FXML
    private TableView<Reporte> reportesTable;

    @FXML
    private TableColumn<Reporte, Long> idColumn;

    @FXML
    private TableColumn<Reporte, String> descripcionColumn;

    @FXML
    private TableColumn<Reporte, String> fechaColumn;

    @FXML
    private TableColumn<Reporte, String> tipoColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    private TextField tipoTextField;

    @FXML
    public void handleAddReporte(ActionEvent event) {
        // Lógica para añadir un nuevo reporte
    }

    @FXML
    public void handleDeleteReporte(ActionEvent event) {
        // Lógica para eliminar un reporte seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) reportesTable.getScene().getWindow();
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
