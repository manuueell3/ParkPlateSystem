package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Tarjeta {

    private Long idTarjeta;
    private Long idUsuario;
    private String numeroTarjeta;
    private String fechaExpiracion;
    private String cvv;

    public Tarjeta(Long idTarjeta, Long idUsuario, String numeroTarjeta, String fechaExpiracion, String cvv) {
        this.idTarjeta = idTarjeta;
        this.idUsuario = idUsuario;
        this.numeroTarjeta = numeroTarjeta;
        this.fechaExpiracion = fechaExpiracion;
        this.cvv = cvv;
    }

    // Getters y setters
    public Long getIdTarjeta() {
        return idTarjeta;
    }

    public void setIdTarjeta(Long idTarjeta) {
        this.idTarjeta = idTarjeta;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    // Método para obtener todas las tarjetas
    public static List<Tarjeta> obtenerTodas() {
        List<Tarjeta> tarjetas = new ArrayList<>();
        String sql = "SELECT * FROM Tarjetas";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarjeta tarjeta = new Tarjeta(
                        rs.getLong("id_tarjeta"),
                        rs.getLong("id_usuario"),
                        rs.getString("número_tarjeta"),
                        rs.getString("fecha_expiración"),
                        rs.getString("cvv")
                );
                tarjetas.add(tarjeta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tarjetas;
    }

    // Método para guardar una tarjeta
    public void guardar() {
        String sql = "INSERT INTO Tarjetas (id_usuario, número_tarjeta, fecha_expiración, cvv) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.numeroTarjeta);
            stmt.setString(3, this.fechaExpiracion);
            stmt.setString(4, this.cvv);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una tarjeta
    public void actualizar() {
        String sql = "UPDATE Tarjetas SET número_tarjeta = ?, fecha_expiración = ?, cvv = ? WHERE id_tarjeta = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.numeroTarjeta);
            stmt.setString(2, this.fechaExpiracion);
            stmt.setString(3, this.cvv);
            stmt.setLong(4, this.idTarjeta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una tarjeta
    public void eliminar() {
        String sql = "DELETE FROM Tarjetas WHERE id_tarjeta = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idTarjeta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
