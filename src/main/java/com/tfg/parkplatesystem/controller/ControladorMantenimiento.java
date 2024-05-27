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
import com.tfg.parkplatesystem.model.Mantenimiento;

public class ControladorMantenimiento {

    @FXML
    private TableView<Mantenimiento> mantenimientoTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private TableColumn<Mantenimiento, Long> idColumn;

    @FXML
    private TableColumn<Mantenimiento, String> descripcionColumn;

    @FXML
    private TableColumn<Mantenimiento, String> fechaColumn;

    @FXML
    private TableColumn<Mantenimiento, String> estadoColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private TextField fechaTextField;

    @FXML
    private TextField estadoTextField;

    @FXML
    public void handleAddMantenimiento(ActionEvent event) {
        // Lógica para añadir un nuevo mantenimiento
    }

    @FXML
    public void handleDeleteMantenimiento(ActionEvent event) {
        // Lógica para eliminar un mantenimiento seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) mantenimientoTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/ventanaPrincipalAdministrador.fxml"));
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
