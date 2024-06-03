package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Registro {

    private Long idRegistro;
    private Long idUsuario;
    private String tipo;
    private String fecha;
    private String estado;

    public Registro(Long idRegistro, Long idUsuario, String tipo, String fecha, String estado) {
        this.idRegistro = idRegistro;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.fecha = fecha;
        this.estado = estado;
    }

    // Getters y setters
    public Long getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Long idRegistro) {
        this.idRegistro = idRegistro;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Método para obtener todos los registros
    public static List<Registro> obtenerTodos() {
        List<Registro> registros = new ArrayList<>();
        String sql = "SELECT * FROM Registros";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Registro registro = new Registro(
                        rs.getLong("id_registro"),
                        rs.getLong("id_usuario"),
                        rs.getString("tipo"),
                        rs.getString("fecha"),
                        rs.getString("estado")
                );
                registros.add(registro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return registros;
    }

    // Método para guardar un registro
    public void guardar() {
        String sql = "INSERT INTO Registros (id_usuario, tipo, fecha, estado) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.tipo);
            stmt.setString(3, this.fecha);
            stmt.setString(4, this.estado);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar un registro
    public void actualizar() {
        String sql = "UPDATE Registros SET id_usuario = ?, tipo = ?, fecha = ?, estado = ? WHERE id_registro = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.setString(2, this.tipo);
            stmt.setString(3, this.fecha);
            stmt.setString(4, this.estado);
            stmt.setLong(5, this.idRegistro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un registro
    public void eliminar() {
        String sql = "DELETE FROM Registros WHERE id_registro = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idRegistro);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
