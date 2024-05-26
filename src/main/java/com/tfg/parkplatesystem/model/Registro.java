package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Registro {

    private Long idRegistro;
    private Long idUsuario;
    private String actividad;
    private String fechaHora;

    public Registro(Long idRegistro, Long idUsuario, String actividad, String fechaHora) {
        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.actividad = actividad;
        this.fechaHora = fechaHora;
    }

    // Getters y setters
    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    // Método para obtener todos los registros
    public static List<Registro> obtenerTodos() {
        List<Registro> registros = new ArrayList<>();
        String sql = "SELECT * FROM Registro";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Registro registro = new Registro(
                        rs.getLong("id_registro"),
                        rs.getLong("id_usuario"),
                        rs.getString("actividad"),
                        rs.getString("fecha_hora")
                );
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros;
    }

    // Método para guardar un registro
    public void guardar() {
        String sql = "INSERT INTO Registro (id_usuario, actividad, fecha_hora) VALUES (?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.actividad);
            stmt.setString(3, this.fechaHora);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un registro
    public void actualizar() {
        String sql = "UPDATE Registro SET id_usuario = ?, actividad = ?, fecha_hora = ? WHERE id_registro = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.actividad);
            stmt.setString(3, this.fechaHora);
            stmt.setLong(4, this.idRegistro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un registro
    public void eliminar() {
        String sql = "DELETE FROM Registro WHERE id_registro = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idRegistro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
