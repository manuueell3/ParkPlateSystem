package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Plaza {

    private Long idPlaza;
    private Integer numeroPlaza;
    private String estado;
    private String fechaBloqueo;
    private String fechaAlta;

    public Plaza(Long idPlaza, Integer numeroPlaza, String estado, String fechaBloqueo, String fechaAlta) {
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
    public static List<Plaza> obtenerTodas() throws SQLException {
        List<Plaza> plazas = new ArrayList<>();
        String sql = "SELECT * FROM Plazas";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Plaza plaza = new Plaza(
                        rs.getLong("id_plaza"),
                        rs.getInt("numero_plaza"),
                        rs.getString("estado"),
                        rs.getString("fecha_bloqueo"),
                        rs.getString("fecha_alta")
                );
                plazas.add(plaza);
            }
        }
        return plazas;
    }

    // Método para guardar una nueva plaza
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Plazas (numero_plaza, estado, fecha_alta) VALUES (?, ?, NOW())";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, this.numeroPlaza);
            stmt.setString(2, this.estado);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar una plaza
    public void actualizar() throws SQLException {
        String sql = "UPDATE Plazas SET estado = ? WHERE id_plaza = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.estado);
            stmt.setLong(2, this.idPlaza);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar una plaza
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Plazas WHERE id_plaza = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idPlaza);
            stmt.executeUpdate();
        }
    }

    @Override
    public String toString() {
        return "Plaza " + numeroPlaza + " (ID: " + idPlaza + ")";
    }
}
