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
import com.tfg.parkplatesystem.model.Sancion;

import java.util.Objects;

public class ControladorSanciones {

    @FXML
    private TableView<Sancion> sancionesTable;

    @FXML
    private TableColumn<Sancion, Long> idColumn;

    @FXML
    private TableColumn<Sancion, String> usuarioColumn;

    @FXML
    private TableColumn<Sancion, String> descripcionColumn;

    @FXML
    private TableColumn<Sancion, Double> montoColumn;

    @FXML
    private TableColumn<Sancion, String> fechaColumn;

    @FXML
    private TextField usuarioTextField;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField montoTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    public void handleAddSancion(ActionEvent event) {
        // Lógica para añadir una nueva sanción
    }

    @FXML
    public void handleDeleteSancion(ActionEvent event) {
        // Lógica para eliminar una sanción seleccionada
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) sancionesTable.getScene().getWindow();
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
