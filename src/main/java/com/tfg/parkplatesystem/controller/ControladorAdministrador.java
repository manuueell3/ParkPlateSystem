package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorAdministrador {

    @FXML
    private Label nombreUsuarioLabel;

    @FXML
    private TableView<Usuario> tablaUsuarios;

    @FXML
    private TableColumn<Usuario, Long> columnaId;

    @FXML
    private TableColumn<Usuario, String> columnaNombre;

    @FXML
    private TableColumn<Usuario, String> columnaApellidos;

    @FXML
    private TableColumn<Usuario, String> columnaEmail;

    @FXML
    private TableColumn<Usuario, String> columnaRol;

    public void setUsuario(Usuario usuario) {
        nombreUsuarioLabel.setText(usuario.getNombre() + " " + usuario.getApellidos());
        cargarUsuarios();
    }

    @FXML
    private void initialize() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaRol.setCellValueFactory(new PropertyValueFactory<>("rol"));
    }

    private void cargarUsuarios() {
        tablaUsuarios.getItems().setAll(Usuario.obtenerTodos());
    }
}
