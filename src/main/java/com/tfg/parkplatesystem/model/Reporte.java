package com.tfg.parkplatesystem.model;

import com.tfg.parkplatesystem.util.UtilMysql;
import javafx.beans.property.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Reporte {

    private LongProperty id;
    private StringProperty descripcion;
    private StringProperty fecha;
    private StringProperty tipo;
    private LongProperty idUsuario;

    public Reporte(Long id, String descripcion, String fecha, String tipo, Long idUsuario) {
        this.id = id != null ? new SimpleLongProperty(id) : new SimpleLongProperty();
        this.descripcion = new SimpleStringProperty(descripcion);
        this.fecha = new SimpleStringProperty(fecha);
        this.tipo = new SimpleStringProperty(tipo);
        this.idUsuario = new SimpleLongProperty(idUsuario);
    }

    // Getters y setters para las propiedades
    public LongProperty idProperty() {
        return id;
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public StringProperty fechaProperty() {
        return fecha;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public LongProperty idUsuarioProperty() {
        return idUsuario;
    }

    public long getId() {
        return id.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public String getFecha() {
        return fecha.get();
    }

    public String getTipo() {
        return tipo.get();
    }

    public long getIdUsuario() {
        return idUsuario.get();
    }

    public static List<Reporte> obtenerTodos() {
        List<Reporte> reportes = new ArrayList<>();
        String sql = "SELECT * FROM Reportes";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Reporte reporte = new Reporte(
                        rs.getLong("id_reporte"),
                        rs.getString("descripcion"),
                        rs.getString("fecha"),
                        rs.getString("tipo"),
                        rs.getLong("id_usuario")
                );
                reportes.add(reporte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reportes;
    }

    public void guardar() {
        String sql = "INSERT INTO Reportes (descripcion, fecha, tipo, id_usuario) VALUES (?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, this.descripcion.get());
            stmt.setString(2, this.fecha.get());
            stmt.setString(3, this.tipo.get());
            stmt.setLong(4, this.idUsuario.get());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    this.id.set(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminar() {
        String sql = "DELETE FROM Reportes WHERE id_reporte = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.id.get());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
