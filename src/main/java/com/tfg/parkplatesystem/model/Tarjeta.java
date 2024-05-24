package com.tfg.parkplatesystem.model;

public class Tarjeta {

    private Long id;
    private String usuario;
    private String numero;
    private String fechaExpiracion;

    public Tarjeta(Long id, String usuario, String numero, String fechaExpiracion) {
        this.id = id;
        this.usuario = usuario;
        this.numero = numero;
        this.fechaExpiracion = fechaExpiracion;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(String fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }
}
