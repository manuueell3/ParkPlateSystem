package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Reporte {

    private Long idReporte;
    private String titulo;
    private String descripcion;
    private String fechaHora;
    private Long idUsuario;

    public Reporte(Long idReporte, String titulo, String descripcion, String fechaHora, Long idUsuario) {
        this.idReporte = idReporte;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.idUsuario = idUsuario;
    }

    // Getters y setters
    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Método para obtener todos los reportes
    public static List<Reporte> obtenerTodos() {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM Reportes";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reporte reporte = new Reporte(
                        rs.getLong("id_reporte"),
                        rs.getString("titulo"),
                        rs.getString("descripcion"),
                        rs.getString("fecha_hora"),
                        rs.getLong("id_usuario")
                );
                reportes.add(reporte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportes;
    }

    // Método para guardar un reporte
    public void guardar() {
        String sql = "INSERT INTO Reportes (titulo, descripcion, fecha_hora, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.titulo);
            stmt.setString(2, this.descripcion);
            stmt.setString(3, this.fechaHora);
            stmt.setLong(4, this.idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un reporte
    public void actualizar() {
        String sql = "UPDATE Reportes SET titulo = ?, descripcion = ?, fecha_hora = ?, id_usuario = ? WHERE id_reporte = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.titulo);
            stmt.setString(2, this.descripcion);
            stmt.setString(3, this.fechaHora);
            stmt.setLong(4, this.idUsuario);
            stmt.setLong(5, this.idReporte);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un reporte
    public void eliminar() {
        String sql = "DELETE FROM Reportes WHERE id_reporte = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idReporte);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
