package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Vehiculo {

    private Long idVehiculo;
    private String matricula;
    private String marca;
    private String modelo;
    private String color;
    private Long idUsuario;

    public Vehiculo(Long idVehiculo, String matricula, String marca, String modelo, String color, Long idUsuario) {
        this.idVehiculo = idVehiculo;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.color = color;
        this.idUsuario = idUsuario;
    }

    // Getters y setters
    public Long getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(Long idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Método para obtener todos los vehículos
    public static List<Vehiculo> obtenerTodos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT * FROM Vehículos";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vehiculo vehiculo = new Vehiculo(
                        rs.getLong("id_vehículo"),
                        rs.getString("matrícula"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("color"),
                        rs.getLong("id_usuario")
                );
                vehiculos.add(vehiculo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehiculos;
    }

    // Método para guardar un vehículo
    public void guardar() {
        String sql = "INSERT INTO Vehículos (matrícula, marca, modelo, color, id_usuario) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.matricula);
            stmt.setString(2, this.marca);
            stmt.setString(3, this.modelo);
            stmt.setString(4, this.color);
            stmt.setLong(5, this.idUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un vehículo
    public void actualizar() {
        String sql = "UPDATE Vehículos SET matrícula = ?, marca = ?, modelo = ?, color = ?, id_usuario = ? WHERE id_vehículo = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.matricula);
            stmt.setString(2, this.marca);
            stmt.setString(3, this.modelo);
            stmt.setString(4, this.color);
            stmt.setLong(5, this.idUsuario);
            stmt.setLong(6, this.idVehiculo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un vehículo
    public void eliminar() {
        String sql = "DELETE FROM Vehículos WHERE id_vehículo = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idVehiculo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
