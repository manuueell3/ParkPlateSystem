package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
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

public class ControladorIncidencias {

    @FXML
    private TableView<Incidencia> incidenciasTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalUsuario.fxml"));
            Parent root = loader.load();

            // Pasa el usuario al controlador de la vista principal
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
