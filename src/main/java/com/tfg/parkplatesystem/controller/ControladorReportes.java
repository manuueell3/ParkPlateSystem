package com.tfg.parkplatesystem.controller;

import com.itextpdf.layout.properties.UnitValue;
import com.tfg.parkplatesystem.model.Reporte;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorReportes {

    @FXML
    private TableView<Reporte> reportesTable;

    @FXML
    private TableColumn<Reporte, Long> idColumn;

    @FXML
    private TableColumn<Reporte, String> descripcionColumn;

    @FXML
    private TableColumn<Reporte, String> fechaColumn;

    @FXML
    private TableColumn<Reporte, String> tipoColumn;

    @FXML
    private TextField descripcionTextField;

    @FXML
    private ComboBox<String> tipoComboBox;

    @FXML
    private TextField buscarTextField;

    private Usuario usuario;
    private ObservableList<Reporte> reportesData;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        descripcionColumn.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        fechaColumn.setCellValueFactory(cellData -> cellData.getValue().fechaProperty());
        tipoColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        cargarReportes();

        // Inicializar ComboBox con opciones
        tipoComboBox.setItems(FXCollections.observableArrayList(
                "Incidencia", "Mantenimiento", "Pago", "Sanción", "Reserva", "Evento"
        ));

        configurarBusqueda();
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private void cargarReportes() {
        List<Reporte> reportes = Reporte.obtenerTodos();
        reportesData = FXCollections.observableArrayList(reportes);
        reportesTable.setItems(reportesData);
    }

    @FXML
    public void handleAddReporte(ActionEvent event) {
        String descripcion = descripcionTextField.getText();
        String tipo = tipoComboBox.getValue();

        if (descripcion.isEmpty() || tipo == null || tipo.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        if (descripcion.length() > 255) {
            mostrarAlerta("Error de entrada", "La descripción no puede exceder los 255 caracteres.", Alert.AlertType.ERROR);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de Registro");
        alert.setHeaderText(null);
        alert.setContentText("¿Está seguro de que desea registrar este reporte?");

        if (alert.showAndWait().orElseThrow() == ButtonType.OK) {
            String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            try {
                Reporte nuevoReporte = new Reporte(null, descripcion, fecha, tipo, usuario.getIdUsuario());
                nuevoReporte.guardar();
                reportesData.add(nuevoReporte);

                limpiarCampos();
                mostrarAlerta("Registro exitoso", "El reporte ha sido registrado con éxito.", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                mostrarAlerta("Error al guardar", "No se pudo guardar el reporte. Inténtelo nuevamente.", Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleDeleteReporte(ActionEvent event) {
        Reporte selectedReporte = reportesTable.getSelectionModel().getSelectedItem();
        if (selectedReporte != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de Eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar este reporte?");

            if (alert.showAndWait().orElseThrow() == ButtonType.OK) {
                try {
                    selectedReporte.eliminar();
                    reportesData.remove(selectedReporte);
                    mostrarAlerta("Eliminación exitosa", "El reporte ha sido eliminado con éxito.", Alert.AlertType.INFORMATION);
                } catch (Exception e) {
                    mostrarAlerta("Error al eliminar", "No se pudo eliminar el reporte. Inténtelo nuevamente.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            mostrarAlerta("Selección vacía", "Por favor, seleccione un reporte para eliminar.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    public void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) reportesTable.getScene().getWindow();
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
            mostrarAlerta("Error al cargar", "No se pudo cargar la vista principal. Inténtelo nuevamente.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void configurarBusqueda() {
        FilteredList<Reporte> filteredData = new FilteredList<>(reportesData, p -> true);

        buscarTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(reporte -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (reporte.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reporte.getTipo().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Reporte> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(reportesTable.comparatorProperty());

        reportesTable.setItems(sortedData);
    }

    @FXML
    public void exportarCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.setInitialFileName("reportes.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        Stage stage = (Stage) reportesTable.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write("ID,Descripción,Fecha,Tipo\n");
                for (Reporte reporte : reportesTable.getItems()) {
                    writer.write(reporte.getId() + ","
                            + reporte.getDescripcion() + ","
                            + reporte.getFecha() + ","
                            + reporte.getTipo() + "\n");
                }
                mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                mostrarAlerta("Error de exportación", "No se pudo exportar los datos.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    public void exportarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.setInitialFileName("reportes.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        Stage stage = (Stage) reportesTable.getScene().getWindow();
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

            doc.add(new Paragraph("Reportes").setBold().setFontSize(18));

            Table table = new Table(new float[]{2, 5, 3, 3});
            table.setWidth(UnitValue.createPercentValue(100));  // Establecer el ancho de la tabla al 100% del ancho disponible

            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Descripción")));
            table.addHeaderCell(new Cell().add(new Paragraph("Fecha")));
            table.addHeaderCell(new Cell().add(new Paragraph("Tipo")));

            for (Reporte reporte : reportesTable.getItems()) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(reporte.getId()))));
                table.addCell(new Cell().add(new Paragraph(reporte.getDescripcion())));
                table.addCell(new Cell().add(new Paragraph(reporte.getFecha())));
                table.addCell(new Cell().add(new Paragraph(reporte.getTipo())));
            }

            doc.add(table);
            doc.close();
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarGraficoReportes(ActionEvent event) {
        Stage stage = new Stage();
        stage.setTitle("Gráfico de Reportes");

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Distribución de Tipos de Reportes");

        reportesData.stream()
                .collect(Collectors.groupingBy(Reporte::getTipo, Collectors.counting()))
                .forEach((tipo, count) -> pieChart.getData().add(new PieChart.Data(tipo, count)));

        Scene scene = new Scene(pieChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void limpiarCampos() {
        descripcionTextField.clear();
        tipoComboBox.setValue(null);
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
