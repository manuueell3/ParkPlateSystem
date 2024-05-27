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
import com.tfg.parkplatesystem.model.RegistroEntradaSalida;

public class ControladorEntradasSalidas {

    @FXML
    private TableView<RegistroEntradaSalida> entradasSalidasTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    private TableColumn<RegistroEntradaSalida, Long> idColumn;

    @FXML
    private TableColumn<RegistroEntradaSalida, String> vehiculoColumn;

    @FXML
    private TableColumn<RegistroEntradaSalida, String> fechaEntradaColumn;

    @FXML
    private TableColumn<RegistroEntradaSalida, String> fechaSalidaColumn;

    @FXML
    private TableColumn<RegistroEntradaSalida, String> estadoColumn;

    @FXML
    private TextField vehiculoTextField;

    @FXML
    private TextField fechaEntradaTextField;

    @FXML
    private TextField fechaSalidaTextField;

    @FXML
    private TextField estadoTextField;

    @FXML
    public void handleAddRegistro(ActionEvent event) {
        // Lógica para añadir un nuevo registro de entrada/salida
    }

    @FXML
    public void handleDeleteRegistro(ActionEvent event) {
        // Lógica para eliminar un registro de entrada/salida seleccionado
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) entradasSalidasTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/ventanaPrincipalUsuario.fxml"));
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
