package com.tfg.parkplatesystem.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.tfg.parkplatesystem.model.RolHistorial;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControladorRoles {

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
    private ComboBox<String> rolComboBox;

    @FXML
    private TextField searchTextField;

    @FXML
    private ComboBox<String> searchRolComboBox;

    private ObservableList<Usuario> usuariosObservableList;
    private FilteredList<Usuario> filteredData;
    private SortedList<Usuario> sortedData;

    private Usuario usuario;

    private static final Logger LOGGER = Logger.getLogger(ControladorRoles.class.getName());

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

        rolComboBox.setItems(FXCollections.observableArrayList("administrador", "conductor"));
        searchRolComboBox.setItems(FXCollections.observableArrayList("Todos", "administrador", "conductor"));

        cargarUsuarios();

        usuariosTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesUsuario(newValue));

        configurarBusqueda();
    }

    private void cargarUsuarios() {
        List<Usuario> usuarios = Usuario.obtenerTodos();
        usuariosObservableList = FXCollections.observableArrayList(usuarios);
        filteredData = new FilteredList<>(usuariosObservableList, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usuariosTable.comparatorProperty());
        usuariosTable.setItems(sortedData);
    }

    private void mostrarDetallesUsuario(Usuario usuario) {
        if (usuario != null) {
            nombreTextField.setText(usuario.getNombre());
            apellidosTextField.setText(usuario.getApellidos());
            correoTextField.setText(usuario.getEmail());
            rolComboBox.setValue(usuario.getRol());
        } else {
            limpiarCampos();
        }
    }

    @FXML
    public void handleUpdateRol(ActionEvent event) {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            String nuevoRol = rolComboBox.getValue();
            if (nuevoRol == null || nuevoRol.isEmpty()) {
                mostrarAlerta("Error de entrada", "Por favor, seleccione un rol.", Alert.AlertType.ERROR);
                return;
            }

            if (nuevoRol.equals(usuarioSeleccionado.getRol())) {
                mostrarAlerta("Error de entrada", "El usuario ya tiene el rol seleccionado.", Alert.AlertType.ERROR);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de cambio de rol");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea cambiar el rol de " + usuarioSeleccionado.getNombre() + " a " + nuevoRol + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    usuarioSeleccionado.actualizarRol(nuevoRol);
                    mostrarAlerta("Actualización exitosa", "Rol actualizado con éxito.", Alert.AlertType.INFORMATION);
                    cargarUsuarios();
                    // Cambiar el color del borde del campo para indicar éxito
                    rolComboBox.setStyle("-fx-border-color: green; -fx-border-width: 2px;");
                    // Volver al estilo normal después de 2 segundos
                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(e -> rolComboBox.setStyle(""));
                    pause.play();
                    configurarBusqueda();
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error en la base de datos al actualizar el rol", e);
                    mostrarAlerta("Error en la base de datos", "No se pudo actualizar el rol.", Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Error", "Por favor, seleccione un usuario para actualizar.", Alert.AlertType.ERROR);
        }
    }

    private void limpiarCampos() {
        nombreTextField.clear();
        apellidosTextField.clear();
        correoTextField.clear();
        rolComboBox.setValue(null);
    }

    private void configurarBusqueda() {
        if (filteredData == null) {
            filteredData = new FilteredList<>(usuariosObservableList, p -> true);
        }

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());
        searchRolComboBox.valueProperty().addListener((observable, oldValue, newValue) -> aplicarFiltros());

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(usuariosTable.comparatorProperty());

        usuariosTable.setItems(sortedData);
    }

    private void aplicarFiltros() {
        String textoBusqueda = searchTextField.getText().toLowerCase();
        String rolSeleccionado = searchRolComboBox.getValue();

        filteredData.setPredicate(usuario -> {
            boolean coincideTextoBusqueda = (textoBusqueda == null || textoBusqueda.isEmpty())
                    || usuario.getNombre().toLowerCase().contains(textoBusqueda)
                    || usuario.getApellidos().toLowerCase().contains(textoBusqueda)
                    || usuario.getEmail().toLowerCase().contains(textoBusqueda);

            boolean coincideRolSeleccionado = (rolSeleccionado == null || rolSeleccionado.isEmpty() || rolSeleccionado.equals("Todos"))
                    || usuario.getRol().equalsIgnoreCase(rolSeleccionado);

            return coincideTextoBusqueda && coincideRolSeleccionado;
        });
    }

    @FXML
    public void handleExportCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.setInitialFileName("usuarios.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        Stage stage = (Stage) usuariosTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            exportarCSV(file);
        }
    }

    private void exportarCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("ID,Nombre,Apellidos,Correo,Rol\n");
            for (Usuario usuario : usuariosTable.getItems()) {
                writer.write(usuario.getIdUsuario() + ","
                        + usuario.getNombre() + ","
                        + usuario.getApellidos() + ","
                        + usuario.getEmail() + ","
                        + usuario.getRol() + "\n");
            }
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleExportPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.setInitialFileName("usuarios.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        Stage stage = (Stage) usuariosTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            exportarPDF(file);
        }
    }

    private void exportarPDF(File file) {
        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document doc = new Document(pdfDoc);

            doc.add(new Paragraph("Usuarios").setBold().setFontSize(18));

            for (Usuario usuario : usuariosTable.getItems()) {
                doc.add(new Paragraph(
                        usuario.getIdUsuario() + " " +
                                usuario.getNombre() + " " +
                                usuario.getApellidos() + " " +
                                usuario.getEmail() + " " +
                                usuario.getRol()
                ));
            }

            doc.close();
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos.", Alert.AlertType.ERROR);
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
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al cargar la vista principal", e);
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

    @FXML
    public void handleViewHistorial(ActionEvent event) {
        Usuario usuarioSeleccionado = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            List<RolHistorial> historial = usuarioSeleccionado.obtenerHistorialRoles();
            StringBuilder historialTexto = new StringBuilder("Historial de Roles:\n\n");
            for (RolHistorial cambio : historial) {
                historialTexto.append("Usuario: ").append(cambio.getNombreUsuario())
                        .append(", Rol Anterior: ").append(cambio.getRolAnterior())
                        .append(", Rol Nuevo: ").append(cambio.getRolNuevo())
                        .append(", Fecha de Cambio: ").append(cambio.getFechaCambio())
                        .append("\n");
            }

            // Crear una nueva ventana para mostrar el historial
            Stage stage = new Stage();
            stage.setTitle("Historial de Roles");

            TextArea textArea = new TextArea(historialTexto.toString());
            textArea.setEditable(false);
            textArea.getStyleClass().add("historial-text-area");

            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            Scene scene = new Scene(scrollPane, 600, 400);
            scene.getStylesheets().add(getClass().getResource("/com/tfg/parkplatesystem/css/estilosGenerales.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } else {
            mostrarAlerta("Error", "Por favor, seleccione un usuario para ver su historial.", Alert.AlertType.ERROR);
        }
    }
}
