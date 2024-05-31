package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControladorRegistroUsuario {

    @FXML
    private TextField nombreCampoTexto;

    @FXML
    private TextField apellidosCampoTexto;

    @FXML
    private TextField emailCampoTexto;

    @FXML
    private PasswordField contraseñaCampo;

    @FXML
    private PasswordField confirmarContraseñaCampo;

    @FXML
    private Button registrarBoton;

    @FXML
    private Button volverBoton;

    @FXML
    private Label mensajeConfirmarContraseña;

    @FXML
    private Label mensajeRegistroUsuario;

    @FXML
    public void accionRegistrarBoton() {
        String nombre = nombreCampoTexto.getText();
        String apellidos = apellidosCampoTexto.getText();
        String email = emailCampoTexto.getText();
        String contraseña = contraseñaCampo.getText();
        String confirmarContraseña = confirmarContraseñaCampo.getText();

        if (!validarCampos(nombre, apellidos, email, contraseña, confirmarContraseña)) {
            return;
        }

        // Registrar usuario en la base de datos
        try {
            String fechaAlta = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            Usuario usuario = new Usuario(null, nombre, apellidos, email, contraseña, "conductor", fechaAlta);
            usuario.guardar();
            mostrarAlerta("Registro exitoso", "Usuario registrado con éxito.", Alert.AlertType.INFORMATION);
            limpiarCampos();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error en la base de datos", "No se pudo registrar el usuario.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos(String nombre, String apellidos, String email, String contraseña, String confirmarContraseña) {
        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || contraseña.isEmpty() || confirmarContraseña.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }

        if (!nombre.matches("[a-zA-Z\\s]+")) {
            mostrarAlerta("Error de entrada", "El nombre solo debe contener letras y espacios.", Alert.AlertType.ERROR);
            return false;
        }

        if (!apellidos.matches("[a-zA-Z\\s]+")) {
            mostrarAlerta("Error de entrada", "Los apellidos solo deben contener letras y espacios.", Alert.AlertType.ERROR);
            return false;
        }

        if (!validarEmail(email)) {
            mostrarAlerta("Error de entrada", "Por favor, ingrese un correo electrónico válido.", Alert.AlertType.ERROR);
            return false;
        }

        if (!contraseña.equals(confirmarContraseña)) {
            mostrarAlerta("Error de coincidencia de contraseña", "Las contraseñas no coinciden.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean validarEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    public void accionVolverBoton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/inicioSesion.fxml"));
            Stage stage = (Stage) volverBoton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo cargar la ventana de inicio de sesión.", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombreCampoTexto.clear();
        apellidosCampoTexto.clear();
        emailCampoTexto.clear();
        contraseñaCampo.clear();
        confirmarContraseñaCampo.clear();
    }
}
