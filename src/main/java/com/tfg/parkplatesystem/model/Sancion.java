package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Sancion {

    private Long idSancion;
    private Long idVehiculo;
    private Long idUsuario;
    private String motivo;
    private Double monto;
    private String fechaHora;
    private String fechaMaxPago;

    public Sancion(Long idSancion, Long idVehiculo, Long idUsuario, String motivo, Double monto, String fechaHora, String fechaMaxPago) {
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

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFechaMaxPago() {
        return fechaMaxPago;
    }

    public void setFechaMaxPago(String fechaMaxPago) {
        this.fechaMaxPago = fechaMaxPago;
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
                        rs.getLong("id_sanción"),
                        rs.getLong("id_vehículo"),
                        rs.getLong("id_usuario"),
                        rs.getString("motivo"),
                        rs.getDouble("monto"),
                        rs.getString("fecha_hora"),
                        rs.getString("fecha_max_pago")
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
        String sql = "INSERT INTO Sanciones (id_vehículo, id_usuario, motivo, monto, fecha_hora, fecha_max_pago) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.setLong(2, this.idUsuario);
            stmt.setString(3, this.motivo);
            stmt.setDouble(4, this.monto);
            stmt.setString(5, this.fechaHora);
            stmt.setString(6, this.fechaMaxPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una sanción
    public void actualizar() {
        String sql = "UPDATE Sanciones SET id_vehículo = ?, id_usuario = ?, motivo = ?, monto = ?, fecha_hora = ?, fecha_max_pago = ? WHERE id_sanción = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.setLong(2, this.idUsuario);
            stmt.setString(3, this.motivo);
            stmt.setDouble(4, this.monto);
            stmt.setString(5, this.fechaHora);
            stmt.setString(6, this.fechaMaxPago);
            stmt.setLong(7, this.idSancion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una sanción
    public void eliminar() {
        String sql = "DELETE FROM Sanciones WHERE id_sanción = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idSancion);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
