package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ControladorUsuarios {

    @FXML
    private TableView<Usuario> usuariosTable;

    @FXML
    private TableColumn<Usuario, Long> idColumn;

    @FXML
    private TableColumn<Usuario, String> nombreColumn;

    @FXML
    private TableColumn<Usuario, String> apellidosColumn;

    @FXML
    private TableColumn<Usuario, String> correoColumn;

    @FXML
    private TableColumn<Usuario, String> rolColumn;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidosTextField;

    @FXML
    private TextField correoTextField;

    @FXML
    private TextField rolTextField;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        apellidosColumn.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        correoColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        rolColumn.setCellValueFactory(new PropertyValueFactory<>("rol"));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        List<Usuario> usuarios = Usuario.obtenerTodos();
        ObservableList<Usuario> usuariosObservableList = FXCollections.observableArrayList(usuarios);
        usuariosTable.setItems(usuariosObservableList);
    }

    @FXML
    public void handleAddUsuario(ActionEvent event) {
        String nombre = nombreTextField.getText();
        String apellidos = apellidosTextField.getText();
        String email = correoTextField.getText();
        String rol = rolTextField.getText();

        if (!validarCampos(nombre, apellidos, email, rol)) {
            return;
        }

        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();
        // Formatear la fecha y hora en el formato deseado
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaHoraActual = ahora.format(formato);

        Usuario nuevoUsuario = new Usuario(
                null,
                nombre,
                apellidos,
                email,
                "1234", // Establece una contraseña predeterminada
                rol,
                fechaHoraActual // Establece la fecha y hora de alta actual
        );

        try {
            nuevoUsuario.guardar();
            mostrarAlerta("Registro exitoso", "Usuario registrado con éxito.", Alert.AlertType.INFORMATION);
            cargarUsuarios();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // Código de error para duplicados en MySQL
                mostrarAlerta("Error de entrada", "El correo electrónico ya está en uso.", Alert.AlertType.ERROR);
            } else {
                mostrarAlerta("Error en la base de datos", "No se pudo registrar el usuario.", Alert.AlertType.ERROR);
            }
        }
    }

    private boolean validarCampos(String nombre, String apellidos, String email, String rol) {
        if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || rol.isEmpty()) {
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

        if (!rol.matches("administrador|conductor")) {
            mostrarAlerta("Error de entrada", "El rol debe ser 'administrador' o 'conductor'.", Alert.AlertType.ERROR);
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
    public void handleDeleteUsuario(ActionEvent event) {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar al usuario seleccionado?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    usuarioSeleccionado.eliminar();
                    mostrarAlerta("Eliminación exitosa", "Usuario eliminado con éxito.", Alert.AlertType.INFORMATION);
                    cargarUsuarios();
                } catch (SQLException e) {
                    mostrarAlerta("Error en la base de datos", "No se pudo eliminar el usuario.", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Error", "Por favor, seleccione un usuario para eliminar.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) usuariosTable.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalAdministrador.fxml"));
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

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
