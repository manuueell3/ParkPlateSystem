package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Objects;

public class ControladorAutenticacion {

    public Button iniciarSesionBoton;
    public Button cancelarBoton;
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
            return;
        }

        try {
            Usuario usuario = Usuario.verificarCredenciales(email, password);
            if (usuario != null) {
                if (usuario.esAdministrador()) {
                    cambiarAPantallaAdministrador(usuario);
                } else {
                    cambiarAPantallaPrincipal(usuario);
                }
            } else {
                errorLabel.setText("Correo electrónico o contraseña incorrectos.");
            }
        } catch (Exception e) {
            errorLabel.setText("Ocurrió un error al intentar iniciar sesión. Por favor, intente de nuevo.");
            e.printStackTrace();
        }
    }

    private void cambiarAPantallaAdministrador(Usuario usuario) throws Exception {
        Stage stage = (Stage) emailTextField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/administrador.fxml"));
        Parent root = loader.load();

        // Pasa el usuario al controlador de la pantalla de administrador
        ControladorAdministrador controlador = loader.getController();
        controlador.setUsuario(usuario);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Park Plate System - Administrador");
        stage.show();
    }

    private void cambiarAPantallaPrincipal(Usuario usuario) throws Exception {
        Stage stage = (Stage) emailTextField.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principal.fxml"));
        Parent root = loader.load();

        // Pasa el usuario al controlador de la pantalla principal
        ControladorPrincipal controlador = loader.getController();
        controlador.setUsuario(usuario);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Park Plate System - Principal");
        stage.show();
    }

    public void accionCancelarBoton(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelarBoton.getScene().getWindow();
        stage.close();
    }
}
