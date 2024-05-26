package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class RegistroEntradaSalida {

    private Long idRegistro;
    private Long idVehiculo;
    private String fechaHoraEntrada;
    private String fechaHoraSalida;
    private Long idPlaza;

    public RegistroEntradaSalida(Long idRegistro, Long idVehiculo, String fechaHoraEntrada, String fechaHoraSalida, Long idPlaza) {
        this.idRegistro = idRegistro;
        this.idVehiculo = idVehiculo;
        this.fechaHoraEntrada = fechaHoraEntrada;
        this.fechaHoraSalida = fechaHoraSalida;
        this.idPlaza = idPlaza;
    }

    // Getters y setters
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

    public String getFechaHoraEntrada() {
        return fechaHoraEntrada;
    }

    public void setFechaHoraEntrada(String fechaHoraEntrada) {
        this.fechaHoraEntrada = fechaHoraEntrada;
    }

    public String getFechaHoraSalida() {
        return fechaHoraSalida;
    }

    public void setFechaHoraSalida(String fechaHoraSalida) {
        this.fechaHoraSalida = fechaHoraSalida;
    }

    public Long getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(Long idPlaza) {
        this.idPlaza = idPlaza;
    }

    // Método para obtener todos los registros de entradas y salidas
    public static List<RegistroEntradaSalida> obtenerTodos() {
        List<RegistroEntradaSalida> registros = new ArrayList<>();
        String sql = "SELECT * FROM EntradasSalidas";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RegistroEntradaSalida registro = new RegistroEntradaSalida(
                        rs.getLong("id_registro"),
                        rs.getLong("id_vehículo"),
                        rs.getString("fecha_hora_entrada"),
                        rs.getString("fecha_hora_salida"),
                        rs.getLong("id_plaza")
                );
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros;
    }

    // Método para guardar un registro de entrada y salida
    public void guardar() {
        String sql = "INSERT INTO EntradasSalidas (id_vehículo, fecha_hora_entrada, fecha_hora_salida, id_plaza) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.setString(2, this.fechaHoraEntrada);
            stmt.setString(3, this.fechaHoraSalida);
            stmt.setLong(4, this.idPlaza);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un registro de entrada y salida
    public void actualizar() {
        String sql = "UPDATE EntradasSalidas SET id_vehículo = ?, fecha_hora_entrada = ?, fecha_hora_salida = ?, id_plaza = ? WHERE id_registro = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.setString(2, this.fechaHoraEntrada);
            stmt.setString(3, this.fechaHoraSalida);
            stmt.setLong(4, this.idPlaza);
            stmt.setLong(5, this.idRegistro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un registro de entrada y salida
    public void eliminar() {
        String sql = "DELETE FROM EntradasSalidas WHERE id_registro = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idRegistro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
