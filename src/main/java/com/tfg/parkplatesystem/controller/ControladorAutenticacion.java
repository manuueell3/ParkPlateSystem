package com.tfg.parkplatesystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class ControladorAutenticacion {

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    public void handleLoginButton(ActionEvent event) {
        String email = emailTextField.getText();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Por favor, ingrese el correo electrónico y la contraseña.");
        } else {
            // Lógica de autenticación
            if (email.equals("admin@example.com") && password.equals("admin")) {
                try {
                    Stage stage = (Stage) emailTextField.getScene().getWindow();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/tfg/parkplatesystem/fxml/principal.fxml")));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);
                    stage.setTitle("Park Plate System - Principal");
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                errorLabel.setText("Correo electrónico o contraseña incorrectos.");
            }
        }
    }
}
