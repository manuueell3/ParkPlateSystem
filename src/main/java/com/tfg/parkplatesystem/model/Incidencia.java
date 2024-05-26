package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Incidencia {

    private Long idIncidencia;
    private Long idUsuario;
    private String descripcion;
    private String fechaHora;
    private String estado;

    public Incidencia(Long idIncidencia, Long idUsuario, String descripcion, String fechaHora, String estado) {
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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
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
                        rs.getString("fecha_hora"),
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
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.descripcion);
            stmt.setString(3, this.fechaHora);
            stmt.setString(4, this.estado);
            stmt.executeUpdate();
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
            stmt.setString(3, this.fechaHora);
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
