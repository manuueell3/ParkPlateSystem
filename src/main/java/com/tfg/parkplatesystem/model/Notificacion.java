package com.tfg.parkplatesystem.model;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Notificacion {

    private Long idNotificacion;
    private Long idUsuario;
    private String mensaje;
    private Boolean leida;
    private LocalDateTime fechaHora;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Notificacion(Long idNotificacion, Long idUsuario, String mensaje, Boolean leida, LocalDateTime fechaHora) {
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

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaHoraFormateada() {
        return this.fechaHora.format(FORMATTER);
    }

    // Método para obtener todas las notificaciones por usuario
    public static List<Notificacion> obtenerPorUsuario(Long idUsuario) {
        List<Notificacion> notificaciones = new ArrayList<>();
        String sql = "SELECT * FROM Notificaciones WHERE id_usuario = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Notificacion notificacion = new Notificacion(
                            rs.getLong("id_notificación"),
                            rs.getLong("id_usuario"),
                            rs.getString("mensaje"),
                            rs.getBoolean("leída"),
                            rs.getTimestamp("fecha_hora").toLocalDateTime()
                    );
                    notificaciones.add(notificacion);
                }
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
            stmt.setTimestamp(4, Timestamp.valueOf(this.fechaHora));
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
            stmt.setTimestamp(4, Timestamp.valueOf(this.fechaHora));
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
