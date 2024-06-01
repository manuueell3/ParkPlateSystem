package com.tfg.parkplatesystem.controller;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.tfg.parkplatesystem.model.HistorialTarifas;
import com.tfg.parkplatesystem.model.Tarifa;
import com.tfg.parkplatesystem.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ControladorTarifas {

    @FXML
    private TableView<Tarifa> tablaTarifas;
    @FXML
    private TableColumn<Tarifa, Long> columnaId;
    @FXML
    private TableColumn<Tarifa, String> columnaDescripcion;
    @FXML
    private TableColumn<Tarifa, Double> columnaMontoPorHora;
    @FXML
    private TableColumn<Tarifa, Double> columnaMontoPorDia;
    @FXML
    private TextField txtDescripcion;
    @FXML
    private TextField txtMontoPorHora;
    @FXML
    private TextField txtMontoPorDia;
    @FXML
    private TextField txtBuscar;

    private Tarifa tarifaSeleccionada;
    private ObservableList<Tarifa> tarifasObservableList;
    private FilteredList<Tarifa> filteredData;
    private SortedList<Tarifa> sortedData;

    private Usuario usuario;
    private Stage graficoStage;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @FXML
    public void initialize() {
        columnaId.setCellValueFactory(new PropertyValueFactory<>("idTarifa"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaMontoPorHora.setCellValueFactory(new PropertyValueFactory<>("montoPorHora"));
        columnaMontoPorDia.setCellValueFactory(new PropertyValueFactory<>("montoPorDia"));
        cargarTarifas();

        tablaTarifas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetallesTarifa(newValue));

        configurarBusqueda();
    }

    private void cargarTarifas() {
        List<Tarifa> tarifas = Tarifa.obtenerTodasLasTarifas();
        tarifasObservableList = FXCollections.observableArrayList(tarifas);
        filteredData = new FilteredList<>(tarifasObservableList, p -> true);
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaTarifas.comparatorProperty());
        tablaTarifas.setItems(sortedData);
    }

    private void mostrarDetallesTarifa(Tarifa tarifa) {
        if (tarifa != null) {
            this.tarifaSeleccionada = tarifa;
            txtDescripcion.setText(tarifa.getDescripcion());
            txtMontoPorHora.setText(tarifa.getMontoPorHora().toString());
            txtMontoPorDia.setText(tarifa.getMontoPorDia().toString());
        } else {
            limpiarCampos();
        }
    }

    @FXML
    private void agregarTarifa(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }
        try {
            String descripcion = txtDescripcion.getText();
            Double montoPorHora = Double.parseDouble(txtMontoPorHora.getText());
            Double montoPorDia = Double.parseDouble(txtMontoPorDia.getText());
            Tarifa tarifa = new Tarifa(null, descripcion, montoPorHora, montoPorDia);
            tarifa.guardar();
            mostrarAlerta("Registro exitoso", "Tarifa registrada con éxito.", Alert.AlertType.INFORMATION);
            cargarTarifas();
            limpiarCampos();
            configurarBusqueda();
            actualizarGraficoTarifas(); // Actualizar el gráfico
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en la base de datos", "No se pudo registrar la tarifa.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void modificarTarifa(ActionEvent event) {
        if (tarifaSeleccionada == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione una tarifa para modificar.", Alert.AlertType.ERROR);
            return;
        }
        if (!validarCampos()) {
            return;
        }
        try {
            String descripcion = txtDescripcion.getText();
            Double montoPorHora = Double.parseDouble(txtMontoPorHora.getText());
            Double montoPorDia = Double.parseDouble(txtMontoPorDia.getText());

            if (descripcion.equals(tarifaSeleccionada.getDescripcion()) &&
                    montoPorHora.equals(tarifaSeleccionada.getMontoPorHora()) &&
                    montoPorDia.equals(tarifaSeleccionada.getMontoPorDia())) {
                mostrarAlerta("Sin cambios", "No se han realizado cambios en la tarifa.", Alert.AlertType.INFORMATION);
                return;
            }

            HistorialTarifas historial = new HistorialTarifas(
                    Math.toIntExact(tarifaSeleccionada.getIdTarifa()),
                    tarifaSeleccionada.getDescripcion(),
                    tarifaSeleccionada.getMontoPorHora(),
                    tarifaSeleccionada.getMontoPorDia(),
                    descripcion,
                    montoPorHora,
                    montoPorDia
            );
            historial.guardar();

            tarifaSeleccionada.setDescripcion(descripcion);
            tarifaSeleccionada.setMontoPorHora(montoPorHora);
            tarifaSeleccionada.setMontoPorDia(montoPorDia);
            tarifaSeleccionada.actualizar();
            mostrarAlerta("Actualización exitosa", "Tarifa actualizada con éxito.", Alert.AlertType.INFORMATION);
            cargarTarifas();
            limpiarCampos();
            configurarBusqueda();
            actualizarGraficoTarifas(); // Actualizar el gráfico
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en la base de datos", "No se pudo actualizar la tarifa.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void eliminarTarifa(ActionEvent event) {
        if (tarifaSeleccionada == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione una tarifa para eliminar.", Alert.AlertType.ERROR);
            return;
        }
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea eliminar la tarifa seleccionada?");

            if (alert.showAndWait().orElseThrow() == ButtonType.OK) {
                tarifaSeleccionada.eliminar();
                mostrarAlerta("Eliminación exitosa", "Tarifa eliminada con éxito.", Alert.AlertType.INFORMATION);
                cargarTarifas();
                limpiarCampos();
                configurarBusqueda();
                actualizarGraficoTarifas(); // Actualizar el gráfico
            }
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en la base de datos", "No se pudo eliminar la tarifa.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void duplicarTarifa(ActionEvent event) {
        if (tarifaSeleccionada == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione una tarifa para duplicar.", Alert.AlertType.ERROR);
            return;
        }
        try {
            Tarifa tarifaDuplicada = new Tarifa(null, tarifaSeleccionada.getDescripcion(), tarifaSeleccionada.getMontoPorHora(), tarifaSeleccionada.getMontoPorDia());
            tarifaDuplicada.guardar();
            mostrarAlerta("Duplicación exitosa", "Tarifa duplicada con éxito.", Alert.AlertType.INFORMATION);
            cargarTarifas();
            configurarBusqueda();
            actualizarGraficoTarifas(); // Actualizar el gráfico
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarAlerta("Error en la base de datos", "No se pudo duplicar la tarifa.", Alert.AlertType.ERROR);
        }
    }

    private boolean validarCampos() {
        String descripcion = txtDescripcion.getText();
        String montoPorHora = txtMontoPorHora.getText();
        String montoPorDia = txtMontoPorDia.getText();

        if (descripcion.isEmpty() || montoPorHora.isEmpty() || montoPorDia.isEmpty()) {
            mostrarAlerta("Error de entrada", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return false;
        }

        try {
            double montoHora = Double.parseDouble(montoPorHora);
            double montoDia = Double.parseDouble(montoPorDia);
            if (montoHora < 0 || montoHora > 1000 || montoDia < 0 || montoDia > 10000) {
                mostrarAlerta("Error de entrada", "Los montos deben estar dentro de un rango razonable.", Alert.AlertType.ERROR);
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de entrada", "Monto por hora y monto por día deben ser números válidos.", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }

    @FXML
    private void limpiarCampos() {
        txtDescripcion.clear();
        txtMontoPorHora.clear();
        txtMontoPorDia.clear();
        tarifaSeleccionada = null;
    }

    private void configurarBusqueda() {
        if (filteredData == null) {
            filteredData = new FilteredList<>(tarifasObservableList, p -> true);
        }

        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(tarifa -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (tarifa.getDescripcion().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (tarifa.getMontoPorHora().toString().contains(lowerCaseFilter)) {
                    return true;
                } else if (tarifa.getMontoPorDia().toString().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tablaTarifas.comparatorProperty());

        tablaTarifas.setItems(sortedData);
    }

    @FXML
    private void exportarCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo CSV");
        fileChooser.setInitialFileName("tarifas.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
        Stage stage = (Stage) tablaTarifas.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            exportarCSV(file);
        }
    }

    private void exportarCSV(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("ID,Descripcion,Monto por Hora,Monto por Dia\n");
            for (Tarifa tarifa : tablaTarifas.getItems()) {
                writer.write(tarifa.getIdTarifa() + ","
                        + tarifa.getDescripcion() + ","
                        + tarifa.getMontoPorHora() + ","
                        + tarifa.getMontoPorDia() + "\n");
            }
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void exportarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.setInitialFileName("tarifas.pdf");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        Stage stage = (Stage) tablaTarifas.getScene().getWindow();
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

            doc.add(new Paragraph("Tarifas").setBold().setFontSize(18));

            for (Tarifa tarifa : tablaTarifas.getItems()) {
                doc.add(new Paragraph(
                        tarifa.getIdTarifa() + " " +
                                tarifa.getDescripcion() + " " +
                                tarifa.getMontoPorHora() + " " +
                                tarifa.getMontoPorDia()
                ));
            }

            doc.close();
            mostrarAlerta("Exportación exitosa", "Los datos se han exportado correctamente a " + file.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error de exportación", "No se pudo exportar los datos.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void verHistorial(ActionEvent event) {
        if (tarifaSeleccionada == null) {
            mostrarAlerta("Error de selección", "Por favor, seleccione una tarifa para ver el historial.", Alert.AlertType.ERROR);
            return;
        }
        try {
            List<HistorialTarifas> historial = HistorialTarifas.obtenerHistorialPorIdTarifa(Math.toIntExact(tarifaSeleccionada.getIdTarifa()));
            if (historial.isEmpty()) {
                mostrarAlerta("Historial vacío", "No hay cambios registrados para esta tarifa.", Alert.AlertType.INFORMATION);
                return;
            }

            StringBuilder historialTexto = new StringBuilder("Historial de Tarifas:\n\n");
            for (HistorialTarifas cambio : historial) {
                historialTexto.append("ID Tarifa: ").append(cambio.getIdTarifa()).append("\n")
                        .append("Descripción Anterior: ").append(cambio.getDescripcionAnterior()).append("\n")
                        .append("Monto por Hora Anterior: ").append(cambio.getMontoPorHoraAnterior()).append("\n")
                        .append("Monto por Día Anterior: ").append(cambio.getMontoPorDiaAnterior()).append("\n")
                        .append("Descripción Nueva: ").append(cambio.getDescripcionNueva()).append("\n")
                        .append("Monto por Hora Nuevo: ").append(cambio.getMontoPorHoraNueva()).append("\n")
                        .append("Monto por Día Nuevo: ").append(cambio.getMontoPorDiaNueva()).append("\n")
                        .append("Fecha de Cambio: ").append(cambio.getFechaCambio()).append("\n\n");
            }

            // Crear una nueva ventana para mostrar el historial
            Stage stage = new Stage();
            stage.setTitle("Historial de Tarifas");

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
        } catch (SQLException e) {
            mostrarAlerta("Error en la base de datos", "No se pudo obtener el historial de tarifas.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarEstadisticas(ActionEvent event) {
        double totalMontoPorHora = 0;
        double totalMontoPorDia = 0;
        int contador = tarifasObservableList.size();

        for (Tarifa tarifa : tarifasObservableList) {
            totalMontoPorHora += tarifa.getMontoPorHora();
            totalMontoPorDia += tarifa.getMontoPorDia();
        }

        double promedioMontoPorHora = totalMontoPorHora / contador;
        double promedioMontoPorDia = totalMontoPorDia / contador;

        mostrarAlerta("Estadísticas de Tarifas", "Promedio Monto por Hora: " + promedioMontoPorHora + "\nPromedio Monto por Día: " + promedioMontoPorDia, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void mostrarGraficoTarifas(ActionEvent event) {
        if (graficoStage != null && graficoStage.isShowing()) {
            graficoStage.toFront();
            return;
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Tarifas");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Montos");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Montos por Hora y por Día de las Tarifas");

        XYChart.Series<String, Number> seriesHora = new XYChart.Series<>();
        seriesHora.setName("Monto por Hora");

        XYChart.Series<String, Number> seriesDia = new XYChart.Series<>();
        seriesDia.setName("Monto por Día");

        for (Tarifa tarifa : tarifasObservableList) {
            seriesHora.getData().add(new XYChart.Data<>(tarifa.getDescripcion(), tarifa.getMontoPorHora()));
            seriesDia.getData().add(new XYChart.Data<>(tarifa.getDescripcion(), tarifa.getMontoPorDia()));
        }

        barChart.getData().addAll(seriesHora, seriesDia);

        VBox vbox = new VBox(barChart);
        vbox.setPadding(new Insets(10));

        graficoStage = new Stage();
        graficoStage.setTitle("Gráfico de Tarifas");
        graficoStage.setScene(new Scene(vbox, 800, 600));
        graficoStage.show();
    }

    private void actualizarGraficoTarifas() {
        if (graficoStage != null && graficoStage.isShowing()) {
            graficoStage.close();
            mostrarGraficoTarifas(new ActionEvent());
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
    private void handleBackButton(ActionEvent event) {
        try {
            Stage stage = (Stage) tablaTarifas.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tfg/parkplatesystem/fxml/principalAdministrador.fxml"));
            Parent root = loader.load();

            // Pasa el usuario al controlador de la vista principal
            ControladorPrincipal controladorPrincipal = loader.getController();
            controladorPrincipal.setUsuario(usuario); // Asumiendo que el controlador principal necesita un usuario

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Park Plate System - Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error de navegación", "No se pudo volver al menú principal.", Alert.AlertType.ERROR);
        }
    }
}
