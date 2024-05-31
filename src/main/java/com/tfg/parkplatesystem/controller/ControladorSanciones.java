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
import com.tfg.parkplatesystem.model.Sancion;

public class ControladorSanciones {

    @FXML
    private TableView<Sancion> sancionesTable;

    @FXML
    private Usuario usuario;

    @FXML
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

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
