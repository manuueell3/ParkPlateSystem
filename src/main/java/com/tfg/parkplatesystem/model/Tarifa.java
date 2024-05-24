package com.tfg.parkplatesystem.model;

public class Tarifa {

    private Long id;
    private String descripcion;
    private Double precio;

    public Tarifa(Long id, String descripcion, Double precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
