package com.tfg.parkplatesystem.model;

public class Pago {

    private Long id;
    private String usuario;
    private Double monto;
    private String fecha;

    public Pago(Long id, String usuario, Double monto, String fecha) {
        this.id = id;
        this.usuario = usuario;
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
