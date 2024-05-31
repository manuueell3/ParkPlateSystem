package com.tfg.parkplatesystem.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.tfg.parkplatesystem.util.UtilMysql;

public class Usuario {

    private Long idUsuario;
    private String nombre;
    private String apellidos;
    private String email;
    private String contraseña;
    private String rol;
    private String fechaAlta;

    public Usuario(Long idUsuario, String nombre, String apellidos, String email, String contraseña, String rol, String fechaAlta) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.contraseña = contraseña;
        this.rol = rol;
        this.fechaAlta = fechaAlta;
    }

    // Getters y setters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    // Método para obtener todos los usuarios
    public static List<Usuario> obtenerTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getLong("id_usuario"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("contraseña"),
                        rs.getString("rol"),
                        rs.getString("fecha_alta")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método para guardar un usuario
    public void guardar() throws SQLException {
        String sql = "INSERT INTO Usuarios (nombre, apellidos, email, contraseña, rol, fecha_alta) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.nombre);
            stmt.setString(2, this.apellidos);
            stmt.setString(3, this.email);
            stmt.setString(4, this.contraseña);
            stmt.setString(5, this.rol);
            stmt.setString(6, this.fechaAlta);
            stmt.executeUpdate();
        }
    }

    // Método para actualizar un usuario
    public void actualizar() throws SQLException {
        String sql = "UPDATE Usuarios SET nombre = ?, apellidos = ?, email = ?, contraseña = ?, rol = ?, fecha_alta = ? WHERE id_usuario = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, this.nombre);
            stmt.setString(2, this.apellidos);
            stmt.setString(3, this.email);
            stmt.setString(4, this.contraseña);
            stmt.setString(5, this.rol);
            stmt.setString(6, this.fechaAlta);
            stmt.setLong(7, this.idUsuario);
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un usuario
    public void eliminar() throws SQLException {
        String sql = "DELETE FROM Usuarios WHERE id_usuario = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, this.idUsuario);
            stmt.executeUpdate();
        }
    }

    // Método para verificar las credenciales de un usuario
    public static Usuario verificarCredenciales(String email, String contrasena) {
        String sql = "SELECT * FROM Usuarios WHERE email = ? AND contraseña = ?";
        try (Connection conn = UtilMysql.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, contrasena);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getLong("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            rs.getString("email"),
                            rs.getString("contraseña"),
                            rs.getString("rol"),
                            rs.getString("fecha_alta")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para verificar si el usuario es administrador
    public boolean esAdministrador() {
        return "administrador".equalsIgnoreCase(this.rol);
    }
}
