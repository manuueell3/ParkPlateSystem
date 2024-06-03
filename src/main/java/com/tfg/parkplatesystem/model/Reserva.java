package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Reserva {

    private Integer idReserva;
    private Integer idUsuario;
    private Integer idPlaza;
    private String fechaHoraInicio;
    private String fechaHoraFin;
    private String estado;
    private String usuario;
    private String plaza;

    public Reserva(Integer idReserva, Integer idUsuario, Integer idPlaza, String fechaHoraInicio, String fechaHoraFin, String estado) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idPlaza = idPlaza;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = estado;
        this.usuario = obtenerNombreUsuario(idUsuario);
        this.plaza = obtenerNumeroPlaza(idPlaza);
    }

    // Getters y setters
    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(Integer idPlaza) {
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

    public String getUsuario() {
        return usuario;
    }

    public String getPlaza() {
        return plaza;
    }

    // Método para obtener todas las reservas
    public static List<Reserva> obtenerTodas() {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT r.*, u.nombre as usuario, p.numero_plaza as plaza FROM Reservas r "
                + "JOIN Usuarios u ON r.id_usuario = u.id_usuario "
                + "JOIN Plazas p ON r.id_plaza = p.id_plaza";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reserva reserva = new Reserva(
                        rs.getInt("id_reserva"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_plaza"),
                        rs.getString("fecha_hora_inicio"),
                        rs.getString("fecha_hora_fin"),
                        rs.getString("estado")
                );
                reserva.usuario = rs.getString("usuario");
                reserva.plaza = rs.getString("plaza");
                reservas.add(reserva);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservas;
    }

    // Método para guardar una reserva
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Reservas (id_usuario, id_plaza, fecha_hora_inicio, fecha_hora_fin, estado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.idUsuario);
            stmt.setInt(2, this.idPlaza);
            stmt.setString(3, this.fechaHoraInicio);
            stmt.setString(4, this.fechaHoraFin);
            stmt.setString(5, this.estado);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar una reserva
    public void actualizar() throws SQLException {
        String sql = "UPDATE Reservas SET id_usuario = ?, id_plaza = ?, fecha_hora_inicio = ?, fecha_hora_fin = ?, estado = ? WHERE id_reserva = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.idUsuario);
            stmt.setInt(2, this.idPlaza);
            stmt.setString(3, this.fechaHoraInicio);
            stmt.setString(4, this.fechaHoraFin);
            stmt.setString(5, this.estado);
            stmt.setInt(6, this.idReserva);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar una reserva
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Reservas WHERE id_reserva = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.idReserva);
            stmt.executeUpdate();
        }
    }

    // Método para obtener el nombre de usuario
    private String obtenerNombreUsuario(Integer idUsuario) {
        String nombreUsuario = "";
        String sql = "SELECT nombre FROM Usuarios WHERE id_usuario = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idUsuario);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    nombreUsuario = rs.getString("nombre");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nombreUsuario;
    }

    // Método para obtener el número de plaza
    private String obtenerNumeroPlaza(Integer idPlaza) {
        String numeroPlaza = "";
        String sql = "SELECT numero_plaza FROM Plazas WHERE id_plaza = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPlaza);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    numeroPlaza = rs.getString("numero_plaza");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numeroPlaza;
    }
}
