package com.tfg.parkplatesystem.controller;

import com.tfg.parkplatesystem.model.Reserva;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ControladorGraficoReservas {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    public void initialize() {
        cargarDatosGrafico();
    }

    private void cargarDatosGrafico() {
        List<Reserva> reservas = Reserva.obtenerTodas();
        Map<String, Long> conteoPorEstado = reservas.stream()
                .collect(Collectors.groupingBy(Reserva::getEstado, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        conteoPorEstado.forEach((estado, cantidad) -> series.getData().add(new XYChart.Data<>(estado, cantidad)));

        barChart.getData().add(series);
    }
}
