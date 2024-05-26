package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Notificacion {

    private Long idNotificacion;
    private Long idUsuario;
    private String mensaje;
    private Boolean leida;
    private String fechaHora;

    public Notificacion(Long idNotificacion, Long idUsuario, String mensaje, Boolean leida, String fechaHora) {
        this.idNotificacion = idNotificacion;
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.leida = leida;
        this.fechaHora = fechaHora;
    }

    // Getters y setters
    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getLeida() {
        return leida;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    // Método para obtener todas las notificaciones
    public static List<Notificacion> obtenerTodas() {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificaciones";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Notificacion notificacion = new Notificacion(
                        rs.getLong("id_notificación"),
                        rs.getLong("id_usuario"),
                        rs.getString("mensaje"),
                        rs.getBoolean("leída"),
                        rs.getString("fecha_hora")
                );
                notificaciones.add(notificacion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificaciones;
    }

    // Método para guardar una notificación
    public void guardar() {
        String sql = "INSERT INTO Notificaciones (id_usuario, mensaje, leída, fecha_hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.mensaje);
            stmt.setBoolean(3, this.leida);
            stmt.setString(4, this.fechaHora);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una notificación
    public void actualizar() {
        String sql = "UPDATE Notificaciones SET id_usuario = ?, mensaje = ?, leída = ?, fecha_hora = ? WHERE id_notificación = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.mensaje);
            stmt.setBoolean(3, this.leida);
            stmt.setString(4, this.fechaHora);
            stmt.setLong(5, this.idNotificacion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una notificación
    public void eliminar() {
        String sql = "DELETE FROM Notificaciones WHERE id_notificación = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idNotificacion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
