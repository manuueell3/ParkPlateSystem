package com.tfg.parkplatesystem.model;

public class RegistroEntradaSalida {

    private Long id;
    private String vehiculo;
    private String fechaEntrada;
    private String fechaSalida;
    private String estado;

    public RegistroEntradaSalida(Long id, String vehiculo, String fechaEntrada, String fechaSalida, String estado) {
        this.id = id;
        this.vehiculo = vehiculo;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(String fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(String fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
