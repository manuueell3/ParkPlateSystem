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

    public Tarifa(Long idTarifa, String descripcion, Double montoPorHora, Double montoPorDia) {
        this.idTarifa = idTarifa;
        this.descripcion = descripcion;
        this.montoPorHora = montoPorHora;
        this.montoPorDia = montoPorDia;
    }

    // Getters y setters
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
    public static List<Tarifa> obtenerTodas() {
        List<Tarifa> tarifas = new ArrayList<>();
        String sql = "SELECT * FROM Tarificaciones";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarifa tarifa = new Tarifa(
                        rs.getLong("id_tarifa"),
                        rs.getString("descripción"),
                        rs.getDouble("monto_por_hora"),
                        rs.getDouble("monto_por_día")
                );
                tarifas.add(tarifa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarifas;
    }

    // Método para guardar una tarifa
    public void guardar() {
        String sql = "INSERT INTO Tarificaciones (descripción, monto_por_hora, monto_por_día) VALUES (?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.descripcion);
            stmt.setDouble(2, this.montoPorHora);
            stmt.setDouble(3, this.montoPorDia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una tarifa
    public void actualizar() {
        String sql = "UPDATE Tarificaciones SET descripción = ?, monto_por_hora = ?, monto_por_día = ? WHERE id_tarifa = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.descripcion);
            stmt.setDouble(2, this.montoPorHora);
            stmt.setDouble(3, this.montoPorDia);
            stmt.setLong(4, this.idTarifa);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una tarifa
    public void eliminar() {
        String sql = "DELETE FROM Tarificaciones WHERE id_tarifa = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idTarifa);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
