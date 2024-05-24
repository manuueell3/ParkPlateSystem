package com.tfg.parkplatesystem.model;

public class Sancion {

    private Long id;
    private String usuario;
    private String descripcion;
    private Double monto;
    private String fecha;

    public Sancion(Long id, String usuario, String descripcion, Double monto, String fecha) {
        this.id = id;
        this.usuario = usuario;
        this.descripcion = descripcion;
        this.monto = monto;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
