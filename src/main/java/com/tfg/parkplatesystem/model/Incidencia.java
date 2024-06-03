package com.tfg.parkplatesystem.model;

import com.tfg.parkplatesystem.util.UtilMysql;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Incidencia {

    private Long idIncidencia;
    private Long idUsuario;
    private String descripcion;
    private LocalDateTime fechaHora;
    private String estado;

    public Incidencia(Long idIncidencia, Long idUsuario, String descripcion, LocalDateTime fechaHora, String estado) {
        this.idIncidencia = idIncidencia;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.estado = estado;
    }

    // Getters y setters
    public Long getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(Long idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para obtener todas las incidencias
    public static List<Incidencia> obtenerTodas() {
        List<Incidencia> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM Incidencias";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Incidencia incidencia = new Incidencia(
                        rs.getLong("id_incidencia"),
                        rs.getLong("id_usuario"),
                        rs.getString("descripción"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("estado")
                );
                incidencias.add(incidencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incidencias;
    }

    // Método para obtener incidencias por usuario
    public static List<Incidencia> obtenerPorUsuario(Long idUsuario) {
        List<Incidencia> incidencias = new ArrayList<>();
        String sql = "SELECT * FROM Incidencias WHERE id_usuario = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Incidencia incidencia = new Incidencia(
                        rs.getLong("id_incidencia"),
                        rs.getLong("id_usuario"),
                        rs.getString("descripción"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getString("estado")
                );
                incidencias.add(incidencia);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return incidencias;
    }

    // Método para guardar una incidencia
    public void guardar() {
        String sql = "INSERT INTO Incidencias (id_usuario, descripción, fecha_hora, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.descripcion);
            stmt.setTimestamp(3, Timestamp.valueOf(this.fechaHora));
            stmt.setString(4, this.estado);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        this.idIncidencia = generatedKeys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una incidencia
    public void actualizar() {
        String sql = "UPDATE Incidencias SET id_usuario = ?, descripción = ?, fecha_hora = ?, estado = ? WHERE id_incidencia = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.descripcion);
            stmt.setTimestamp(3, Timestamp.valueOf(this.fechaHora));
            stmt.setString(4, this.estado);
            stmt.setLong(5, this.idIncidencia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una incidencia
    public void eliminar() {
        String sql = "DELETE FROM Incidencias WHERE id_incidencia = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idIncidencia);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
