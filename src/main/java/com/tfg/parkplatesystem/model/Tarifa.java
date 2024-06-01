package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Tarifa {

    private Long idTarifa;
    private String descripcion;
    private Double montoPorHora;
    private Double montoPorDia;

    // Constructor
    public Tarifa(Long idTarifa, String descripcion, Double montoPorHora, Double montoPorDia) {
        this.idTarifa = idTarifa;
        this.descripcion = descripcion;
        this.montoPorHora = montoPorHora;
        this.montoPorDia = montoPorDia;
    }

    // Getters y setters...
    public Long getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Long idTarifa) {
        this.idTarifa = idTarifa;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMontoPorHora() {
        return montoPorHora;
    }

    public void setMontoPorHora(Double montoPorHora) {
        this.montoPorHora = montoPorHora;
    }

    public Double getMontoPorDia() {
        return montoPorDia;
    }

    public void setMontoPorDia(Double montoPorDia) {
        this.montoPorDia = montoPorDia;
    }

    // Método para obtener todas las tarifas
    public static List<Tarifa> obtenerTodasLasTarifas() {
        List<Tarifa> tarifas = new ArrayList<>();
        String sql = "SELECT * FROM Tarifas";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarifa tarifa = new Tarifa(
                        rs.getLong("id_tarifa"),
                        rs.getString("descripcion"),
                        rs.getDouble("monto_por_hora"),
                        rs.getDouble("monto_por_dia")
                );
                tarifas.add(tarifa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifas;
    }

    // Método para guardar una nueva tarifa
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Tarifas (descripcion, monto_por_hora, monto_por_dia) VALUES (?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.descripcion);
            stmt.setDouble(2, this.montoPorHora);
            stmt.setDouble(3, this.montoPorDia);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar una tarifa existente
    public void actualizar() throws SQLException {
        String sql = "UPDATE Tarifas SET descripcion = ?, monto_por_hora = ?, monto_por_dia = ? WHERE id_tarifa = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.descripcion);
            stmt.setDouble(2, this.montoPorHora);
            stmt.setDouble(3, this.montoPorDia);
            stmt.setLong(4, this.idTarifa);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar una tarifa
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Tarifas WHERE id_tarifa = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idTarifa);
            stmt.executeUpdate();
        }
    }
}
