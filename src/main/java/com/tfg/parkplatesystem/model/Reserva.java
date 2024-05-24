package com.tfg.parkplatesystem.model;

public class Reserva {

    private Long id;
    private String usuario;
    private String plaza;
    private String fecha;

    public Reserva(Long id, String usuario, String plaza, String fecha) {
        this.id = id;
        this.usuario = usuario;
        this.plaza = plaza;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPlaza() {
        return plaza;
    }

    public void setPlaza(String plaza) {
        this.plaza = plaza;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
