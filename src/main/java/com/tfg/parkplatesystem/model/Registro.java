package com.tfg.parkplatesystem.model;

public class Registro {

    private Long id;
    private String usuario;
    private String tipo;
    private String fecha;
    private String estado;

    public Registro(Long id, String usuario, String tipo, String fecha, String estado) {
        this.id = id;
        this.usuario = usuario;
        this.tipo = tipo;
        this.fecha = fecha;
        this.estado = estado;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
