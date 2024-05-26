package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Pago {

    private Long idPago;
    private Long idUsuario;
    private Long idRegistro;
    private Long idVehiculo;
    private Double monto;
    private String fechaHoraPago;
    private String formaPago;

    public Pago(Long idPago, Long idUsuario, Long idRegistro, Long idVehiculo, Double monto, String fechaHoraPago, String formaPago) {
        this.idPago = idPago;
        this.idUsuario = idUsuario;
        this.idRegistro = idRegistro;
        this.idVehiculo = idVehiculo;
        this.monto = monto;
        this.fechaHoraPago = fechaHoraPago;
        this.formaPago = formaPago;
    }

    // Getters y setters
    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getFechaHoraPago() {
        return fechaHoraPago;
    }

    public void setFechaHoraPago(String fechaHoraPago) {
        this.fechaHoraPago = fechaHoraPago;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    // Método para obtener todos los pagos
    public static List<Pago> obtenerTodos() {
        List<Pago> pagos = new ArrayList<>();
        String sql = "SELECT * FROM Pagos";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Pago pago = new Pago(
                        rs.getLong("id_pago"),
                        rs.getLong("id_usuario"),
                        rs.getLong("id_registro"),
                        rs.getLong("id_vehículo"),
                        rs.getDouble("monto"),
                        rs.getString("fecha_hora_pago"),
                        rs.getString("forma_pago")
                );
                pagos.add(pago);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pagos;
    }

    // Método para guardar un pago
    public void guardar() {
        String sql = "INSERT INTO Pagos (id_usuario, id_registro, id_vehículo, monto, fecha_hora_pago, forma_pago) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setLong(2, this.idRegistro);
            stmt.setLong(3, this.idVehiculo);
            stmt.setDouble(4, this.monto);
            stmt.setString(5, this.fechaHoraPago);
            stmt.setString(6, this.formaPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un pago
    public void actualizar() {
        String sql = "UPDATE Pagos SET id_usuario = ?, id_registro = ?, id_vehículo = ?, monto = ?, fecha_hora_pago = ?, forma_pago = ? WHERE id_pago = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setLong(2, this.idRegistro);
            stmt.setLong(3, this.idVehiculo);
            stmt.setDouble(4, this.monto);
            stmt.setString(5, this.fechaHoraPago);
            stmt.setString(6, this.formaPago);
            stmt.setLong(7, this.idPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un pago
    public void eliminar() {
        String sql = "DELETE FROM Pagos WHERE id_pago = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
