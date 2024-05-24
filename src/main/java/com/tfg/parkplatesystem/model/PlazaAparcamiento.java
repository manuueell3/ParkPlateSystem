package com.tfg.parkplatesystem.model;

public class PlazaAparcamiento {

    private Long id;
    private String ubicacion;
    private String estado;

    public PlazaAparcamiento(Long id, String ubicacion, String estado) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
