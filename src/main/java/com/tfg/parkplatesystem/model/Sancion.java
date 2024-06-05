package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Sancion {

    private Long idSancion;
    private Long idVehiculo;
    private Long idUsuario;
    private String motivo;
    private Double monto;
    private LocalDateTime fechaHora;
    private LocalDateTime fechaMaxPago;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Sancion(Long idSancion, Long idVehiculo, Long idUsuario, String motivo, Double monto, LocalDateTime fechaHora, LocalDateTime fechaMaxPago) {
        this.idSancion = idSancion;
        this.idVehiculo = idVehiculo;
        this.idUsuario = idUsuario;
        this.motivo = motivo;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.fechaMaxPago = fechaMaxPago;
    }

    // Getters y setters
    public Long getIdSancion() {
        return idSancion;
    }

    public void setIdSancion(Long idSancion) {
        this.idSancion = idSancion;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public LocalDateTime getFechaMaxPago() {
        return fechaMaxPago;
    }

    public void setFechaMaxPago(LocalDateTime fechaMaxPago) {
        this.fechaMaxPago = fechaMaxPago;
    }

    public String getFechaHoraFormateada() {
        return this.fechaHora.format(FORMATTER);
    }

    public String getFechaMaxPagoFormateada() {
        return this.fechaMaxPago.format(FORMATTER);
    }

    // Método para obtener todas las sanciones
    public static List<Sancion> obtenerTodas() {
        List<Sancion> sanciones = new ArrayList<>();
        String sql = "SELECT * FROM Sanciones";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Sancion sancion = new Sancion(
                        rs.getLong("id_sancion"),
                        rs.getLong("id_vehiculo"),
                        rs.getLong("id_usuario"),
                        rs.getString("motivo"),
                        rs.getDouble("monto"),
                        rs.getTimestamp("fecha_hora").toLocalDateTime(),
                        rs.getTimestamp("fecha_max_pago").toLocalDateTime()
                );
                sanciones.add(sancion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sanciones;
    }

    // Método para guardar una sanción
    public void guardar() {
        String sql = "INSERT INTO Sanciones (id_vehiculo, id_usuario, motivo, monto, fecha_hora, fecha_max_pago) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.setLong(2, this.idUsuario);
            stmt.setString(3, this.motivo);
            stmt.setDouble(4, this.monto);
            stmt.setTimestamp(5, Timestamp.valueOf(this.fechaHora));
            stmt.setTimestamp(6, Timestamp.valueOf(this.fechaMaxPago));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una sanción
    public void actualizar() {
        String sql = "UPDATE Sanciones SET id_vehiculo = ?, id_usuario = ?, motivo = ?, monto = ?, fecha_hora = ?, fecha_max_pago = ? WHERE id_sancion = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.setLong(2, this.idUsuario);
            stmt.setString(3, this.motivo);
            stmt.setDouble(4, this.monto);
            stmt.setTimestamp(5, Timestamp.valueOf(this.fechaHora));
            stmt.setTimestamp(6, Timestamp.valueOf(this.fechaMaxPago));
            stmt.setLong(7, this.idSancion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una sanción
    public void eliminar() {
        String sql = "DELETE FROM Sanciones WHERE id_sancion = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idSancion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
