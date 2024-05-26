package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class PlazaAparcamiento {

    private Long idPlaza;
    private Integer numeroPlaza;
    private String estado;
    private String fechaBloqueo;
    private String fechaAlta;

    public PlazaAparcamiento(Long idPlaza, Integer numeroPlaza, String estado, String fechaBloqueo, String fechaAlta) {
        this.idPlaza = idPlaza;
        this.numeroPlaza = numeroPlaza;
        this.estado = estado;
        this.fechaBloqueo = fechaBloqueo;
        this.fechaAlta = fechaAlta;
    }

    // Getters y setters
    public Long getIdPlaza() {
        return idPlaza;
    }

    public void setIdPlaza(Long idPlaza) {
        this.idPlaza = idPlaza;
    }

    public Integer getNumeroPlaza() {
        return numeroPlaza;
    }

    public void setNumeroPlaza(Integer numeroPlaza) {
        this.numeroPlaza = numeroPlaza;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(String fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    // Método para obtener todas las plazas de aparcamiento
    public static List<PlazaAparcamiento> obtenerTodas() {
        List<PlazaAparcamiento> plazas = new ArrayList<>();
        String sql = "SELECT * FROM Plazas";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PlazaAparcamiento plaza = new PlazaAparcamiento(
                        rs.getLong("id_plaza"),
                        rs.getInt("número_plaza"),
                        rs.getString("estado"),
                        rs.getString("fecha_bloqueo"),
                        rs.getString("fecha_alta")
                );
                plazas.add(plaza);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return plazas;
    }

    // Método para guardar una plaza de aparcamiento
    public void guardar() {
        String sql = "INSERT INTO Plazas (número_plaza, estado, fecha_bloqueo, fecha_alta) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.numeroPlaza);
            stmt.setString(2, this.estado);
            stmt.setString(3, this.fechaBloqueo);
            stmt.setString(4, this.fechaAlta);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para actualizar una plaza de aparcamiento
    public void actualizar() {
        String sql = "UPDATE Plazas SET número_plaza = ?, estado = ?, fecha_bloqueo = ?, fecha_alta = ? WHERE id_plaza = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.numeroPlaza);
            stmt.setString(2, this.estado);
            stmt.setString(3, this.fechaBloqueo);
            stmt.setString(4, this.fechaAlta);
            stmt.setLong(5, this.idPlaza);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar una plaza de aparcamiento
    public void eliminar() {
        String sql = "DELETE FROM Plazas WHERE id_plaza = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idPlaza);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
