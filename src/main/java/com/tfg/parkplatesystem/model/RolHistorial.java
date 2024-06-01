package com.tfg.parkplatesystem.model;

import java.time.LocalDateTime;

public class RolHistorial {

    private Long idUsuario;
    private String nombreUsuario;
    private String rolAnterior;
    private String rolNuevo;
    private LocalDateTime fechaCambio;

    public RolHistorial(Long idUsuario, String nombreUsuario, String rolAnterior, String rolNuevo, LocalDateTime fechaCambio) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.rolAnterior = rolAnterior;
        this.rolNuevo = rolNuevo;
        this.fechaCambio = fechaCambio;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getRolAnterior() {
        return rolAnterior;
    }

    public String getRolNuevo() {
        return rolNuevo;
    }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }
}
