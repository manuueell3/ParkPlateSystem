package com.tfg.parkplatesystem.model;

import javafx.beans.property.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Mantenimiento {

    private LongProperty idMantenimiento;
    private LongProperty idPlaza;
    private StringProperty descripcion;
    private StringProperty fechaInicio;
    private StringProperty fechaFin;
    private StringProperty estado;

    public Mantenimiento(Long idPlaza, String descripcion, String fechaInicio, String fechaFin, String estado) {
        this.idMantenimiento = new SimpleLongProperty();
        this.idPlaza = new SimpleLongProperty(idPlaza);
        this.descripcion = new SimpleStringProperty(descripcion);
        this.fechaInicio = new SimpleStringProperty(fechaInicio);
        this.fechaFin = new SimpleStringProperty(fechaFin);
        this.estado = new SimpleStringProperty(estado);
    }

    public long getIdMantenimiento() {
        return idMantenimiento.get();
    }

    public LongProperty idMantenimientoProperty() {
        return idMantenimiento;
    }

    public void setIdMantenimiento(long idMantenimiento) {
        this.idMantenimiento.set(idMantenimiento);
    }

    public long getIdPlaza() {
        return idPlaza.get();
    }

    public LongProperty idPlazaProperty() {
        return idPlaza;
    }

    public void setIdPlaza(long idPlaza) {
        this.idPlaza.set(idPlaza);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getFechaInicio() {
        return fechaInicio.get();
    }

    public StringProperty fechaInicioProperty() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio.set(fechaInicio);
    }

    public String getFechaFin() {
        return fechaFin.get();
    }

    public StringProperty fechaFinProperty() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin.set(fechaFin);
    }

    public String getEstado() {
        return estado.get();
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public static List<Mantenimiento> obtenerTodos() throws SQLException {
        List<Mantenimiento> mantenimientos = new ArrayList<>();
        Connection connection = UtilMysql.getConnection();
        String query = "SELECT * FROM Mantenimientos";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Mantenimiento mantenimiento = new Mantenimiento(
                    resultSet.getLong("id_plaza"),
                    resultSet.getString("descripción"),
                    resultSet.getString("fecha_inicio"),
                    resultSet.getString("fecha_fin"),
                    resultSet.getString("estado")
            );
            mantenimiento.setIdMantenimiento(resultSet.getLong("id_mantenimiento"));
            mantenimientos.add(mantenimiento);
        }
        connection.close();
        return mantenimientos;
    }

    public void guardar() throws SQLException {
        Connection connection = UtilMysql.getConnection();
        String query = "INSERT INTO Mantenimientos (id_plaza, descripción, fecha_inicio, fecha_fin, estado) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setLong(1, this.getIdPlaza());
        statement.setString(2, this.getDescripcion());
        statement.setString(3, this.getFechaInicio());
        statement.setString(4, this.getFechaFin());
        statement.setString(5, this.getEstado());
        statement.executeUpdate();
        ResultSet generatedKeys = statement.getGeneratedKeys();
        if (generatedKeys.next()) {
            this.setIdMantenimiento(generatedKeys.getLong(1));
        }
        connection.close();
    }

    public void actualizar() throws SQLException {
        Connection connection = UtilMysql.getConnection();
        String query = "UPDATE Mantenimientos SET id_plaza = ?, descripción = ?, fecha_inicio = ?, fecha_fin = ?, estado = ? WHERE id_mantenimiento = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, this.getIdPlaza());
        statement.setString(2, this.getDescripcion());
        statement.setString(3, this.getFechaInicio());
        statement.setString(4, this.getFechaFin());
        statement.setString(5, this.getEstado());
        statement.setLong(6, this.getIdMantenimiento());
        statement.executeUpdate();
        connection.close();
    }

    public void eliminar() throws SQLException {
        Connection connection = UtilMysql.getConnection();
        String query = "DELETE FROM Mantenimientos WHERE id_mantenimiento = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setLong(1, this.getIdMantenimiento());
        statement.executeUpdate();
        connection.close();
    }
}
