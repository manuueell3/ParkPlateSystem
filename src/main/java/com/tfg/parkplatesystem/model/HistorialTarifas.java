package com.tfg.parkplatesystem.model;

import com.tfg.parkplatesystem.util.UtilMysql;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistorialTarifas {
    private Integer idHistorial;
    private Integer idTarifa;
    private String descripcionAnterior;
    private Double montoPorHoraAnterior;
    private Double montoPorDiaAnterior;
    private String descripcionNueva;
    private Double montoPorHoraNueva;
    private Double montoPorDiaNueva;
    private String fechaCambio;

    public HistorialTarifas(Integer idTarifa, String descripcionAnterior, Double montoPorHoraAnterior, Double montoPorDiaAnterior,
                            String descripcionNueva, Double montoPorHoraNueva, Double montoPorDiaNueva) {
        this.idTarifa = idTarifa;
        this.descripcionAnterior = descripcionAnterior;
        this.montoPorHoraAnterior = montoPorHoraAnterior;
        this.montoPorDiaAnterior = montoPorDiaAnterior;
        this.descripcionNueva = descripcionNueva;
        this.montoPorHoraNueva = montoPorHoraNueva;
        this.montoPorDiaNueva = montoPorDiaNueva;
    }

    public Integer getIdHistorial() {
        return idHistorial;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public String getDescripcionAnterior() {
        return descripcionAnterior;
    }

    public Double getMontoPorHoraAnterior() {
        return montoPorHoraAnterior;
    }

    public Double getMontoPorDiaAnterior() {
        return montoPorDiaAnterior;
    }

    public String getDescripcionNueva() {
        return descripcionNueva;
    }

    public Double getMontoPorHoraNueva() {
        return montoPorHoraNueva;
    }

    public Double getMontoPorDiaNueva() {
        return montoPorDiaNueva;
    }

    public String getFechaCambio() {
        return fechaCambio;
    }

    public void guardar() throws SQLException {
        String sql = "INSERT INTO HistorialTarifas (id_tarifa, descripcion_anterior, monto_por_hora_anterior, monto_por_dia_anterior, " +
                "descripcion_nueva, monto_por_hora_nueva, monto_por_dia_nueva) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, this.idTarifa);
            stmt.setString(2, this.descripcionAnterior);
            stmt.setDouble(3, this.montoPorHoraAnterior);
            stmt.setDouble(4, this.montoPorDiaAnterior);
            stmt.setString(5, this.descripcionNueva);
            stmt.setDouble(6, this.montoPorHoraNueva);
            stmt.setDouble(7, this.montoPorDiaNueva);
            stmt.executeUpdate();
        }
    }

    public static List<HistorialTarifas> obtenerHistorialPorIdTarifa(int idTarifa) throws SQLException {
        List<HistorialTarifas> historial = new ArrayList<>();
        String sql = "SELECT * FROM HistorialTarifas WHERE id_tarifa = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idTarifa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    HistorialTarifas cambio = new HistorialTarifas(
                            rs.getInt("id_tarifa"),
                            rs.getString("descripcion_anterior"),
                            rs.getDouble("monto_por_hora_anterior"),
                            rs.getDouble("monto_por_dia_anterior"),
                            rs.getString("descripcion_nueva"),
                            rs.getDouble("monto_por_hora_nueva"),
                            rs.getDouble("monto_por_dia_nueva")
                    );
                    cambio.idHistorial = rs.getInt("id_historial");
                    cambio.fechaCambio = rs.getString("fecha_cambio");
                    historial.add(cambio);
                }
            }
        }
        return historial;
    }
}
