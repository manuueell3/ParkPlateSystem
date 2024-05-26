package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Mantenimiento {

    private Long idMantenimiento;
    private Long idPlaza;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String estado;

    public Mantenimiento(Long idMantenimiento, Long idPlaza, String descripcion, String fechaInicio, String fechaFin, String estado) {
        this.idMantenimiento = idMantenimiento;
        this.idPlaza = idPlaza;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    // Getters y setters
    public Long getIdMantenimiento() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(Long idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public Long getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(Long idPlaza) {
        this.idPlaza = idPlaza;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para obtener todos los mantenimientos
    public static List<Mantenimiento> obtenerTodos() {
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        String sql = "SELECT * FROM HistorialMantenimientos";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Mantenimiento mantenimiento = new Mantenimiento(
                        rs.getLong("id_mantenimiento"),
                        rs.getLong("id_plaza"),
                        rs.getString("descripción"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin"),
                        rs.getString("estado")
                );
                mantenimientos.add(mantenimiento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mantenimientos;
    }

    // Método para guardar un mantenimiento
    public void guardar() {
        String sql = "INSERT INTO HistorialMantenimientos (id_plaza, descripción, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idPlaza);
            stmt.setString(2, this.descripcion);
            stmt.setString(3, this.fechaInicio);
            stmt.setString(4, this.fechaFin);
            stmt.setString(5, this.estado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un mantenimiento
    public void actualizar() {
        String sql = "UPDATE HistorialMantenimientos SET id_plaza = ?, descripción = ?, fecha_inicio = ?, fecha_fin = ?, estado = ? WHERE id_mantenimiento = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idPlaza);
            stmt.setString(2, this.descripcion);
            stmt.setString(3, this.fechaInicio);
            stmt.setString(4, this.fechaFin);
            stmt.setString(5, this.estado);
            stmt.setLong(6, this.idMantenimiento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un mantenimiento
    public void eliminar() {
        String sql = "DELETE FROM HistorialMantenimientos WHERE id_mantenimiento = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idMantenimiento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
