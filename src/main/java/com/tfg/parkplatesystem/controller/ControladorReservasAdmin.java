package com.tfg.parkplatesystem.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.tfg.parkplatesystem.model.Reserva;
import com.tfg.parkplatesystem.model.Usuario;
import com.tfg.parkplatesystem.util.UtilMysql;
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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ControladorReservasAdmin {

    @FXML
    private TableView<Reserva> tablaReservas;

    @FXML
    private TableColumn<Reserva, Integer> colId;

    @FXML
    private TableColumn<Reserva, String> colUsuario;

    @FXML
    private TableColumn<Reserva, String> colPlaza;

    @FXML
    private TableColumn<Reserva, String> colInicio;

    @FXML
    private TableColumn<Reserva, String> colFin;

    @FXML
    private TableColumn<Reserva, String> colEstado;

    @FXML
    private ComboBox<String> usuarioComboBox;

    @FXML
    private ComboBox<String> plazaComboBox;

    @FXML
    private DatePicker inicioDatePicker;

    @FXML
    private ComboBox<String> inicioHourComboBox;

    @FXML
    private ComboBox<String> inicioMinuteComboBox;

    @FXML
    private DatePicker finDatePicker;

    @FXML
    private ComboBox<String> finHourComboBox;

    @FXML
    private ComboBox<String> finMinuteComboBox;

    @FXML
    private ComboBox<String> estadoComboBox;

    @FXML
    private TextField searchTextField;

    private ObservableList<Reserva> reservasObservableList;

    private Reserva reservaSeleccionada;

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idReserva"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
        colPlaza.setCellValueFactory(new PropertyValueFactory<>("plaza"));
        colInicio.setCellValueFactory(new PropertyValueFactory<>("fechaHoraInicio"));
        colFin.setCellValueFactory(new PropertyValueFactory<>("fechaHoraFin"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        estadoComboBox.setItems(FXCollections.observableArrayList("pendiente", "confirmada", "cancelada"));
        inicializarComboBoxes();

        cargarReservas();
        configurarBusqueda();
    }

    private void inicializarComboBoxes() {
        // Cargar datos de usuarios y plazas en los ComboBoxes
        try (Connection conn = UtilMysql.getConnection()) {
            cargarUsuarios(conn);
            cargarPlazas(conn);

            // Inicializar horas y minutos
            ObservableList<String> horas = FXCollections.observableArrayList();
            ObservableList<String> minutos = FXCollections.observableArrayList();
            for (int i = 0; i < 24; i++) horas.add(String.format("%02d", i));
            for (int i = 0; i < 60; i++) minutos.add(String.format("%02d", i));
            inicioHourComboBox.setItems(horas);
            inicioMinuteComboBox.setItems(minutos);
            finHourComboBox.setItems(horas);
            finMinuteComboBox.setItems(minutos);
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudieron cargar los usuarios y plazas.", Alert.AlertType.ERROR);
        }
    }

    private void cargarUsuarios(Connection conn) throws SQLException {
        ObservableList<String> usuarios = FXCollections.observableArrayList();
        String sql = "SELECT nombre FROM Usuarios";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                usuarios.add(rs.getString("nombre"));
            }
            usuarioComboBox.setItems(usuarios);
        }
    }

    private void cargarPlazas(Connection conn) throws SQLException {
        ObservableList<String> plazas = FXCollections.observableArrayList();
        String sql = "SELECT numero_plaza FROM Plazas";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                plazas.add(rs.getString("numero_plaza"));
            }
            plazaComboBox.setItems(plazas);
        }
    }

    private void cargarReservas() {
        List<Reserva> reservas = Reserva.obtenerTodas();
        reservasObservableList = FXCollections.observableArrayList(reservas);
        tablaReservas.setItems(reservasObservableList);
    }

    private void configurarBusqueda() {
        FilteredList<Reserva> filteredData = new FilteredList<>(reservasObservableList, p -> true);

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reserva -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (reserva.getUsuario().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reserva.getPlaza().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reserva.getFechaHoraInicio().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reserva.getFechaHoraFin().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reserva.getEstado().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Reserva> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaReservas.comparatorProperty());

        tablaReservas.setItems(sortedData);
    }

    @FXML
    public void handleAddReserva(ActionEvent event) {
        String usuario = usuarioComboBox.getValue();
        String plaza = plazaComboBox.getValue();
        LocalDate inicioDate = inicioDatePicker.getValue();
        String inicioHour = inicioHourComboBox.getValue();
        String inicioMinute = inicioMinuteComboBox.getValue();
        LocalDate finDate = finDatePicker.getValue();
        String finHour = finHourComboBox.getValue();
        String finMinute = finMinuteComboBox.getValue();
        String estado = estadoComboBox.getValue();

        if (!validarCampos(usuario, plaza, inicioDate, inicioHour, inicioMinute, finDate, finHour, finMinute, estado)) {
            return;
        }

        try {
            Integer idUsuario = obtenerIdUsuario(usuario);
            Integer idPlaza = obtenerIdPlaza(plaza);
            if (idUsuario == null || idPlaza == null) {
                mostrarAlerta("Error", "Usuario o Plaza no encontrados.", Alert.AlertType.ERROR);
                return;
            }

            String inicio = formatearFechaHora(inicioDate, inicioHour, inicioMinute);
            String fin = formatearFechaHora(finDate, finHour, finMinute);

            Reserva nuevaReserva = new Reserva(
                    null,
                    idUsuario,
                    idPlaza,
                    inicio,
                    fin,
                    estado
            );

            nuevaReserva.guardar();
            mostrarAlerta("Registro exitoso", "Reserva registrada con éxito.", Alert.AlertType.INFORMATION);
            cargarReservas();
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo registrar la reserva. Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleEditReserva(ActionEvent event) {
        reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada != null) {
            usuarioComboBox.setValue(reservaSeleccionada.getUsuario());
            plazaComboBox.setValue(reservaSeleccionada.getPlaza());

            LocalDateTime inicioDateTime = LocalDateTime.parse(reservaSeleccionada.getFechaHoraInicio(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            inicioDatePicker.setValue(inicioDateTime.toLocalDate());
            inicioHourComboBox.setValue(String.format("%02d", inicioDateTime.getHour()));
            inicioMinuteComboBox.setValue(String.format("%02d", inicioDateTime.getMinute()));

            LocalDateTime finDateTime = LocalDateTime.parse(reservaSeleccionada.getFechaHoraFin(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            finDatePicker.setValue(finDateTime.toLocalDate());
            finHourComboBox.setValue(String.format("%02d", finDateTime.getHour()));
            finMinuteComboBox.setValue(String.format("%02d", finDateTime.getMinute()));

            estadoComboBox.setValue(reservaSeleccionada.getEstado());
        } else {
            mostrarAlerta("Error", "Por favor, seleccione una reserva para editar.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleUpdateReserva(ActionEvent event) {
        if (reservaSeleccionada != null) {
            try {
                Integer idUsuario = obtenerIdUsuario(usuarioComboBox.getValue());
                Integer idPlaza = obtenerIdPlaza(plazaComboBox.getValue());
                if (idUsuario == null || idPlaza == null) {
                    mostrarAlerta("Error", "Usuario o Plaza no encontrados.", Alert.AlertType.ERROR);
                    return;
                }

                LocalDate inicioDate = inicioDatePicker.getValue();
                String inicioHour = inicioHourComboBox.getValue();
                String inicioMinute = inicioMinuteComboBox.getValue();
                LocalDate finDate = finDatePicker.getValue();
                String finHour = finHourComboBox.getValue();
                String finMinute = finMinuteComboBox.getValue();

                if (!esFechaHoraValida(inicioDate, inicioHour, inicioMinute, finDate, finHour, finMinute)) {
                    mostrarAlerta("Error de entrada", "La fecha y hora de fin deben ser posteriores a la fecha y hora de inicio.", Alert.AlertType.ERROR);
                    return;
                }

                reservaSeleccionada.setIdUsuario(idUsuario);
                reservaSeleccionada.setIdPlaza(idPlaza);
                reservaSeleccionada.setFechaHoraInicio(formatearFechaHora(inicioDate, inicioHour, inicioMinute));
                reservaSeleccionada.setFechaHoraFin(formatearFechaHora(finDate, finHour, finMinute));
                reservaSeleccionada.setEstado(estadoComboBox.getValue());

                reservaSeleccionada.actualizar();
                mostrarAlerta("Actualización exitosa", "Reserva actualizada con éxito.", Alert.AlertType.INFORMATION);
                cargarReservas();
            } catch (SQLException e) {
                mostrarAlerta("Error en la base de datos", "No se pudo actualizar la reserva. Error: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void handleDeleteReserva(ActionEvent event) {
        Reserva reservaSeleccionada = tablaReservas.getSelectionModel().getSelectedItem();
        if (reservaSeleccionada != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar la reserva seleccionada?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    reservaSeleccionada.eliminar();
                    mostrarAlerta("Eliminación exitosa", "Reserva eliminada con éxito.", Alert.AlertType.INFORMATION);
                    cargarReservas();
                } catch (SQLException e) {
                    mostrarAlerta("Error en la base de datos", "No se pudo eliminar la reserva. Error: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        } else {
            mostrarAlerta("Error", "Por favor, seleccione una reserva para eliminar.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleExportCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.setInitialFileName("reservas.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        Stage stage = (Stage) tablaReservas.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            exportarCSV(file);
        }
    }

    private void exportarCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("ID,Usuario,Plaza,Inicio,Fin,Estado\n");
            for (Reserva reserva : tablaReservas.getItems()) {
                writer.write(reserva.getIdReserva() + ","
                        + reserva.getUsuario() + ","
                        + reserva.getPlaza() + ","
                        + reserva.getFechaHoraInicio() + ","
                        + reserva.getFechaHoraFin() + ","
                        + reserva.getEstado() + "\n");
            }
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos. Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleExportPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.setInitialFileName("reservas.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        Stage stage = (Stage) tablaReservas.getScene().getWindow();
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

            doc.add(new Paragraph("Reservas").setBold().setFontSize(18));

            for (Reserva reserva : tablaReservas.getItems()) {
                doc.add(new Paragraph(
                        reserva.getIdReserva() + " " +
                                reserva.getUsuario() + " " +
                                reserva.getPlaza() + " " +
                                reserva.getFechaHoraInicio() + " " +
                                reserva.getFechaHoraFin() + " " +
                                reserva.getEstado()
                ));
            }

            doc.close();
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos. Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaReservas.getScene().getWindow();
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

    private boolean validarCampos(String usuario, String plaza, LocalDate inicioDate, String inicioHour, String inicioMinute, LocalDate finDate, String finHour, String finMinute, String estado) {
        if (usuario == null || usuario.isEmpty() || plaza == null || plaza.isEmpty() ||
                inicioDate == null || inicioHour == null || inicioMinute == null ||
                finDate == null || finHour == null || finMinute == null || estado == null || estado.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }

        if (!esFechaHoraValida(inicioDate, inicioHour, inicioMinute, finDate, finHour, finMinute)) {
            mostrarAlerta("Error de entrada", "La fecha y hora de fin deben ser posteriores a la fecha y hora de inicio.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    private boolean esFechaHoraValida(LocalDate inicioDate, String inicioHour, String inicioMinute, LocalDate finDate, String finHour, String finMinute) {
        try {
            LocalDateTime inicio = LocalDateTime.of(inicioDate, LocalTime.of(Integer.parseInt(inicioHour), Integer.parseInt(inicioMinute)));
            LocalDateTime fin = LocalDateTime.of(finDate, LocalTime.of(Integer.parseInt(finHour), Integer.parseInt(finMinute)));
            return fin.isAfter(inicio);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String formatearFechaHora(LocalDate date, String hour, String minute) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + " " + hour + ":" + minute + ":00";
    }

    private Integer obtenerIdUsuario(String nombreUsuario) {
        Integer idUsuario = null;
        String sql = "SELECT id_usuario FROM Usuarios WHERE nombre = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombreUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idUsuario = rs.getInt("id_usuario");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idUsuario;
    }

    private Integer obtenerIdPlaza(String numeroPlaza) {
        Integer idPlaza = null;
        String sql = "SELECT id_plaza FROM Plazas WHERE numero_plaza = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, numeroPlaza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idPlaza = rs.getInt("id_plaza");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idPlaza;
    }

    @FXML
    public void handleViewGraph(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/graficoReservas.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Gráfico de Reservas");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
