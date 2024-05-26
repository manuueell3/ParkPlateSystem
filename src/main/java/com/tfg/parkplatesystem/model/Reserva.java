package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Reserva {

    private Long idReserva;
    private Long idUsuario;
    private Long idPlaza;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private String estado;

    public Reserva(Long idReserva, Long idUsuario, Long idPlaza, String fechaHoraInicio, String fechaHoraFin, String estado) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idPlaza = idPlaza;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = estado;
    }

    // Getters y setters
    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(Long idPlaza) {
        this.idPlaza = idPlaza;
    }

    public String getFechaHoraInicio() {
        return fechaHoraInicio;
    }

    public void setFechaHoraInicio(String fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public String getFechaHoraFin() {
        return fechaHoraFin;
    }

    public void setFechaHoraFin(String fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para obtener todas las reservas
    public static List<Reserva> obtenerTodas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reservas";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getLong("id_reserva"),
                        rs.getLong("id_usuario"),
                        rs.getLong("id_plaza"),
                        rs.getString("fecha_hora_inicio"),
                        rs.getString("fecha_hora_fin"),
                        rs.getString("estado")
                );
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    // Método para guardar una reserva
    public void guardar() {
        String sql = "INSERT INTO Reservas (id_usuario, id_plaza, fecha_hora_inicio, fecha_hora_fin, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setLong(2, this.idPlaza);
            stmt.setString(3, this.fechaHoraInicio);
            stmt.setString(4, this.fechaHoraFin);
            stmt.setString(5, this.estado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una reserva
    public void actualizar() {
        String sql = "UPDATE Reservas SET id_usuario = ?, id_plaza = ?, fecha_hora_inicio = ?, fecha_hora_fin = ?, estado = ? WHERE id_reserva = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setLong(2, this.idPlaza);
            stmt.setString(3, this.fechaHoraInicio);
            stmt.setString(4, this.fechaHoraFin);
            stmt.setString(5, this.estado);
            stmt.setLong(6, this.idReserva);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una reserva
    public void eliminar() {
        String sql = "DELETE FROM Reservas WHERE id_reserva = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idReserva);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
