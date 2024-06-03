package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pago {

    private Long idPago;
    private Long idUsuario;
    private Long idRegistro;
    private Long idVehiculo;
    private Double monto;
    private String fechaHoraPago;
    private String formaPago;

    private static final Logger LOGGER = Logger.getLogger(Pago.class.getName());

    public Pago(Long idPago, Long idUsuario, Long idRegistro, Long idVehiculo, Double monto, String fechaHoraPago, String formaPago) {
        if (idUsuario == null || idRegistro == null || idVehiculo == null || monto == null || fechaHoraPago == null || formaPago == null) {
            throw new IllegalArgumentException("Todos los campos deben ser completados.");
        }
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
    public static List<Pago> obtenerTodos() throws SQLException {
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
                        rs.getLong("id_vehiculo"),
                        rs.getDouble("monto"),
                        rs.getString("fecha_hora_pago"),
                        rs.getString("forma_pago")
                );
                pagos.add(pago);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los pagos", e);
            throw e;
        }
        return pagos;
    }

    // Método para guardar un pago
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Pagos (id_usuario, id_registro, id_vehiculo, monto, fecha_hora_pago, forma_pago) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!validarCampos()) {
                throw new SQLException("Datos del pago no válidos");
            }

            stmt.setLong(1, this.idUsuario);
            stmt.setLong(2, this.idRegistro);
            stmt.setLong(3, this.idVehiculo);
            stmt.setDouble(4, this.monto);
            stmt.setString(5, this.fechaHoraPago);
            stmt.setString(6, this.formaPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al guardar el pago", e);
            throw e;
        }
    }

    // Método para actualizar un pago
    public void actualizar() throws SQLException {
        String sql = "UPDATE Pagos SET id_usuario = ?, id_registro = ?, id_vehiculo = ?, monto = ?, fecha_hora_pago = ?, forma_pago = ? WHERE id_pago = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!validarCampos()) {
                throw new SQLException("Datos del pago no válidos");
            }

            stmt.setLong(1, this.idUsuario);
            stmt.setLong(2, this.idRegistro);
            stmt.setLong(3, this.idVehiculo);
            stmt.setDouble(4, this.monto);
            stmt.setString(5, this.fechaHoraPago);
            stmt.setString(6, this.formaPago);
            stmt.setLong(7, this.idPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al actualizar el pago", e);
            throw e;
        }
    }

    // Método para eliminar un pago
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Pagos WHERE id_pago = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idPago);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al eliminar el pago", e);
            throw e;
        }
    }

    // Validaciones de los campos del pago
    private boolean validarCampos() {
        if (this.idUsuario == null || this.idRegistro == null || this.idVehiculo == null || this.monto == null || this.fechaHoraPago == null || this.formaPago == null) {
            LOGGER.log(Level.WARNING, "Campos del pago no válidos");
            return false;
        }
        return true;
    }
}
