package com.example.ranchomuu.model;

public class Pedidos
{
    String id, cantidadProducto, nombreProducto, precioProducto;

    public Pedidos() {
    }

    public Pedidos(String id, String cantidadProducto, String nombreProducto, String precioProducto) {
        this.id = id;
        this.cantidadProducto = cantidadProducto;
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(String cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getPrecioProducto() {
        return precioProducto;
    }

    public void setPrecioProducto(String precioProducto) {
        this.precioProducto = precioProducto;
    }
}
