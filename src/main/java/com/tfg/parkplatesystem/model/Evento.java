package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Evento {

    private Long idEvento;
    private Long idUsuario;
    private String tipoEvento;
    private String descripcion;
    private String fechaHora;

    public Evento(Long idEvento, Long idUsuario, String tipoEvento, String descripcion, String fechaHora) {
        this.idEvento = idEvento;
        this.idUsuario = idUsuario;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    // Getters y setters
    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
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

    // Método para obtener todos los eventos
    public static List<Evento> obtenerTodos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT * FROM Eventos";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Evento evento = new Evento(
                        rs.getLong("id_evento"),
                        rs.getLong("id_usuario"),
                        rs.getString("tipo_evento"),
                        rs.getString("descripción"),
                        rs.getString("fecha_hora")
                );
                eventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }

    // Método para guardar un evento
    public void guardar() {
        String sql = "INSERT INTO Eventos (id_usuario, tipo_evento, descripción, fecha_hora) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.tipoEvento);
            stmt.setString(3, this.descripcion);
            stmt.setString(4, this.fechaHora);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un evento
    public void actualizar() {
        String sql = "UPDATE Eventos SET id_usuario = ?, tipo_evento = ?, descripción = ?, fecha_hora = ? WHERE id_evento = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.tipoEvento);
            stmt.setString(3, this.descripcion);
            stmt.setString(4, this.fechaHora);
            stmt.setLong(5, this.idEvento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un evento
    public void eliminar() {
        String sql = "DELETE FROM Eventos WHERE id_evento = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idEvento);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
