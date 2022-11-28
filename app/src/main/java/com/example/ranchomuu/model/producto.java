package com.example.ranchomuu.model;

public class producto
{
    String nombreProducto, precioProducto, cantidadProducto, photo;

    public producto()
    {

    }

    public producto(String nombreProducto, String precioProducto, String cantidadProducto, String photo) {
        this.nombreProducto = nombreProducto;
        this.precioProducto = precioProducto;
        this.cantidadProducto = cantidadProducto;
        this.photo = photo;
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

    public String getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(String cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "producto{" +
                "nombreProducto='" + nombreProducto + '\'' +
                ", precioProducto='" + precioProducto + '\'' +
                ", cantidadProducto='" + cantidadProducto + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }
}
